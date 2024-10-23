package org.sunbong.board_api1.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sunbong.board_api1.board.domain.BoardPost;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentListDTO {
    private Long cno;
    private String writer;
    private String content;
    private LocalDateTime createTime;  // 생성날짜
    private LocalDateTime updateTime;  // 수정날짜
    private Long boardPostBno;         // 게시물 번호만 포함
}
