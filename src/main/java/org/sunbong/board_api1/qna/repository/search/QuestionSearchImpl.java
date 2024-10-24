package org.sunbong.board_api1.qna.repository.search;

import com.querydsl.core.BooleanBuilder;
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
import java.util.Set;

@Log4j2
public class QuestionSearchImpl extends QuerydslRepositorySupport implements QuestionSearch {

    public QuestionSearchImpl() {
        super(Question.class);
    }

    @Override
    public PageResponseDTO<QuestionListDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("-------------------list with search-----------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("qno").descending()
        );

        QQuestion question = QQuestion.question;
        QAnswer answer = QAnswer.answer;

        JPQLQuery<Question> query = from(question);
        query.leftJoin(answer).on(answer.question.eq(question));

        // 검색 조건 추가
        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType(); // 검색 타입 (title, content, writer)

        // or 써서

        // BooleanBuilder 사용하여 동적 조건 추가
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && type != null) {
            if (type.contains("title")) {
                builder.or(question.title.containsIgnoreCase(keyword));
            }
            if (type.contains("content")) {
                builder.or(question.content.containsIgnoreCase(keyword));
            }
            if (type.contains("writer")) {
                builder.or(question.writer.containsIgnoreCase(keyword));
            }
        }

        query.where(builder);

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
//                question.tags
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

//            Set<String> tags = tuple.get(question.tags);

            QuestionListDTO dto = QuestionListDTO.builder()
                    .qno(qno)
                    .title(title)
                    .writer(writer)
                    .createdDate(createdDate)
                    .modifiedDate(modifiedDate)
                    .answerCount(answerCount)
//                    .tags(tags)
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
