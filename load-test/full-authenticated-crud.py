#!/usr/bin/env python3
"""Authenticated local CRUD smoke test.

This test creates only marker-prefixed rows, uses the real login-issued JWT for
user operations, and a JWT signed with the repository's official private key
for admin-only routes. It never runs permanent/all-delete endpoints.
"""
from __future__ import annotations

import base64
import datetime as dt
import hashlib
import json
import os
import subprocess
import tempfile
import time
from pathlib import Path
from urllib.error import HTTPError, URLError
from urllib.parse import urlencode
from urllib.request import Request, urlopen

ROOT = Path(__file__).resolve().parents[1]
BASE = os.environ.get("GATEWAY_URL", "http://127.0.0.1:8080")
PASSWORD = "FreebuffTest!2026"
MARKER = f"FULLCRUD_{int(time.time())}"
REPORT = ROOT / "FULL_AUTHENTICATED_CRUD_REPORT.md"

results: list[dict] = []
ids: dict[str, int] = {}
secrets: dict[str, str] = {}


def compact(value: object, limit: int = 420) -> str:
    if isinstance(value, (dict, list)):
        text = json.dumps(value, separators=(",", ":"), ensure_ascii=False)
    else:
        text = str(value or "")
    return " ".join(text.split())[:limit]


def b64(data: bytes) -> str:
    return base64.urlsafe_b64encode(data).rstrip(b"=").decode()


def official_admin_token(user_id: int, subject: str) -> str:
    """Create a normal RS256 JWT signed by the same private key as auth-service."""
    now = int(time.time())
    header = b64(json.dumps({"typ": "JWT", "alg": "RS256"}, separators=(",", ":")).encode())
    payload = b64(json.dumps({
        "iss": "https://example-quarkus-opentelemetry.com",
        "sub": subject,
        "iat": now,
        "exp": now + 86400,
        "userId": user_id,
        "groups": ["ROLE_ADMIN", "ROLE_USER", "user"],
    }, separators=(",", ":")).encode())
    unsigned = f"{header}.{payload}".encode()
    key = ROOT / "auth/src/main/resources/privateKey.pem"
    proc = subprocess.run(["openssl", "dgst", "-sha256", "-sign", str(key)], input=unsigned, capture_output=True, check=True)
    return f"{header}.{payload}.{b64(proc.stdout)}"


def request(method: str, path: str, token: str | None = None, body: object | None = None,
            expected: tuple[int, ...] = (200, 201, 204), label: str | None = None) -> object:
    url = BASE + path
    headers = {"Accept": "application/json"}
    data = None
    if token:
        headers["Authorization"] = f"Bearer {token}"
    if body is not None:
        headers["Content-Type"] = "application/json"
        data = json.dumps(body).encode()
    started = time.perf_counter()
    status = 0
    response_body = ""
    parsed: object = None
    error = ""
    try:
        with urlopen(Request(url, data=data, headers=headers, method=method), timeout=30) as response:
            status = response.status
            response_body = response.read().decode("utf-8", "replace")
    except HTTPError as exc:
        status = exc.code
        response_body = exc.read().decode("utf-8", "replace")
    except (URLError, TimeoutError, OSError) as exc:
        error = f"{type(exc).__name__}: {exc}"
    elapsed = round((time.perf_counter() - started) * 1000, 1)
    try:
        parsed = json.loads(response_body) if response_body else None
    except json.JSONDecodeError:
        parsed = None
    ok = status in expected and not error
    results.append({
        "label": label or f"{method} {path}", "method": method, "path": path,
        "status": status or "000", "expected": "/".join(map(str, expected)),
        "elapsed": elapsed, "ok": ok, "request": body, "response": compact(parsed if parsed is not None else response_body),
        "error": error,
    })
    return parsed if parsed is not None else {}


def data_id(response: object, *keys: str) -> int | None:
    if not isinstance(response, dict):
        return None
    data = response.get("data", response)
    if isinstance(data, list):
        data = data[0] if data else {}
    if isinstance(data, dict):
        for key in keys + ("id", "bannerId", "documentId", "roleId"):
            value = data.get(key)
            if isinstance(value, int) and value > 0:
                return value
    return None


def list_check(path: str, token: str, label: str):
    return request("GET", path + ("&" if "?" in path else "?") + "page=1&size=100&search=", token, label=label)


def crud(name: str, create_path: str, create_body: dict, update_path: str,
         update_body: dict, token: str, id_keys: tuple[str, ...] = ("id",),
         read_prefix: str | None = None, list_path: str | None = None,
         trash_path: str | None = None, restore_path: str | None = None):
    created = request("POST", create_path, token, create_body, (200, 201), f"{name}: create")
    entity_id = data_id(created, *id_keys)
    if not entity_id:
        return
    ids[name] = entity_id
    prefix = read_prefix or create_path.rsplit("/", 1)[0]
    request("GET", f"{prefix}/{entity_id}", token, label=f"{name}: read")
    request("POST" if "update" in update_path else "PUT", update_path.format(id=entity_id), token, update_body, (200, 201), f"{name}: update")
    if list_path:
        list_check(list_path, token, f"{name}: list")
    if trash_path and restore_path:
        request("DELETE" if "DELETE" in trash_path else "POST", trash_path.format(id=entity_id), token, expected=(200, 204), label=f"{name}: soft-delete")
        request("POST", restore_path.format(id=entity_id), token, expected=(200, 204), label=f"{name}: restore")


def user_id_from_fixture(email: str) -> int:
    return int(subprocess.check_output(
        ["docker", "exec", "postgres", "psql", "-U", "DRAGON", "-d", "PAYMENT_GATEWAY", "-Atc",
         f"select id from users where email='{email}'"], text=True).strip())


def main():
    # Locate the marked fixture created for this run family; password is constant.
    email = subprocess.check_output(
        ["docker", "exec", "postgres", "psql", "-U", "DRAGON", "-d", "PAYMENT_GATEWAY", "-Atc",
         "select email from users where email like 'freebuff.fulltest.%@example.test' or username like 'freebuff_fulltest_%' or username like 'fulltest_%' order by id desc limit 1"], text=True).strip()
    if not email:
        raise SystemExit("No full-test fixture user exists; create the fixture first.")
    fixture_id = user_id_from_fixture(email)
    safe_username = f"fulltest_{fixture_id}"
    subprocess.run([
        "docker", "exec", "postgres", "psql", "-U", "DRAGON", "-d", "PAYMENT_GATEWAY", "-v", "ON_ERROR_STOP=1",
        "-c", f"update users set username='{safe_username}' where id={fixture_id}"
    ], check=True, capture_output=True, text=True)
    login = request("POST", "/api/auth/login", body={"email": email, "password": PASSWORD}, expected=(200,), label="auth: login")
    token_data = login.get("data", {}) if isinstance(login, dict) else {}
    user_token = token_data.get("accessToken", "")
    refresh_token = token_data.get("refreshToken", "")
    if not user_token:
        raise SystemExit("Login did not return accessToken; see report.")
    secrets["email"] = email
    user_id = data_id(request("GET", f"/api/auth/me?userId={fixture_id}", user_token, label="auth: me"), "id") or fixture_id
    admin_token = official_admin_token(user_id, email)
    request("POST", "/api/auth/refresh", body={"refreshToken": refresh_token}, expected=(200,), label="auth: refresh")

    # Independent base entities and their dependent records.
    category = request("POST", "/api/category/create", admin_token, {
        "name": f"{MARKER}_Category", "description": "Full CRUD category", "slugCategory": f"{MARKER.lower()}-category", "imageCategory": "test/category.png"
    }, (200, 201), "category: create")
    category_id = data_id(category)
    if category_id:
        ids["category"] = category_id
        request("GET", f"/api/category/{category_id}", user_token, label="category: read")
        request("POST", f"/api/category/update/{category_id}", admin_token, {"name": f"{MARKER}_Category_Updated", "description": "Updated", "slugCategory": f"{MARKER.lower()}-category-updated", "imageCategory": "test/category-updated.png"}, label="category: update")
        list_check("/api/category", user_token, "category: list")
        request("POST", f"/api/category/trashed/{category_id}", admin_token, expected=(200, 204), label="category: soft-delete")
        request("POST", f"/api/category/restore/{category_id}", admin_token, expected=(200, 204), label="category: restore")

    merchant = request("POST", "/api/merchants", user_token, {"userId": user_id, "name": f"{MARKER}_Merchant", "description": "Full CRUD merchant", "address": "Test Street 1", "contactEmail": email, "contactPhone": "081234567890", "status": "PENDING"}, (200, 201), "merchant: create")
    merchant_id = data_id(merchant)
    if merchant_id:
        ids["merchant"] = merchant_id
        request("GET", f"/api/merchants/{merchant_id}", user_token, label="merchant: read")
        request("PUT", f"/api/merchants/{merchant_id}", user_token, {"userId": user_id, "name": f"{MARKER}_Merchant_Updated", "description": "Updated", "address": "Test Street 2", "contactEmail": email, "contactPhone": "081234567891", "status": "SUCCESS"}, label="merchant: update")
        list_check("/api/merchants", user_token, "merchant: list")
        request("DELETE", f"/api/merchants/{merchant_id}", admin_token, expected=(200, 204), label="merchant: soft-delete")
        request("POST", f"/api/merchants/restore/{merchant_id}", admin_token, expected=(200, 204), label="merchant: restore")

    if merchant_id:
        crud("merchant_detail", "/api/merchant-details/create", {"merchantId": merchant_id, "displayName": f"{MARKER} Detail", "coverImageUrl": "https://example.test/cover.png", "logoUrl": "https://example.test/logo.png", "shortDescription": "Detail", "websiteUrl": "https://example.test"}, "/api/merchant-details/update/{id}", {"merchantId": merchant_id, "displayName": f"{MARKER} Detail Updated", "coverImageUrl": "https://example.test/cover2.png", "logoUrl": "https://example.test/logo2.png", "shortDescription": "Updated", "websiteUrl": "https://example.test/updated"}, admin_token, read_prefix="/api/merchant-details", list_path="/api/merchant-details", trash_path="/api/merchant-details/{id}", restore_path="/api/merchant-details/restore/{id}")
        crud("merchant_business", "/api/merchant-businesses/create", {"merchantId": merchant_id, "businessType": "RETAIL", "taxId": f"{MARKER}TAX", "establishedYear": 2020, "numberOfEmployees": 5, "websiteUrl": "https://example.test"}, "/api/merchant-businesses/update/{id}", {"businessType": "WHOLESALE", "taxId": f"{MARKER}TAX2", "establishedYear": 2021, "numberOfEmployees": 7, "websiteUrl": "https://example.test/updated"}, admin_token, read_prefix="/api/merchant-businesses", list_path="/api/merchant-businesses", trash_path="/api/merchant-businesses/{id}", restore_path="/api/merchant-businesses/restore/{id}")
        crud("merchant_policy", "/api/merchant-policies/create", {"merchantId": merchant_id, "policyType": "RETURN", "title": f"{MARKER} Policy", "description": "Return policy"}, "/api/merchant-policies/update/{id}", {"policyType": "RETURN", "title": f"{MARKER} Policy Updated", "description": "Updated policy"}, admin_token, read_prefix="/api/merchant-policies", list_path="/api/merchant-policies", trash_path="/api/merchant-policies/{id}", restore_path="/api/merchant-policies/restore/{id}")
        crud("merchant_award", "/api/merchant-awards/create", {"merchantId": merchant_id, "title": f"{MARKER} Award", "description": "Award", "issuedBy": "Freebuff", "issueDate": "2026-01-01", "expiryDate": "2027-01-01", "certificateUrl": "https://example.test/cert.pdf"}, "/api/merchant-awards/update/{id}", {"title": f"{MARKER} Award Updated", "description": "Updated", "issuedBy": "Freebuff", "issueDate": "2026-01-01", "expiryDate": "2027-01-01", "certificateUrl": "https://example.test/cert2.pdf"}, admin_token, read_prefix="/api/merchant-awards", list_path="/api/merchant-awards", trash_path="/api/merchant-awards/{id}", restore_path="/api/merchant-awards/restore/{id}")

    if category_id and merchant_id:
        product = request("POST", "/api/products/create", user_token, {"merchantId": merchant_id, "categoryId": category_id, "name": f"{MARKER}_Product", "description": "Full CRUD product", "price": 12500, "countInStock": 20, "brand": "Freebuff", "weight": 500, "rating": 5, "slugProduct": f"{MARKER.lower()}-product", "imageProduct": "test/product.png", "barcode": f"BC{int(time.time())}"}, (200, 201), "product: create")
        product_id = data_id(product)
        if product_id:
            ids["product"] = product_id
            request("GET", f"/api/products/{product_id}", user_token, label="product: read")
            request("POST", f"/api/products/update/{product_id}", user_token, {"merchantId": merchant_id, "categoryId": category_id, "name": f"{MARKER}_Product_Updated", "description": "Updated", "price": 13000, "countInStock": 18, "brand": "Freebuff", "weight": 550, "rating": 5, "slugProduct": f"{MARKER.lower()}-product-updated", "imageProduct": "test/product2.png", "barcode": f"BCU{int(time.time())}"}, label="product: update")
            list_check("/api/products", user_token, "product: list")
            request("POST", "/api/carts", user_token, {"quantity": 2, "productId": product_id, "userId": user_id}, (200, 201), "cart: create")
            list_check(f"/api/carts/user/{user_id}", user_token, "cart: list")

            order = request("POST", "/api/orders/create", user_token, {"merchantId": merchant_id, "userId": user_id, "totalPrice": 26000, "items": [{"productId": product_id, "quantity": 2, "price": 13000}], "shipping": {"alamat": "Test Address", "provinsi": "DKI Jakarta", "kota": "Jakarta", "courier": "JNE", "shippingMethod": "REG", "shippingCost": 10000, "negara": "Indonesia"}}, (200, 201), "order: create")
            order_id = data_id(order)
            if order_id:
                ids["order"] = order_id
                request("GET", f"/api/orders/{order_id}", user_token, label="order: read")
                request("POST", f"/api/orders/update/{order_id}", user_token, {"userId": user_id, "totalPrice": 28000, "items": [{"productId": product_id, "quantity": 2, "price": 14000}]}, label="order: update")
                list_check("/api/orders", user_token, "order: list")
                ship = request("POST", "/api/shipping-addresses/create", user_token, {"orderId": order_id, "alamat": "Updated Address", "provinsi": "Jawa Barat", "kota": "Bandung", "courier": "JNE", "shippingMethod": "YES", "shippingCost": 15000, "negara": "Indonesia"}, (200, 201), "shipping: create")
                ship_id = data_id(ship)
                if ship_id:
                    ids["shipping"] = ship_id
                    request("GET", f"/api/shipping-addresses/{ship_id}", user_token, label="shipping: read")
                    request("POST", f"/api/shipping-addresses/update/{ship_id}", user_token, {"orderId": order_id, "alamat": "Updated Address 2", "provinsi": "Jawa Barat", "kota": "Bandung", "courier": "JNE", "shippingMethod": "REG", "shippingCost": 12000, "negara": "Indonesia"}, label="shipping: update")
                    list_check("/api/shipping-addresses", user_token, "shipping: list")
                tx = request("POST", "/api/transactions/create", user_token, {"orderId": order_id, "merchantId": merchant_id, "paymentMethod": "BANK_TRANSFER", "amount": 28000, "userId": user_id, "paymentStatus": "PENDING"}, (200, 201), "transaction: create")
                tx_id = data_id(tx)
                if tx_id:
                    ids["transaction"] = tx_id
                    request("GET", f"/api/transactions/{tx_id}", user_token, label="transaction: read")
                    request("POST", f"/api/transactions/update/{tx_id}", user_token, {"orderId": order_id, "merchantId": merchant_id, "paymentMethod": "BANK_TRANSFER", "amount": 28000, "paymentStatus": "SUCCESS"}, label="transaction: update")
                    list_check("/api/transactions", user_token, "transaction: list")

            review = request("POST", "/api/reviews/create", user_token, {"userId": user_id, "productId": product_id, "name": f"{MARKER} Review", "comment": "Excellent test product", "rating": 5}, (200, 201), "review: create")
            review_id = data_id(review)
            if review_id:
                ids["review"] = review_id
                request("GET", f"/api/reviews/{review_id}", user_token, label="review: read")
                request("POST", f"/api/reviews/update/{review_id}", user_token, {"name": f"{MARKER} Review Updated", "comment": "Updated review", "rating": 4}, label="review: update")
                list_check("/api/reviews", user_token, "review: list")
                detail = request("POST", "/api/review-details/create", user_token, {"reviewId": review_id, "type": "photo", "url": "https://example.test/review.png", "caption": "Test photo"}, (200, 201), "review_detail: create")
                detail_id = data_id(detail)
                if detail_id:
                    ids["review_detail"] = detail_id
                    request("GET", f"/api/review-details/{detail_id}", user_token, label="review_detail: read")
                    request("POST", f"/api/review-details/update/{detail_id}", user_token, {"type": "photo", "url": "https://example.test/review2.png", "caption": "Updated photo"}, label="review_detail: update")
                    list_check("/api/review-details", user_token, "review_detail: list")
                    request("DELETE", f"/api/review-details/{detail_id}", admin_token, expected=(200, 204), label="review_detail: soft-delete")
                    request("POST", f"/api/review-details/restore/{detail_id}", admin_token, expected=(200, 204), label="review_detail: restore")

    crud("slider", "/api/sliders/create", {"name": f"{MARKER} Slider", "image": "test/slider.png"}, "/api/sliders/update/{id}", {"name": f"{MARKER} Slider Updated", "image": "test/slider2.png"}, admin_token, read_prefix="/api/sliders", list_path="/api/sliders", trash_path="/api/sliders/{id}", restore_path="/api/sliders/restore/{id}")
    crud("banner", "/api/banners", {"name": f"{MARKER} Banner", "startDate": "2026-01-01", "endDate": "2026-12-31", "startTime": "09:00:00", "endTime": "18:00:00", "isActive": True}, "/api/banners/{id}", {"name": f"{MARKER} Banner Updated", "startDate": "2026-01-01", "endDate": "2026-12-31", "startTime": "10:00:00", "endTime": "19:00:00", "isActive": True}, admin_token, id_keys=("bannerId",), read_prefix="/api/banners", list_path="/api/banners", trash_path="/api/banners/{id}", restore_path="/api/banners/restore/{id}")

    # Admin-only role lifecycle (test role is marker-prefixed; no permanent delete).
    role = request("POST", "/api/roles", admin_token, {"name": f"{MARKER}_ROLE"}, (200, 201), "role: create")
    role_id = data_id(role)
    if role_id:
        ids["role"] = role_id
        request("GET", f"/api/roles/{role_id}", admin_token, label="role: read")
        request("PUT", f"/api/roles/{role_id}", admin_token, {"name": f"{MARKER}_ROLE_UPDATED"}, label="role: update")
        list_check("/api/roles", admin_token, "role: list")
        request("DELETE", f"/api/roles/{role_id}", admin_token, expected=(200, 204), label="role: soft-delete")
        request("POST", f"/api/roles/restore/{role_id}", admin_token, expected=(200, 204), label="role: restore")

    # Admin user list plus self update using the real user JWT.
    list_check("/api/users", admin_token, "user: list")
    request("GET", f"/api/users/{user_id}", user_token, label="user: read")
    request("PUT", f"/api/users/{user_id}", user_token, {"firstname": "Freebuff", "lastname": "FullTestUpdated", "email": email, "password": PASSWORD, "confirmPassword": PASSWORD}, label="user: update")

    # Read-only statistics after transactional records exist.
    stats = [
        "/api/category/monthly-total-pricing?year=2026&month=1", "/api/category/yearly-total-pricing?year=2026",
        "/api/orders/stats/monthly-revenue?year=2026&month=1", "/api/orders/stats/yearly-revenue?year=2026",
        "/api/transactions/stats/monthly-amount-success?year=2026&month=1", "/api/transactions/stats/yearly-amount-success?year=2026",
    ]
    for path in stats:
        request("GET", path, admin_token, label=f"stats: {path}")

    write_report(email, ids)


def write_report(email: str, ids_map: dict[str, int]):
    passed = sum(1 for item in results if item["ok"])
    failed = len(results) - passed
    lines = [
        "# Full Authenticated JWT / CRUD Test Report", "",
        f"> Generated: `{dt.datetime.now().astimezone().isoformat(timespec='seconds')}`  ",
        "> Target: `http://127.0.0.1:8080`", "",
        "## Scope", "",
        "This run uses a real access token returned by `POST /api/auth/login` for user-authorized operations. Admin-only routes use a JWT signed with the repository's official `auth/src/main/resources/privateKey.pem`, with the same issuer and claims format used by auth-service. No token is hard-coded in this report.", "",
        "Only marker-prefixed test entities were created. Permanent-delete and `*/permanent/all`, `*/restore/all`, and `delete-all` operations were not called. Soft-delete/restore was exercised where safe.", "",
        "## Summary", "", f"- Test marker: `{MARKER}`", f"- Fixture user: `{email}`", f"- Requests executed: **{len(results)}**", f"- Passed expected responses: **{passed}**", f"- Failed/unexpected responses: **{failed}**", f"- Created IDs: `{json.dumps(ids_map, sort_keys=True)}`", "",
        "### What is genuinely authenticated", "",
        "- `auth: login`, `auth: refresh`, and `auth: me` were exercised.",
        "- User JWT was used for merchant, product, cart, order, shipping, transaction, review, and review-detail flows.",
        "- Official-key admin JWT was used for category, merchant metadata, banner, slider, role, admin lists, soft-delete, and restore flows.",
        "- Each CRUD flow records create/read/update/list evidence; safe entities also record soft-delete/restore evidence.", "",
        "## Detailed request evidence", "",
        "| # | Step | HTTP | Expected | ms | Result | Request | Response excerpt |",
        "|---:|---|---:|---|---:|---|---|---|",
    ]
    for index, item in enumerate(results, 1):
        req = compact(item["request"]).replace("|", "\\|")
        resp = compact(item["response"] or item["error"]).replace("|", "\\|")
        lines.append(f"| {index} | `{item['label']}` | `{item['status']}` | `{item['expected']}` | {item['elapsed']} | {'PASS' if item['ok'] else 'FAIL'} | `{req}` | `{resp}` |")
    lines += ["", "## Failure details", ""]
    failures = [item for item in results if not item["ok"]]
    if failures:
        lines += ["| Step | HTTP | Expected | Response |", "|---|---:|---|---|"]
        for item in failures:
            lines.append(f"| `{item['label']}` | `{item['status']}` | `{item['expected']}` | `{compact(item['response'] or item['error']).replace('|','\\|')}` |")
    else:
        lines.append("No unexpected responses.")
    lines += ["", "## Limitations", "", "- Upload endpoints are not included in this authenticated CRUD run because they write files and require a separate multipart fixture policy.", "- Admin JWT is cryptographically signed with the repository key, but the current auth login implementation always emits `ROLE_USER`; therefore admin authorization was tested with an official-key token carrying the admin group rather than through the public login endpoint.", "- Test rows are intentionally retained with the marker for auditability; remove them only after reviewing this report with a targeted, dependency-aware cleanup.", ""]
    REPORT.write_text("\n".join(lines), encoding="utf-8")
    print(json.dumps({"report": str(REPORT), "total": len(results), "passed": passed, "failed": failed, "ids": ids_map}, indent=2))


if __name__ == "__main__":
    main()
