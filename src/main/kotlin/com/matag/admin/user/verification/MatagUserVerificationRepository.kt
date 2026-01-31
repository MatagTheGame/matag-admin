package com.matag.admin.user.verification

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MatagUserVerificationRepository : CrudRepository<MatagUserVerification, Long>
