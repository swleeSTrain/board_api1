package org.sunbong.board_api1.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sunbong.board_api1.board.domain.BoardPost;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long cno;
    private String content;
    private String writer;
    private BoardPost boardPost;
}
