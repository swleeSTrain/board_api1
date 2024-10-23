package org.sunbong.board_api1.board.repository.search;

import org.sunbong.board_api1.board.dto.CommentDTO;
import org.sunbong.board_api1.board.dto.CommentListDTO;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;

public interface CommentSearch {
    PageResponseDTO<CommentDTO> readByCno(Long cno, PageRequestDTO pageRequestDTO);

    PageResponseDTO<CommentListDTO> readByBno(Long bno, PageRequestDTO pageRequestDTO);
}
