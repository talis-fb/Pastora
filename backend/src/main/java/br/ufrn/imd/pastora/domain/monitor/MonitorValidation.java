package br.ufrn.imd.pastora.domain.monitor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@With
@Builder
public class MonitorValidation {
    @NotNull
    protected final String field; // e.g., "status", "url", "method", "body", "headers"

    @Valid
    @NotNull
    protected final Operation operation;
    @NotNull
    protected final Object value; // e.g., expected value or pattern

    public enum Operation {
        EQUALS,
        NOT_EQUALS,
        CONTAINS,
    }
}
