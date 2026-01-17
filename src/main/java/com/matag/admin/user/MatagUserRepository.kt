package com.matag.admin.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatagUserRepository extends CrudRepository<MatagUser, Long> {
  Optional<MatagUser> findByEmailAddress(String username);

  Optional<MatagUser> findByUsername(String username);

  @Query("SELECT COUNT(id) FROM MatagUser WHERE status = ?1")
  long countUsersByStatus(MatagUserStatus userStatus);

  @Query("SELECT u.username FROM MatagUser u JOIN MatagSession ms on u.id = ms.matagUser.id WHERE ms.validUntil > ?1")
  List<String> retrieveOnlineUsers(LocalDateTime now);
}
