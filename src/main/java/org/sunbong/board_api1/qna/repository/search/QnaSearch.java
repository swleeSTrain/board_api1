package org.sunbong.board_api1.qna.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.product.domain.Product;
import org.sunbong.board_api1.product.dto.ProductListDTO;
import org.sunbong.board_api1.qna.domain.Question;
import org.sunbong.board_api1.qna.dto.QnaListDTO;

public interface QnaSearch {

    Page<QnaListDTO> list(Pageable pageable);

//    PageResponseDTO<QnaListDTO> listByQno(Long qno, PageRequestDTO pageRequestDTO);

}
