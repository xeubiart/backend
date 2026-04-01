package xeubiart.com.domain.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xeubiart.com.domain.identity.model.Identity;
import xeubiart.com.domain.user.model.User;

import java.util.Optional;

public interface IdentityRepository extends JpaRepository<Identity, Integer> {
    Optional<Identity> findByEmail(String email);

    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT i.user FROM Identity i WHERE i.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);
}
