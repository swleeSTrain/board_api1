package org.sunbong.board_api1.qna.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerAddDTO {

    private String content;
    private String writer;
    private Long qno;
}
