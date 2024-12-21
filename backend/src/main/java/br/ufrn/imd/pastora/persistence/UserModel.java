package br.ufrn.imd.pastora.persistence;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;

@CompoundIndexes({
        @CompoundIndex(name = "email_1", def = "{ 'email': 1 }")
})
@Document(collection = "users")
@Builder
@Value
@With
public class UserModel {
    @Id
    String id;

    @Indexed(unique = true)
    String email;

    String name;
    String password;
}
