package org.sunbong.board_api1.faq.dto;


import lombok.Data;

@Data
public class FAQDto {

    private String question;
    private String answer;
    private String writer;
    private Long fno;

    public FAQDto(String question, String answer, String writer, Long fno) {
        this.question = question;
        this.answer = answer;
        this.writer = writer;
        this.fno = fno;
    }
}
