package org.sunbong.board_api1.qna.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.board_api1.qna.domain.QAnswer;
import org.sunbong.board_api1.qna.domain.QQuestion;
import org.sunbong.board_api1.qna.domain.Question;
import org.sunbong.board_api1.qna.dto.QnaReadDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class QnaSearchImpl extends QuerydslRepositorySupport implements QnaSearch {

    public QnaSearchImpl() {
        super(Question.class);
    }

    @Override
    public Page<QnaReadDTO> readByQno(Long qno, Pageable pageable) {
        log.info("-------------------list by qno-----------");

        QQuestion question = QQuestion.question;
        QAnswer answer = QAnswer.answer;

        // 질문을 기준으로 쿼리 시작
        JPQLQuery<Question> query = from(question);
        query.leftJoin(answer).on(answer.question.eq(question));

        // qno로 질문 필터링
        query.where(question.qno.eq(qno));

        // 페이징 처리 및 정렬 적용
        this.getQuerydsl().applyPagination(pageable, query);

        // 필요한 필드 선택
        JPQLQuery<Tuple> tupleQuery = query.select(
                question.qno,
                question.title,
                question.content,
                question.writer,
                question.createdDate,
                question.modifiedDate,
                answer.ano,
                answer.content,
                answer.writer,
                answer.createdDate
        );

        // 결과 리스트 가져오기
        List<Tuple> results = tupleQuery.fetch();

        List<QnaReadDTO> dtoList = new ArrayList<>();

        for (Tuple tuple : results) {
            Long questionQno = tuple.get(question.qno);
            String questionTitle = tuple.get(question.title);
            String questionContent = tuple.get(question.content);
            String questionWriter = tuple.get(question.writer);
            LocalDateTime questionCreatedDate = tuple.get(question.createdDate);
            LocalDateTime questionModifiedDate = tuple.get(question.modifiedDate);
            Long answerAno = tuple.get(answer.ano);
            String answerContent = tuple.get(answer.content);
            String answerWriter = tuple.get(answer.writer);
            LocalDateTime answerCreatedDate = tuple.get(answer.createdDate);

            // QnaReadDTO로 변환
            QnaReadDTO dto = QnaReadDTO.builder()
                    .qno(questionQno)
                    .questionTitle(questionTitle)
                    .questionContent(questionContent)
                    .questionWriter(questionWriter)
                    .questionCreatedDate(questionCreatedDate)
                    .questionModifiedDate(questionModifiedDate)
                    .ano(answerAno)
                    .answerContent(answerContent)
                    .answerWriter(answerWriter)
                    .answerCreatedDate(answerCreatedDate)
                    .build();

            dtoList.add(dto);
        }

        long total = tupleQuery.fetchCount();

        // Page 객체로 변환해서 반환
        return new PageImpl<>(dtoList, pageable, total);
    }

}
