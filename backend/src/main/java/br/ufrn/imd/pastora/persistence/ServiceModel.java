package br.ufrn.imd.pastora.persistence;

import br.ufrn.imd.pastora.domain.Service;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@CompoundIndexes({
        @CompoundIndex(name = "name_text_index", def = "{ 'name': 'text' }")
})
@Document(collection = "services")
@Data
@With
@Builder
public class ServiceModel {
    @Id
    String id;
    String userId;
    String name;
    String description;
    String iconUrl;
}
