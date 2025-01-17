package br.ufrn.imd.pastora.domain;

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

    @Builder.Default
    protected final List<String> errors = new ArrayList<>();

    protected final ExecutionData.Status status;

    public static class Error {
        String message;
        String validationId;
    }

    public enum Status {
        RUNNING,
        FINISHED,
    }
}
