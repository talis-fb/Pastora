package br.ufrn.imd.pastora.persistence;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

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
