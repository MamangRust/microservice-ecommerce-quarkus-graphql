#!/usr/bin/env bash
# ghz load test for OrderQueryService.FindById (order service gRPC, Phase 7).
#
# Usage: ./load-test/grpc-order-query.sh
# Env overrides: ORDER_GRPC_HOST, ORDER_GRPC_PORT, CONCURRENCY, DURATION, RPS
# Requires: ghz (https://ghz.sh)
set -euo pipefail

HOST="${ORDER_GRPC_HOST:-localhost}"
PORT="${ORDER_GRPC_PORT:-9018}"
CONCURRENCY="${CONCURRENCY:-50}"
DURATION="${DURATION:-30s}"
RPS="${RPS:-200}"

ghz \
  --proto common/src/main/proto/order/order_query.proto \
  -I common/src/main/proto \
  --call pb.order.OrderQueryService.FindById \
  --data '{"id":1}' \
  --insecure \
  --concurrency "$CONCURRENCY" \
  --rps "$RPS" \
  --duration "$DURATION" \
  --histogram \
  "$HOST:$PORT"
