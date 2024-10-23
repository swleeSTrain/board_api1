package org.sunbong.board_api1.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaReadDTO {

    // 질문 관련
    private Long qno;
    private String questionTitle;
    private String questionContent;
    private String questionWriter;
    private LocalDateTime questionCreatedDate;
    private LocalDateTime questionModifiedDate;
    private Set<String> tags;

    // 첨부된 파일 이름 목록 (질문에 첨부된 파일들)
    private Set<String> attachFiles;

    // 답변 관련
    private List<AnswerDTO> answers; // AnswerDTO를 추가

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnswerDTO {
        private Long ano;
        private String answerContent;
        private String answerWriter;
        private LocalDateTime answerCreatedDate;
    }
}

