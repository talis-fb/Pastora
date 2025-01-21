package br.ufrn.imd.pastora.persistence;

import br.ufrn.imd.pastora.domain.ExecutionData;
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
    String id;

    Date startedTime;
    Date finishedTime;

    String monitorId;
    List<String> triggered; // Monitor IDS that this one triggered
    String data;
    List<String> errors;
    ExecutionData.Status status;
}
