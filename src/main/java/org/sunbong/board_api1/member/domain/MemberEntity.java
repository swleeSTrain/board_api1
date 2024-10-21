package org.sunbong.board_api1.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class MemberEntity {

    @Id
    private String email;

    private String pw;

    private MemberRole role;

}
