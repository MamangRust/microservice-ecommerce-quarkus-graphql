# Load Testing (Phase 7)

Tooling: [k6](https://k6.io) for the HTTP gateway, [ghz](https://ghz.sh) for
gRPC services. Both must be installed on the machine that runs the tests
(`brew install k6 ghz` / download binaries).

## Prerequisites

1. Run the stack locally: `cd deployments/local && docker compose up -d`
   (builds images with the names from `build-docker-images.sh`), or run
   services in Quarkus dev mode pointing at the same Postgres/Redis/Kafka.
2. Confirm the gateway is up: `curl localhost:8080/q/health`.

## 1. Gateway HTTP load test (k6)

```bash
k6 run load-test/gateway-smoke.js
```

`gateway-smoke.js` exercises `/q/health` plus representative REST paths
(`POST /api/auth/login`, `GET /api/carts/user/1`) with ramp-up to 100 VUs.
Built-in thresholds: p95 < 500ms and error rate < 1%. Exit code non-zero
means the run missed the target.

## 2. gRPC load test (ghz)

```bash
# OrderQueryService findById against the order service gRPC port
ghz \
  --proto common/src/main/proto/order/order_query.proto \
  --call pb.order.OrderQueryService.FindByIdOrderRequest \
  --data '{"id":1}' \
  --insecure \
  --concurrency 50 \
  --duration 30s \
  --rps 200 \
  localhost:8098
```

Adjust host/port per service (see `quarkus.grpc.clients.*` in gateway
properties or each service's Dockerfile `EXPOSE`). Add `--histogram` and
`--format=json` for p50/p95/p99 output.

## 3. Measuring P95/P99 from Prometheus

The histogram `request_duration_seconds` now uses explicit buckets
(5ms..10s) so Prometheus quantiles are meaningful:

```promql
histogram_quantile(0.95, sum(rate(request_duration_seconds_bucket[5m])) by (le, method))
histogram_quantile(0.99, sum(rate(request_duration_seconds_bucket[5m])) by (le, method))
```

Kafka producer throughput / retry / consumer lag / outbox backlog:
`kafka_publish_total`, `kafka_retry_total`, `kafka_consumer_lag`,
`outbox_events_published_total`, `outbox_backlog`.

## 4. Pre-flight checks (avoid silent no-ops)

1. **Histogram buckets**: `setExplicitBucketBoundariesAdvice` is *advice* —
   confirm the exporter honors it by checking the exposed bucket boundaries:
   `curl localhost:8080/q/metrics | grep request_duration_seconds_bucket`
   and confirm the `le` labels include 0.005..10. If only SDK defaults appear,
   enable histogram advice for the OTel SDK (see Quarkus docs) before trusting
   the PromQL quantiles.
2. **Redis pool keys**: `quarkus.redis.max-pool-size`/`max-pool-waiting`/
   `timeout` are flat keys matching Quarkus 3.31's `RedisClientConfig`. Unknown
   `quarkus.*` keys only produce a build warning, so confirm the keys took
   effect (e.g. via the Redis client logs or by checking the config docs for
   your exact Quarkus version).

## 5. Procedure before locking production targets

1. Baseline run (this harness) with the Phase 7 tuning defaults in place.
2. Record p50/p95/p99 + error rate + max latency for each critical path
   (login, cart read, order create, email delivery).
3. Only then adjust `quarkus.vertx.worker-pool-size`, `quarkus.thread-pool.max-threads`,
   `quarkus.redis.max-pool-size`, and Kafka `linger.ms`/`batch.size`; re-run and
   compare. Never tune on guesses without a measured baseline.
