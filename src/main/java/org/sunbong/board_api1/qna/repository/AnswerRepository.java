package org.sunbong.board_api1.qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.board_api1.qna.domain.Answer;
import org.sunbong.board_api1.qna.repository.search.AnswerSearch;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerSearch {


}
