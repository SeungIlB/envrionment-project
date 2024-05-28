package com.environment.environment.repository;


import com.environment.environment.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Override
    Optional<Board> findById(Long aLong);

    void deleteById(Long boardId);

}
