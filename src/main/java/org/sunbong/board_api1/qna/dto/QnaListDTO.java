package org.sunbong.board_api1.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaListDTO {

    private Long qno;
    private String title;
    private String writer;
    private Long answerCount;

}
