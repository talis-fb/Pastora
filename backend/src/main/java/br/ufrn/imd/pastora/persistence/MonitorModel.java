package br.ufrn.imd.pastora.persistence;

import br.ufrn.imd.pastora.domain.MonitorData;
import br.ufrn.imd.pastora.domain.monitor.MonitorValidation;
import br.ufrn.imd.pastora.domain.monitor.MonitorHttpDefinition;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "monitors")
@Builder
@Value
@With
public class MonitorModel {
    @Id
    String id;

    // Data fields
    String name;
    String description;
    Boolean enabled;
    String userId;
    String serviceId;
    List<String> onSuccess;
    List<String> onFail;

    // Trigger
    Integer intervalRate;

    // Definitions
    MonitorHttpDefinition definition;
    List<MonitorValidation> validations;
}
