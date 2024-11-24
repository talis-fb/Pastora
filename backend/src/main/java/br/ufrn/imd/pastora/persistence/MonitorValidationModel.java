package br.ufrn.imd.pastora.persistence;

import br.ufrn.imd.pastora.domain.monitor.MonitorValidation;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "validations")
@Builder
@Value
@With
public class MonitorValidationModel {
    @Id
    String id;
    String field; // e.g., "status", "url", "method", "body", "headers"
    MonitorValidation.Operation operation;
    Object value; // e.g., expected value or pattern

    public static MonitorValidationModel fromEntity(MonitorValidation entity) {
        return MonitorValidationModel.builder()
                .field(entity.getField())
                .operation(entity.getOperation())
                .value(entity.getValue())
                .build();
    }
}
