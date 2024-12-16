package br.ufrn.imd.pastora.domain;

import br.ufrn.imd.pastora.persistence.ExecutionModel;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.Date;
import java.util.List;

@Data
@With
@Builder
public class Execution {
    Date startedTime;
    Date finishedTime;

    String monitorId;
    List<String> triggered; // Monitor IDS that this one triggered
    Object data;
    List<Execution.Error> errors;

    public static class Error {
        String message;
        String validationId;
    }
}
