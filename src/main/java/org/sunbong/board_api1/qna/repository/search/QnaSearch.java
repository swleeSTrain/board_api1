package org.sunbong.board_api1.qna.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.qna.dto.QnaReadDTO;

public interface QnaSearch {

    PageResponseDTO<QnaReadDTO> readByQno(Long qno, PageRequestDTO pageRequestDTO);
}
