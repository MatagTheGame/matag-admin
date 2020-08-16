package com.matag.admin.game.score;

import com.matag.admin.user.MatagUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {
  @Query("FROM Score s ORDER BY s.elo DESC, s.matagUser.username ASC")
  List<Score> findAll();

  Score findByMatagUser(MatagUser user);

  @Query("FROM Score s WHERE s.matagUser.username = ?1")
  Score findByUsername(String username);
}
