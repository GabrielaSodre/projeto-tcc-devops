import http from 'k6/http';
import { sleep } from 'k6';

const HOST = __ENV.HOST;

export const options = {
  vus: 10,
  duration: '30s',
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<500'],
  },
};

export default function () {
  http.get(`http://${HOST}:8080/users`);
  sleep(1);
}
