package br.ufrn.imd.pastora.persistence.repository;

import br.ufrn.imd.pastora.persistence.UserModel;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserModel, String> {
    Optional<UserModel> findByEmail(String email);
    boolean existsByEmail(String email);
}
