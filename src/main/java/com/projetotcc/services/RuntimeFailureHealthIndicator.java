
package com.projetotcc.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Indicador de saúde para simular uma falha em tempo de execução.
 *
 * Quando app.health.simular-falha=true, a aplicação informa
 * que está indisponível, permitindo testar o rollback automático.
 *
 * Quando app.health.simular-falha=false, informa que o cenário
 * de falha simulada está desativado.
 */
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
                    .withDetail(
                            "cenario",
                            "falha-runtime-simulada"
                    )
                    .withDetail(
                            "mensagem",
                            "Falha simulada para validar o rollback automatico."
                    )
                    .build();
        }

        return Health.up()
                .withDetail(
                        "cenario",
                        "execucao-normal"
                )
                .withDetail(
                        "mensagem",
                        "Falha simulada desativada."
                )
                .build();
    }
}
