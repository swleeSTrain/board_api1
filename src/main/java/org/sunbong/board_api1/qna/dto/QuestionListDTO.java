package org.sunbong.board_api1.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionListDTO {

    private Long qno;
    private String title;
    private String writer;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    // 답변 개수
    private Long answerCount;


}
