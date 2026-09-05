// Gateway smoke/load test (Phase 7).
//
// Run: k6 run load-test/gateway-smoke.js
// Requires a running gateway at GATEWAY_URL (default http://localhost:8080).
//
// Thresholds: p95 < 500ms, error rate < 1%. Adjust for your measured baseline.

import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const BASE_URL = __ENV.GATEWAY_URL || 'http://localhost:8080';

const errorRate = new Rate('gateway_errors');
const loginTrend = new Trend('login_duration_ms', true);
const cartTrend = new Trend('cart_duration_ms', true);
const healthTrend = new Trend('health_duration_ms', true);

export const options = {
  stages: [
    { duration: '30s', target: 20 },  // warm-up
    { duration: '30s', target: 100 }, // ramp to peak
    { duration: '30s', target: 100 }, // hold peak
    { duration: '15s', target: 0 },   // cool down
  ],
  thresholds: {
    gateway_errors: ['rate<0.01'],
    login_duration_ms: ['p(95)<500'],
    cart_duration_ms: ['p(95)<500'],
    health_duration_ms: ['p(95)<200'],
  },
};

export default function () {
  // Health endpoint (liveness/readiness surface).
  const health = http.get(`${BASE_URL}/q/health`);
  healthTrend.add(health.timings.duration);
  errorRate.add(health.status >= 400);
  check(health, { 'health 2xx': (r) => r.status >= 200 && r.status < 300 });

  // Representative business path: login (POST through gRPC chain).
  const login = http.post(
    `${BASE_URL}/api/auth/login`,
    JSON.stringify({ email: `loadtest${__VU}@example.com`, password: 'LoadtestPass1!' }),
    { headers: { 'Content-Type': 'application/json' } },
  );
  loginTrend.add(login.timings.duration);
  // 4xx is an expected outcome for a nonexistent user; only 5xx is an error.
  errorRate.add(login.status >= 500);
  check(login, { 'login not 5xx': (r) => r.status < 500 });

  // Representative read path: cart by user.
  const cart = http.get(`${BASE_URL}/api/carts/user/${__VU}`);
  cartTrend.add(cart.timings.duration);
  errorRate.add(cart.status >= 500);
  check(cart, { 'cart not 5xx': (r) => r.status < 500 });

  sleep(1);
}
