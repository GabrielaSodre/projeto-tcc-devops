#!/usr/bin/env bash
# Sobe o Prometheus e o Grafana na EC2.
# Rode na EC2, com a pasta monitoramento/ do repo copiada para ~/monitoramento.
# Pode rodar de novo: os dados ficam nos volumes.
set -euo pipefail

PASTA="$HOME/monitoramento"

# Rede compartilhada com a aplicação (o CD também cria, se faltar)
docker network inspect monitoramento >/dev/null 2>&1 || docker network create monitoramento

# Prometheus
docker rm -f prometheus || true
docker run -d \
  --name prometheus \
  --network monitoramento \
  --restart unless-stopped \
  -p 127.0.0.1:9090:9090 \
  --memory 300m \
  -v "$PASTA/prometheus.yml:/etc/prometheus/prometheus.yml:ro" \
  -v prometheus-dados:/prometheus \
  prom/prometheus:v3.15.1 \
  --config.file=/etc/prometheus/prometheus.yml \
  --storage.tsdb.path=/prometheus \
  --storage.tsdb.retention.time=15d \
  --storage.tsdb.retention.size=1GB

# Grafana
docker rm -f grafana || true
docker run -d \
  --name grafana \
  --network monitoramento \
  --restart unless-stopped \
  -p 127.0.0.1:3000:3000 \
  --memory 512m \
  -v "$PASTA/grafana/datasource.yml:/etc/grafana/provisioning/datasources/datasource.yml:ro" \
  -v "$PASTA/grafana/dashboard.yml:/etc/grafana/provisioning/dashboards/dashboard.yml:ro" \
  -v "$PASTA/grafana/projeto-tcc.json:/etc/grafana/dashboards/projeto-tcc.json:ro" \
  -v grafana-dados:/var/lib/grafana \
  grafana/grafana:13.2.5
