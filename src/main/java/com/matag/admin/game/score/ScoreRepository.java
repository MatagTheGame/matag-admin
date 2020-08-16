package com.matag.admin.game.score;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends CrudRepository<Score, Long> {
  @Query("FROM Score s ORDER BY s.elo DESC, s.matagUser.username ASC")
  List<Score> findAll();
}
