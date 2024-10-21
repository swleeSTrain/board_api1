package org.sunbong.board_api1.member.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.board_api1.cart.domain.Cart;
import org.sunbong.board_api1.cart.domain.CartDetails;
import org.sunbong.board_api1.cart.repository.CartDetailsRepository;
import org.sunbong.board_api1.cart.repository.CartRepository;
import org.sunbong.board_api1.member.domain.MemberEntity;
import org.sunbong.board_api1.member.domain.MemberRole;
import org.sunbong.board_api1.product.domain.Product;
import org.sunbong.board_api1.product.repository.ProductRepository;

@Log4j2
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartDetailsRepository cartDetailsRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;


    @Test
    public void testCartList(){
        String email = "user1@aaa.com";

        Pageable pageable = PageRequest.of(0, 50);

        log.info(cartDetailsRepository.listOfMember(email, pageable).getContent());
    }

    @Test
    @Transactional
    @Commit
    public void testCartDummies(){
        for (int i = 0; i < 10; i++) {
            MemberEntity member = MemberEntity.builder()
                    .email("user"+i+"@aaa.com")
                    .pw(passwordEncoder.encode("1111"))
                    .build();

            Cart cart = Cart.builder()
                    .member(member)
                    .build();
            cartRepository.save(cart);

        }

    }

    @Test
    @Transactional
    @Commit
    public void testCartDetailDummies(){
        for (int i = 1; i < 11; i++) {

            CartDetails cartDetails = CartDetails.builder()
                    .cart(Cart.builder()
                            .cno((long) i)
                            .build())
                    .product(Product.builder()
                            .pno((long) 2)
                            .build())
                    .qty(50)
                    .build();
            cartDetailsRepository.save(cartDetails);
        }

    }


    @Test
    @Transactional
    @Commit
    public void testDummies() {

        for (int i = 1; i <= 10; i++) {

            MemberEntity member = MemberEntity.builder()
                    .email("user" + i + "@aaa.com")
                    .pw(passwordEncoder.encode("1111"))
                    .role(i < 8 ? MemberRole.USER : MemberRole.ADMIN)
                    .build();

            memberRepository.save(member);

        }
    }
}

