package com.environment.environment.service;

import com.environment.environment.dto.CommentDto;
import com.environment.environment.entity.Board;
import com.environment.environment.entity.Comment;
import com.environment.environment.entity.User;
import com.environment.environment.repository.BoardRepository;
import com.environment.environment.repository.CommentRepository;
import com.environment.environment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Comment createComment(Long userId, Long boardId, CommentDto.CreateCommentDto createCommentDto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + boardId));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setBoard(board);
        comment.setWriter(user.getNickname());
        comment.setComment(createCommentDto.getComment());
        comment.setCreatedDate(LocalDateTime.now());


        return commentRepository.save(comment);
    }

    @Transactional
    public List<Comment> readComments(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + boardId));
        return commentRepository.findByBoardId(boardId);
    }

    @Transactional
    public Comment updateComment(Long userId, Long boardId, Long commentId, CommentDto.UpdateCommentDto updateCommentDto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + boardId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

        if (!comment.getUser().getId().equals(userId) || !comment.getBoard().getId().equals(boardId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User or Board mismatch");
        }

        comment.setComment(updateCommentDto.getComment());
        comment.setModifiedDate(LocalDateTime.now());
        comment.setWriter(user.getNickname());

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId){
        if (!commentRepository.existsById(commentId)) {
            throw new EntityNotFoundException("Comment not found with id: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }
}