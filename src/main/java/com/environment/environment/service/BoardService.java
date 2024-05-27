package com.environment.environment.service;

import com.environment.environment.dto.BoardDto;
import com.environment.environment.entity.Board;
import com.environment.environment.entity.User;
import com.environment.environment.repository.BoardRepository;
import com.environment.environment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<Board> readAllBoard() {
        return boardRepository.findAll();
    }

    @Transactional
    public Optional<Board> getBoardById(Long boardId) {
        return boardRepository.findById(boardId);
    }

    @Transactional
    public Board createBoard(BoardDto.CreateBoardDto boardDto, Long userId) {
        // 사용자 정보를 조회합니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // 보드를 생성합니다.
        Board board = new Board();
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setWriter(user.getNickname());
        board.setPostedTime(LocalDateTime.now());

        // 보드를 사용자에게 추가합니다.
        user.addBoard(board);

        // 보드를 저장합니다.
        return boardRepository.save(board);
    }

    @Transactional
    public void updateBoard(Long userId, Long boardId, BoardDto.UpdateBoardDto boardDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found with id: " + boardId));

        if (!board.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Board does not belong to user with id: " + userId);
        }

        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setWriter(user.getNickname());
        board.setUpdatedTime(LocalDateTime.now());

        boardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }
}