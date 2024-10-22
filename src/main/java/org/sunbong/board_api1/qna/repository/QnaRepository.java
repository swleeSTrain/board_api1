package org.sunbong.board_api1.qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.board_api1.qna.domain.Answer;
import org.sunbong.board_api1.qna.repository.search.QnaSearch;

public interface QnaRepository extends JpaRepository<Answer, Long>, QnaSearch {


}
