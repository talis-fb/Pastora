package br.ufrn.imd.pastora.persistence;

import br.ufrn.imd.pastora.domain.Execution;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "results")
@Builder
@Value
@With
public class ExecutionModel {
    @Id
    Integer id;

    Date startedTime;
    Date finishedTime;

    String monitorId;
    List<Integer> triggered; // Monitor IDS that this one triggered
    Object data;
    List<Execution.Error> errors;
}
