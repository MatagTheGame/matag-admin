package com.matag.admin.game.score;

import com.matag.admin.user.MatagUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {
  @Query("FROM Score s ORDER BY s.elo DESC, s.matagUser.username ASC")
  List<Score> findAll();

  @Query("FROM Score s WHERE s.matagUser = ?1")
  Optional<Score> findByUser(MatagUser user);
}
