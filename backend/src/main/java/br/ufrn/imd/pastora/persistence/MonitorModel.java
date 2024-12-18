package br.ufrn.imd.pastora.persistence;

import br.ufrn.imd.pastora.domain.MonitorAbstractDefinition;
import br.ufrn.imd.pastora.domain.MonitorData;
import br.ufrn.imd.pastora.domain.MonitorHttpDefinition;
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
    MonitorData.SaveSuccessWhen saveSuccessWhen;

    // Trigger
    Integer intervalRate;

    // Definitions
    MonitorAbstractDefinition definition;
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

    public MonitorData toMonitorData() {
        return MonitorData.builder()
            .name(this.getName())
            .description(this.getDescription())
            .serviceId(this.getServiceId())
            .build();
    }
}
