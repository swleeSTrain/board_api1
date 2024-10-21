package org.sunbong.board_api1.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.board_api1.cart.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
