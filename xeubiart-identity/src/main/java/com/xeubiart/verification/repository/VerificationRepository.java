package com.xeubiart.verification.repository;

import com.xeubiart.verification.entity.VerificationSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends CrudRepository<VerificationSession, String> {
}
