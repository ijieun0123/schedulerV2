package com.example.schedulerv2.service;

import com.example.schedulerv2.dto.response.CommentResponseDto;
import com.example.schedulerv2.entity.Comment;
import com.example.schedulerv2.entity.Schedule;
import com.example.schedulerv2.entity.User;
import com.example.schedulerv2.repository.CommentRepository;
import com.example.schedulerv2.repository.ScheduleRepository;
import com.example.schedulerv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public CommentResponseDto save(String contents, Long scheduleId, Long userId) {
        Schedule findSchedule = scheduleRepository.findScheduleByIdOrElseThrow(scheduleId);
        User findUser = userRepository.findUserByIdOrElseThrow(userId);

        Comment comment = new Comment(contents, findSchedule, findUser);

        Comment savedComment = commentRepository.save(comment);

        return CommentResponseDto.toCommentDto(savedComment);
    }

    public CommentResponseDto findByCommentId(Long commentId) {
        Comment findComment = commentRepository.findCommentByIdOrElseThrow(commentId);

        return CommentResponseDto.toCommentDto(findComment);
    }

    public List<CommentResponseDto> findByScheduleId(Long scheduleId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "modifiedAt"));
        Page<Comment> commentPage = commentRepository.findCommentsByScheduleId(scheduleId, pageable);

        return commentPage.getContent().stream().map(CommentResponseDto::toCommentDto).collect(Collectors.toList());
    }


    public List<CommentResponseDto> findByUserId(Long userId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "modifiedAt"));
        Page<Comment> commentPage = commentRepository.findCommentsByUserId(userId, pageable);

        return commentPage.getContent().stream().map(CommentResponseDto::toCommentDto).collect(Collectors.toList());
    }

    // queryDSL test
    public List<CommentResponseDto> getComments(Long scheduleId, Long userId){
        log.info("[service] scheduleId: {}", scheduleId);
        log.info("[service] userId: {}", userId);

        List<Comment> findComments = commentRepository.findByScheduleIdAndUserId(scheduleId, userId);

        return findComments.stream().map(CommentResponseDto::toCommentDto).collect(Collectors.toList());
    }

    public List<CommentResponseDto> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "modifiedAt"));
        Page<Comment> commentPage = commentRepository.findAll(pageable);

        return commentPage.getContent().stream().map(CommentResponseDto::toCommentDto).collect(Collectors.toList());
    }

    public CommentResponseDto update(Long id, String contents, String currentUserEmail) {
        Comment findComment = commentRepository.findCommentByIdOrElseThrow(id);

        if(!findComment.getUser().getEmail().equals(currentUserEmail)){
            throw new SecurityException("이 댓글을 수정할 권한이 없습니다.");
        }

        findComment.setContents(contents);

        return CommentResponseDto.toCommentDto(findComment);
    }

    public void deleteById(Long id, String currentUserEmail) {
        Comment findComment = commentRepository.findCommentByIdOrElseThrow(id);

        if(!findComment.getUser().getEmail().equals(currentUserEmail)){
            throw new SecurityException("이 댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(findComment);
    }
}
