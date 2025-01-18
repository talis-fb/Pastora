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
    @Valid
    protected final Field field;
    public enum Field {
        status,
        body
    }


    @NotNull
    @Valid
    protected final Operation operation;
    public enum Operation {
        equals,
        not_equals,
        contains,
    }


    @NotNull
    protected final String value; // e.g., expected value or pattern
}
