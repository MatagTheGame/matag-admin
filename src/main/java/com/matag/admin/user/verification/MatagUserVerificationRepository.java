package com.matag.admin.user.verification;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatagUserVerificationRepository extends CrudRepository<MatagUserVerification, Long> {

}
