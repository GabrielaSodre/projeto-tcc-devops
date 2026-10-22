package com.projetotcc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("runtimeFailure")
public class RuntimeFailureHealthIndicator implements HealthIndicator {

    private final boolean failureEnabled;

    public RuntimeFailureHealthIndicator(
            @Value("${app.health.simular-falha:false}")
            boolean failureEnabled) {
        this.failureEnabled = failureEnabled;
    }

    @Override
    public Health health() {
        if (failureEnabled) {
            return Health.down()
                    .withDetail("cenario", "falha-runtime-simulada")
                    .withDetail(
                            "mensagem",
                            "Falha ativada para testar o rollback automatico"
                    )
                    .build();
        }

        return Health.up()
                .withDetail("cenario", "execucao-normal")
                .build();
    }
}
