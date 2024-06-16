package com.environment.environment.controller;

import com.environment.environment.dto.BoardDto;
import com.environment.environment.dto.BoardMapper;
import com.environment.environment.entity.Board;
import com.environment.environment.repository.UserDetailsImpl;
import com.environment.environment.service.BoardService;
import com.environment.environment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<BoardDto.PostDetailsDTO>> getAllBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards = boardService.readAllBoard(pageable);
        Page<BoardDto.PostDetailsDTO> boardDtos = boards.map(BoardMapper::toDto);
        return ResponseEntity.ok().body(boardDtos);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto.PostDetailsDTO> getBoardById(@PathVariable Long boardId) {
        return boardService.getBoardById(boardId)
                .map(BoardMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Void> createBoard(@PathVariable Long userId, @RequestBody BoardDto.CreateBoardDto boardDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long loggedInUserId = userService.getLoggedInUserId(userDetails);

        // 요청한 사용자와 현재 로그인한 사용자의 ID가 일치하지 않는 경우 권한이 없는 것으로 처리
        if (!userId.equals(loggedInUserId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boardService.createBoard(boardDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{userId}/{boardId}")
    public ResponseEntity<Void> updateBoard(@PathVariable Long userId, @PathVariable Long boardId,
                                            @RequestBody BoardDto.UpdateBoardDto boardDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long loggedInUserId = userService.getLoggedInUserId(userDetails);

        // 요청한 사용자와 현재 로그인한 사용자의 ID가 일치하지 않는 경우 권한이 없는 것으로 처리
        if (!userId.equals(loggedInUserId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boardService.updateBoard(userId, boardId, boardDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long userId, @PathVariable Long boardId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long loggedInUserId = userService.getLoggedInUserId(userDetails);

        // 요청한 사용자와 현재 로그인한 사용자의 ID가 일치하지 않는 경우 권한이 없는 것으로 처리
        if (!userId.equals(loggedInUserId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().build();
    }
}