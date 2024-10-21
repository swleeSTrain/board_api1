package org.sunbong.board_api1.qna.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "question")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;

    private String content;

    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

}
