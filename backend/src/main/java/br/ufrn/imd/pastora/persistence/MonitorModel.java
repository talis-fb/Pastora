package br.ufrn.imd.pastora.persistence;

import br.ufrn.imd.pastora.domain.monitor.MonitorData;
import br.ufrn.imd.pastora.domain.monitor.definition.AbstractMonitorDefinition;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;

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
    MonitorData.SaveSuccessWhen saveSuccessWhen;

    // Trigger
    Integer intervalRate;

    // Definitions
    AbstractMonitorDefinition definition;
    List<String> validations;

    public static MonitorModel fromMonitorData(MonitorData data) {
        return MonitorModel.builder()
                .name(data.getName())
                .description(data.getDescription())
                .enabled(data.getEnabled())
                .serviceId(data.getServiceId())
                .onSuccess(data.getOnSuccess())
                .onFail(data.getOnFail())
                .saveSuccessWhen(data.getSaveSuccessWhen())
                .build();
    }
}
