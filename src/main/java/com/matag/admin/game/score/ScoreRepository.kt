package com.matag.admin.game.score;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.matag.admin.user.MatagUser;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {
  @Query("FROM Score s ORDER BY s.elo DESC, s.matagUser.username ASC")
  List<Score> findAll();

  Score findByMatagUser(MatagUser user);

  @Query("FROM Score s WHERE s.matagUser.username = ?1")
  Score findByUsername(String username);
}
