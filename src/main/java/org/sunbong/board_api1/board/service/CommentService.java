package org.sunbong.board_api1.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sunbong.board_api1.board.domain.BoardPost;
import org.sunbong.board_api1.board.domain.Comment;
import org.sunbong.board_api1.board.dto.CommentDTO;
import org.sunbong.board_api1.board.dto.CommentListDTO;
import org.sunbong.board_api1.board.repository.BoardPostRepository;
import org.sunbong.board_api1.board.repository.CommentRepository;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.exception.CommonExceptions;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public PageResponseDTO<CommentListDTO> readByBno(Long bno, PageRequestDTO pageRequestDTO) {
        if (pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }
        PageResponseDTO<CommentListDTO> result =commentRepository.readByBno(bno, pageRequestDTO);
        return result;
    }

    public CommentDTO newComment(CommentDTO commentDTO) {

        Comment comment = Comment.builder()
                .boardPost(commentDTO.getBoardPost())
                .writer(commentDTO.getWriter())
                .content(commentDTO.getContent())
                .build();
        commentRepository.save(comment);
        return commentDTO;
    }
    public CommentDTO updateComment(Long cno, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(cno)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다: " + cno));
        Comment originComment = comment.builder()
                .cno(comment.getCno())
                .writer(commentDTO.getWriter())
                .boardPost(commentDTO.getBoardPost())
                .content(commentDTO.getContent())
                .build();
        Comment updateComment = originComment.toBuilder()
                .content(commentDTO.getContent())
                .build();
        commentRepository.save(updateComment);
        return commentDTO;
    }
    public void deleteComment(Long cno) {
        commentRepository.deleteById(cno);
    }
}
