package br.ufrn.imd.pastora.domain.monitor;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.With;

@Data
@With
@Builder
public class MonitorValidation {
    @NotNull
    String field; // e.g., "status", "url", "method", "body", "headers"
    @NotNull
    Operation operation;
    @NotNull
    Object value; // e.g., expected value or pattern

    public enum Operation {
        EQUALS,
        NOT_EQUALS,
        CONTAINS,
    }
}
