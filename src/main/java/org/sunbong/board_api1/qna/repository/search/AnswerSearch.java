package org.sunbong.board_api1.qna.repository.search;

import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.qna.dto.AnswerListDTO;
import org.sunbong.board_api1.qna.dto.QnaReadDTO;

public interface AnswerSearch {

    PageResponseDTO<QnaReadDTO> readByQno(Long qno, PageRequestDTO pageRequestDTO);
}
