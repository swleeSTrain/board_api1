package org.sunbong.board_api1.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerListDTO {

    private Long ano;
    private String content;
    private String writer;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
