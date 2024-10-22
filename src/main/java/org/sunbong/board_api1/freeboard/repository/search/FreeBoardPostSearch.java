package org.sunbong.board_api1.freeboard.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.freeboard.domain.FreeBoardPost;
import org.sunbong.board_api1.freeboard.dto.FreeBoardPostListDTO;

public interface FreeBoardPostSearch {
    Page<FreeBoardPost>boardlist(Pageable pageable);
    PageResponseDTO<FreeBoardPostListDTO> listByBno(long bno, PageRequestDTO requestDTO);
}
