package org.sunbong.board_api1.cart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.sunbong.board_api1.cart.domain.CartDetails;
import org.sunbong.board_api1.cart.dto.CartDetailsListDTO;

public interface CartDetailsRepository extends JpaRepository<CartDetails, Long> {

    @Query("""
        SELECT
            new org.sunbong.board_api1.cart.dto.CartDetailsListDTO(
            p.pno, p.name, p.price, count(r), attach.fileName,
            cd.qty
            )
        FROM
            MemberEntity m
            left join Cart c ON c.member = m
            left join CartDetails cd ON cd.cart = c
            join Product p ON p = cd.product
            left join p.attachFiles attach
            left join Review r ON r.product = p
        WHERE m.email = :email
        AND attach.ord = 0
        GROUP BY p
    """)
    Page<CartDetailsListDTO[]> listOfMember(@Param("email") String email, Pageable pageable);


}
