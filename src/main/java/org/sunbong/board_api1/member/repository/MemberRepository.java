package org.sunbong.board_api1.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.board_api1.member.domain.MemberEntity;

import java.util.Optional;

/**/
public interface MemberRepository extends JpaRepository<MemberEntity, String/*pk타입 문자열*/> {
    /* 내가직접다해야함 */

}
