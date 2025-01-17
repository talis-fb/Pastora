package br.ufrn.imd.pastora.domain;

import br.ufrn.imd.pastora.domain.monitor.MonitorHttpDefinition;
import br.ufrn.imd.pastora.domain.monitor.MonitorValidation;
import jakarta.validation.constraints.NotNull;
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
    protected final Boolean enabled = true;

    @NotNull
    protected final String userId;

    protected final String serviceId;

    protected final Integer intervalRate;

    @NotNull
    MonitorHttpDefinition http;

    List<MonitorValidation> validations;

    protected final List<String> onSuccess = new ArrayList<>();
    protected final List<String> onFail = new ArrayList<>();
}
