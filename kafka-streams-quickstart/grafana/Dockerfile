FROM grafana/grafana:5.4.3

COPY dashboard.yml /etc/grafana/provisioning/dashboards
COPY datasource.yml /etc/grafana/provisioning/datasources
COPY temperatures-dashboard.json /var/lib/grafana/dashboards/temperatures-dashboard.json
