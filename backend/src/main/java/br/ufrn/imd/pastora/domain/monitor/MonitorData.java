package br.ufrn.imd.pastora.domain.monitor;

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

    // Trigger
    protected final Integer intervalRate;

    protected final List<String> onSuccess = new ArrayList<>();
    protected final List<String> onFail = new ArrayList<>();

    @NotNull
    protected final SaveSuccessWhen saveSuccessWhen = SaveSuccessWhen.SUCCESS;

    public enum SaveSuccessWhen {
        SUCCESS,
        ALL_NEXT_SUCCESS,
        ANY_NEXT_SUCCESS,
    }
}
