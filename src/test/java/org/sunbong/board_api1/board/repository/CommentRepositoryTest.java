package org.sunbong.board_api1.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sunbong.board_api1.board.domain.BoardPost;
import org.sunbong.board_api1.board.domain.Comment;
import org.sunbong.board_api1.board.dto.CommentDTO;
import org.sunbong.board_api1.board.dto.CommentListDTO;

import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;


@Log4j2
@SpringBootTest
public class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;

    @Test
    public void insertComment(){
        CommentDTO commentDTO = CommentDTO.builder()
                .boardPost(BoardPost.builder()
                        .bno(1L)
                        .build())
                .writer("작성자")
                .content("댓글내용")
                .build();
        Comment comment = Comment.builder()
                .boardPost(commentDTO.getBoardPost())
                .writer(commentDTO.getWriter())
                .content(commentDTO.getContent())
                .build();
        commentRepository.save(comment);
    }

    @Test
    public void listComment(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();
        PageResponseDTO<CommentListDTO> list = commentRepository.readByBno(1L, pageRequestDTO);
       log.info(list);
    }

    @Test
    public void deleteComment(){
    }


}