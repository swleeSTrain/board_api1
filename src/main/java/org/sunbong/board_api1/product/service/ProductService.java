package org.sunbong.board_api1.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.exception.CommonExceptions;
import org.sunbong.board_api1.product.dto.ProductListDTO;
import org.sunbong.board_api1.product.repository.ProductRepository;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {

        if(pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }

        return productRepository.listByCno(1L, pageRequestDTO);
    }

}