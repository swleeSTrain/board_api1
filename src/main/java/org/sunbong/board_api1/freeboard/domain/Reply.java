package org.sunbong.board_api1.freeboard.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sunbong.board_api1.member.domain.MemberEntity;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "freeBoardPost")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repleno;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private FreeBoardPost freeBoardPost;



}
