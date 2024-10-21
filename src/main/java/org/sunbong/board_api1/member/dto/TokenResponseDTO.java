package org.sunbong.board_api1.member.dto;

import lombok.Data;

@Data
public class TokenResponseDTO {
    private String email;
    private String accessToken;
    private String refreshToken;
}
