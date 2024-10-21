package org.sunbong.board_api1.member.dto;

import lombok.Data;
import lombok.Setter;

@Data
public class MemberDTO {

    private String email;

    private String pw;

    private String role;

}
