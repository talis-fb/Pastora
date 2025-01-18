package br.ufrn.imd.pastora.domain;

import br.ufrn.imd.pastora.domain.monitor.MonitorHttpDefinition;
import br.ufrn.imd.pastora.domain.monitor.MonitorValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@Builder
public class MonitorData {
    @NotNull
    protected final String name;
    protected final String description;

    @NotNull
    @Builder.Default
    protected final Boolean enabled = true;

    protected final String userId;

    protected final String serviceId;

    @Positive
    @NotNull
    protected final Integer intervalRate;

    @Valid
    @NotNull
    MonitorHttpDefinition http;

    @Builder.Default
    @Valid
    @NotNull
    List<MonitorValidation> validations = new ArrayList<>();

    @Builder.Default
    @Valid
    @NotNull
    protected final List<String> onSuccess = new ArrayList<>();

    @Builder.Default
    @Valid
    @NotNull
    protected final List<String> onFail = new ArrayList<>();
}
