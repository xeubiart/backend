package xeubiart.com.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xeubiart.com.domain.user.model.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
