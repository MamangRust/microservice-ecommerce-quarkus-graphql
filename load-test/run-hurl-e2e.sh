#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"
BASE_URL="${GATEWAY_URL:-http://127.0.0.1:8080}"
EMAIL="$(docker exec ecommerce-postgres psql -U DRAGON -d PAYMENT_GATEWAY -Atc "select email from users where email like 'freebuff.fulltest.%@example.test' or username like 'freebuff_fulltest_%' or username like 'fulltest_%' order by id desc limit 1" | tr -d '\r')"
if [[ -z "$EMAIL" ]]; then echo 'No full-test fixture user found' >&2; exit 2; fi
USER_ID="$(docker exec ecommerce-postgres psql -U DRAGON -d PAYMENT_GATEWAY -Atc "select id from users where email='$EMAIL'" | tr -d '\r')"
if [[ -z "$USER_ID" ]]; then echo 'Fixture user has no id' >&2; exit 2; fi

# Keep the fixture username valid for the database constraint without touching
# any business entity data.
docker exec ecommerce-postgres psql -U DRAGON -d PAYMENT_GATEWAY -v ON_ERROR_STOP=1 -Atc "update users set username='fulltest_${USER_ID}' where id=${USER_ID}" >/dev/null

TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
LOGIN_JSON="$TMP/login.json"
python3 - "$BASE_URL" "$EMAIL" >"$LOGIN_JSON" <<'PY'
import json,sys,urllib.request
base,email=sys.argv[1:]
r=urllib.request.urlopen(urllib.request.Request(base+'/api/auth/login',data=json.dumps({'email':email,'password':'FreebuffTest!2026'}).encode(),headers={'Content-Type':'application/json'},method='POST'),timeout=30)
print(r.read().decode())
PY
USER_TOKEN="$(python3 - "$LOGIN_JSON" <<'PY'
import json,sys
print(json.load(open(sys.argv[1]))['data']['accessToken'])
PY
)"
ADMIN_TOKEN="$(python3 - "$USER_ID" "$EMAIL" <<'PY'
import base64,json,subprocess,sys,time
uid,sub=sys.argv[1:]
def b(v): return base64.urlsafe_b64encode(v).rstrip(b'=').decode()
h=b(json.dumps({'typ':'JWT','alg':'RS256'},separators=(',',':')).encode())
p=b(json.dumps({'iss':'https://example-quarkus-opentelemetry.com','sub':sub,'iat':int(time.time()),'exp':int(time.time())+86400,'userId':int(uid),'groups':['ROLE_ADMIN','ROLE_USER','user']},separators=(',',':')).encode())
u=f'{h}.{p}'.encode(); sig=subprocess.run(['openssl','dgst','-sha256','-sign','auth/src/main/resources/privateKey.pem'],input=u,capture_output=True,check=True).stdout
print(f'{h}.{p}.{b(sig)}')
PY
)"

# IDs for read-only endpoint coverage. Empty tables are not fabricated: if a
# fixture is absent, use 1 and the report records the endpoint response.
psql() { docker exec ecommerce-postgres psql -U DRAGON -d PAYMENT_GATEWAY -Atc "$1" | tr -d '\r'; }

# Self-heal: jaga sequence roles tetap sinkron dengan max(id) supaya POST /api/roles
# tidak gagal duplicate-key (seed/manual insert bisa membuat drift).
docker exec ecommerce-postgres psql -U DRAGON -d PAYMENT_GATEWAY -v ON_ERROR_STOP=1 -Atc \
  "select setval('roles_id_seq', greatest((select max(id) from roles), 1))" >/dev/null 2>&1 || true

cat >"$TMP/vars.properties" <<EOF
base_url=$BASE_URL
email=$EMAIL
password=FreebuffTest!2026
user_id=$USER_ID
fixture_user_id=$USER_ID
product_id=$(psql "select coalesce((select id from products where deleted_at is null order by id desc limit 1),1)")
merchant_id=$(psql "select coalesce((select merchant_id from merchants where deleted_at is null order by merchant_id desc limit 1),1)")
order_id=$(psql "select coalesce((select id from orders where deleted_at is null order by id desc limit 1),1)")
role_id=$(psql "select coalesce((select id from roles where deleted_at is null order by id desc limit 1),1)")
banner_id=$(psql "select coalesce((select id from banners where deleted_at is null order by id desc limit 1),1)")
user_token=$USER_TOKEN
admin_token=$ADMIN_TOKEN
refresh_token=$(python3 -c 'import json,sys; print(json.load(open(sys.argv[1]))["data"]["refreshToken"])' "$LOGIN_JSON")
marker=HURL_E2E_$(date +%s)
marker_lower=hurl_e2e_$(date +%s)
fixture_banner_id=$(psql "select coalesce((select id from banners where deleted_at is null order by id desc limit 1),1)")
fixture_category_id=$(psql "select coalesce((select id from categories where deleted_at is null order by id desc limit 1),1)")
fixture_merchant_id=$(psql "select coalesce((select merchant_id from merchants where deleted_at is null order by merchant_id desc limit 1),1)")
fixture_product_id=$(psql "select coalesce((select id from products where deleted_at is null order by id desc limit 1),1)")
fixture_order_id=$(psql "select coalesce((select id from orders where deleted_at is null order by id desc limit 1),1)")
fixture_review_id=$(psql "select coalesce((select id from reviews where deleted_at is null order by id desc limit 1),1)")
fixture_role_id=$(psql "select coalesce((select id from roles where deleted_at is null order by id desc limit 1),1)")
fixture_slider_id=$(psql "select coalesce((select id from sliders where deleted_at is null order by id desc limit 1),1)")
fixture_transaction_id=$(psql "select coalesce((select id from transactions where deleted_at is null order by id desc limit 1),1)")
fixture_shipping_id=$(psql "select coalesce((select id from shipping_addresses where deleted_at is null order by id desc limit 1),1)")
fixture_detail_id=$(psql "select coalesce((select id from merchant_details where deleted_at is null order by id desc limit 1),0)")
fixture_business_id=$(psql "select coalesce((select id from merchant_business_information where deleted_at is null order by id desc limit 1),0)")
fixture_policy_id=$(psql "select coalesce((select id from merchant_policies where deleted_at is null order by id desc limit 1),0)")
fixture_award_id=$(psql "select coalesce((select id from merchant_certifications_and_awards where deleted_at is null order by id desc limit 1),0)")
fixture_document_id=$(psql "select coalesce((select document_id from merchant_documents where deleted_at is null order by document_id desc limit 1),0)")
fixture_review_detail_id=$(psql "select coalesce((select id from review_details where deleted_at is null order by id desc limit 1),0)")
EOF
python3 load-test/generate-hurl-e2e.py >/dev/null
rm -rf reports/hurl
mkdir -p reports/hurl/html reports/hurl/json
(
  cd "$ROOT/load-test/e2e"
  hurl --test --continue-on-error --file-root . --variables-file "$TMP/vars.properties" gateway-all-endpoints.hurl \
    --report-junit "$ROOT/reports/hurl/gateway-all-endpoints.junit.xml" \
    --report-html "$ROOT/reports/hurl/html" \
    --report-json "$ROOT/reports/hurl/json"
)
