package org.sunbong.board_api1.member.domain;

public enum MemberRole {

    USER("USER"),ADMIN("ADMIN");

    String role;

    MemberRole(String role) {
        this.role = role;
    }
}
