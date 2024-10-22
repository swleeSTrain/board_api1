package org.sunbong.board_api1.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAddDTO {

    private String title;
    private String content;
    private String writer;
    private Set<String> attachFiles;
    private Set<String> tags;
}
