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
import org.sunbong.board_api1.qna.domain.*;
import org.sunbong.board_api1.qna.dto.AnswerListDTO;
import org.sunbong.board_api1.qna.dto.QnaReadDTO;
import org.sunbong.board_api1.qna.dto.QuestionListDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
public class QuestionSearchImpl extends QuerydslRepositorySupport implements QuestionSearch {

    public QuestionSearchImpl() {
        super(Question.class);
    }

    @Override
    public PageResponseDTO<QuestionListDTO> questionList(PageRequestDTO pageRequestDTO) {

        log.info("-------------------list with search-----------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("qno").descending()
        );

        QQuestion question = QQuestion.question;
        QAnswer answer = QAnswer.answer;

        BooleanBuilder builder = new BooleanBuilder();
        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType();

        // 키워드 및 타입에 대한 검색 조건 추가
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

        // 태그에 대한 검색 조건 추가 (OR 조건으로 태그 검색)
        if (pageRequestDTO.getTags() != null && !pageRequestDTO.getTags().isEmpty()) {

            BooleanBuilder tagBuilder = new BooleanBuilder();

            pageRequestDTO.getTags().forEach(tag -> {
                tagBuilder.or(question.tags.any().eq(tag));
            });
            builder.and(tagBuilder);
        }

        // JPQL 쿼리 구성
        JPQLQuery<Question> query = from(question)
                .leftJoin(answer).on(answer.question.eq(question))
                .where(builder);

        // 총 개수 계산 (distinct 추가하여 중복 제거)
        long total = query.distinct().fetchCount();
        getQuerydsl().applyPagination(pageable, query);
        List<Question> questions = query.fetch();

        // 답변 개수 계산
        JPQLQuery<Tuple> countQuery = from(answer)
                .select(answer.question.qno, answer.count())
                .groupBy(answer.question.qno);

        List<Tuple> answerCounts = countQuery.fetch();

        Map<Long, Long> answerCountMap = answerCounts.stream()
                .collect(Collectors.toMap(tuple -> tuple.get(0, Long.class), tuple -> tuple.get(1, Long.class)));

        // QuestionListDTO 생성
        List<QuestionListDTO> dtoList = questions.stream()
                .map(q -> QuestionListDTO.builder()
                        .qno(q.getQno())
                        .title(q.getTitle())
                        .content(q.getContent())
                        .writer(q.getWriter())
                        .createdDate(q.getCreatedDate())
                        .modifiedDate(q.getModifiedDate())
                        .answerCount(answerCountMap.getOrDefault(q.getQno(), 0L))
                        .tags(q.getTags())
                        .build())
                .collect(Collectors.toList());

        // 결과 반환
        return PageResponseDTO.<QuestionListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }



}
