package org.sunbong.board_api1.qna.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.board_api1.category.domain.QCategoryProduct;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.product.domain.Product;
import org.sunbong.board_api1.product.domain.QAttachFile;
import org.sunbong.board_api1.product.domain.QProduct;
import org.sunbong.board_api1.product.domain.QReview;
import org.sunbong.board_api1.product.dto.ProductListDTO;
import org.sunbong.board_api1.qna.domain.QAnswer;
import org.sunbong.board_api1.qna.domain.QQuestion;
import org.sunbong.board_api1.qna.domain.Question;
import org.sunbong.board_api1.qna.dto.QnaListDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class QnaSearchImpl extends QuerydslRepositorySupport implements QnaSearch {

    public QnaSearchImpl() {
        super(Question.class);
    }

    @Override
    public Page<QnaListDTO> list(Pageable pageable) {
        log.info("-------------------list-----------");

        QQuestion question = QQuestion.question;
        QAnswer answer = QAnswer.answer;

        JPQLQuery<Question> query = from(question);
        query.leftJoin(answer).on(answer.question.eq(question));

        query.groupBy(question);

        // 페이징 처리 및 정렬
        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery = query.select(
                question.qno,
                question.title,
                question.writer,
                answer.count()
        );

        List<Tuple> results = tupleQuery.fetch();

        List<QnaListDTO> dtoList = new ArrayList<>();

        for (Tuple tuple : results) {
            Long qno = tuple.get(question.qno);
            String title = tuple.get(question.title);
            String writer = tuple.get(question.writer);
            Long answerCount = tuple.get(answer.count());

            // QnaListDTO로 변환
            QnaListDTO dto = QnaListDTO.builder()
                    .qno(qno)
                    .title(title)
                    .writer(writer)
                    .answerCount(answerCount) // 만약 answer count가 필요하다면 추가
                    .build();

            dtoList.add(dto);
        }

        // 총 데이터 수를 구합니다.
        long total = tupleQuery.fetchCount();

        // Page 객체로 변환해서 반환
        return new PageImpl<>(dtoList, pageable, total);
    }

}