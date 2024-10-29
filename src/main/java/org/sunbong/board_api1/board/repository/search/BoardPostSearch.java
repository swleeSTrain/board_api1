package org.sunbong.board_api1.board.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sunbong.board_api1.board.dto.BoardPostReadDTO;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.board.domain.BoardPost;
import org.sunbong.board_api1.board.dto.BoardPostListDTO;

public interface BoardPostSearch {

    PageResponseDTO<BoardPostListDTO> listByBno(PageRequestDTO requestDTO);
    PageResponseDTO<BoardPostReadDTO> readByBno(Long bno, PageRequestDTO pageRequestDTO);
}
