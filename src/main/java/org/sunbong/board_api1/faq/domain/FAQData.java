package org.sunbong.board_api1.faq.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "faq_tbl")
public class FAQData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;

    @NotNull
    private String question;

    @NotNull
    private String answer;

    @NotNull
    private String writer;

}
