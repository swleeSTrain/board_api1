package org.sunbong.board_api1.product.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.product.domain.Product;
import org.sunbong.board_api1.product.dto.ProductListDTO;

public interface ProductSearch {

    Page<Product> list(Pageable pageable);

    PageResponseDTO<ProductListDTO> listByCno(Long cno, PageRequestDTO pageRequestDTO);

}
