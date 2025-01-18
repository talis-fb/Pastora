package br.ufrn.imd.pastora.domain;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@With
@Builder
public class ExecutionData {
    protected final Date startedTime;
    protected final Date finishedTime;

    protected final String monitorId;
    protected final String data;

    @Valid
    @Builder.Default
    protected final List<String> errors = new ArrayList<>();

    @Valid
    protected final ExecutionData.Status status;
    public enum Status {
        RUNNING,
        FINISHED,
    }
}
