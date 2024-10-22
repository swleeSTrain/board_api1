package org.sunbong.board_api1.qna.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sunbong.board_api1.qna.dto.QuestionListDTO;

public interface QuestionSearch {

    Page<QuestionListDTO> list(Pageable pageable);

}
