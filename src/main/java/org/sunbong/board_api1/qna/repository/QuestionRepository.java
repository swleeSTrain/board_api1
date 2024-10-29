package org.sunbong.board_api1.qna.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.qna.domain.Question;
import org.sunbong.board_api1.qna.repository.search.QuestionSearch;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionSearch {

}
