package org.sunbong.board_api1.qna.repository.search;

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

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class AnswerSearchImpl extends QuerydslRepositorySupport implements AnswerSearch {

    public AnswerSearchImpl() {
        super(Answer.class);
    }

    @Override
    public PageResponseDTO<QnaReadDTO> readByQno(Long qno, PageRequestDTO pageRequestDTO) {

        log.info("-------------------read by qno-----------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("createdDate").descending()
        );

        QQuestion question = QQuestion.question;
        QAttachFileQna attachFile = QAttachFileQna.attachFileQna;
        QAnswer answer = QAnswer.answer;

        // 질문 쿼리
        JPQLQuery<Question> questionQuery = from(question)
                .leftJoin(question.attachFiles, attachFile).fetchJoin()
                .where(question.qno.eq(qno));

        Question fetchedQuestion = questionQuery.fetchOne();

        // 답변 목록 쿼리
        JPQLQuery<Answer> answerQuery = from(answer)
                .where(answer.question.qno.eq(qno))
                .orderBy(answer.createdDate.desc());

        this.getQuerydsl().applyPagination(pageable, answerQuery);

        List<AnswerListDTO> answerList = answerQuery.fetch().stream()
                .map(ans -> AnswerListDTO.builder()
                        .ano(ans.getAno())
                        .content(ans.getContent())
                        .writer(ans.getWriter())
                        .createdDate(ans.getCreatedDate())
                        .modifiedDate(ans.getModifiedDate())
                        .build())
                .collect(Collectors.toList());

        // QnaReadDTO 생성
        QnaReadDTO qnaReadDTO = QnaReadDTO.builder()
                .qno(fetchedQuestion.getQno())
                .title(fetchedQuestion.getTitle())
                .content(fetchedQuestion.getContent())
                .writer(fetchedQuestion.getWriter())
                .createdDate(fetchedQuestion.getCreatedDate())
                .modifiedDate(fetchedQuestion.getModifiedDate())
                .tags(fetchedQuestion.getTags()) // 태그 추가
                .attachFiles(fetchedQuestion.getAttachFiles().stream()
                        .map(file -> file.getFileName())
                        .collect(Collectors.toSet()))
                .answers(answerList)
                .build();

        long totalAnswers = answerQuery.fetchCount();

        return PageResponseDTO.<QnaReadDTO>withAll()
                .dtoList(List.of(qnaReadDTO)) // 단일 질문을 PageResponseDTO에 담음
                .totalCount(totalAnswers)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
