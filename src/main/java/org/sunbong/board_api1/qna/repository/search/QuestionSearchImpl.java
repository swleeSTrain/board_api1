package org.sunbong.board_api1.qna.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.qna.domain.QAnswer;
import org.sunbong.board_api1.qna.domain.QQuestion;
import org.sunbong.board_api1.qna.domain.Question;
import org.sunbong.board_api1.qna.dto.QuestionListDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class QuestionSearchImpl extends QuerydslRepositorySupport implements QuestionSearch {

    public QuestionSearchImpl() {
        super(Question.class);
    }

    @Override
    public PageResponseDTO<QuestionListDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("-------------------list-----------");

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("qno").descending()
        );

        QQuestion question = QQuestion.question;
        QAnswer answer = QAnswer.answer;

        JPQLQuery<Question> query = from(question);
        query.leftJoin(answer).on(answer.question.eq(question));

        query.groupBy(question);

        // 페이징 처리 및 정렬 적용
        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery = query.select(
                question.qno,
                question.title,
                question.writer,
                question.createdDate,
                question.modifiedDate,
                answer.count()
        );

        List<Tuple> results = tupleQuery.fetch();

        List<QuestionListDTO> dtoList = new ArrayList<>();

        for (Tuple tuple : results) {
            Long qno = tuple.get(question.qno);
            String title = tuple.get(question.title);
            String writer = tuple.get(question.writer);
            LocalDateTime createdDate = tuple.get(question.createdDate);
            LocalDateTime modifiedDate = tuple.get(question.modifiedDate);
            Long answerCount = tuple.get(answer.count());

            // QuestionListDTO로 변환
            QuestionListDTO dto = QuestionListDTO.builder()
                    .qno(qno)
                    .title(title)
                    .writer(writer)
                    .createdDate(createdDate)
                    .modifiedDate(modifiedDate)
                    .answerCount(answerCount)
                    .build();

            dtoList.add(dto);
        }

        long total = tupleQuery.fetchCount();

        // PageResponseDTO로 변환해서 반환
        return PageResponseDTO.<QuestionListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }


}