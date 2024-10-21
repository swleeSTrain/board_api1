package org.sunbong.board_api1.cart.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sunbong.board_api1.member.domain.MemberEntity;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    @OneToOne //원투원
    private MemberEntity member;
//테스트
}
