package br.ufrn.imd.pastora.persistence;

import br.ufrn.imd.pastora.domain.Service;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "services")
@Builder
@Value
@With
public class ServiceModel {
    @Id
    String id;
    String userId;
    String name;
    String description;
    String iconUrl;

    public static ServiceModel fromEntity(Service service) {
        return ServiceModel.builder()
            .description(service.getDescription())
            .name(service.getName())
            .userId(service.getUserId())
            .description(service.getDescription())
            .iconUrl(service.getIconUrl())
            .build();
    }

    public Service toEntity() {
        return Service.builder()
            .description(description)
            .name(name)
            .iconUrl(iconUrl)
            .userId(userId)
            .build();
    }
}
