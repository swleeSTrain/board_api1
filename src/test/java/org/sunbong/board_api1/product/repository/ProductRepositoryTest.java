package org.sunbong.board_api1.product.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.sunbong.board_api1.common.dto.PageRequestDTO;

@Log4j2
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testList1() {
        Pageable pageable = PageRequest.of(0, 10);
        productRepository.list(pageable);
    }

    @Test
    public void testList2() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
        log.info(productRepository.listByCno(1L, pageRequestDTO));
    }

}
