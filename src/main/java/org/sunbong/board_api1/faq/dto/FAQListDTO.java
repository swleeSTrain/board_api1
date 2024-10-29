package org.sunbong.board_api1.faq.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQListDTO {

    private Long fno;
    private String question;
    private String answer;
    private String fileName;




}
