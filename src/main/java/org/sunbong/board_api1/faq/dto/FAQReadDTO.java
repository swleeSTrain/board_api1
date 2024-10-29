package org.sunbong.board_api1.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sunbong.board_api1.faq.domain.ClassificationType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQReadDTO {
    private String question;
    private String answer;
    private ClassificationType classificationType;

}
