package org.sunbong.board_api1.qna.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.qna.domain.QAnswer;
import org.sunbong.board_api1.qna.domain.QAttachFile;
import org.sunbong.board_api1.qna.domain.QQuestion;
import org.sunbong.board_api1.qna.domain.Question;
import org.sunbong.board_api1.qna.dto.QnaReadDTO;

import java.util.*;

@Log4j2
public class AnswerSearchImpl extends QuerydslRepositorySupport implements AnswerSearch {

    public AnswerSearchImpl() {
        super(Question.class);
    }

    @Override
    public PageResponseDTO<QnaReadDTO> readByQno(Long qno, PageRequestDTO pageRequestDTO) {
        log.info("-------------------list by qno-----------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("qno").descending()
        );

        QQuestion question = QQuestion.question;
        QAnswer answer = QAnswer.answer;
        QAttachFile attachFile = QAttachFile.attachFile;

        JPQLQuery<Question> query = from(question);
        query.leftJoin(answer).on(answer.question.eq(question));
        query.leftJoin(question.attachFiles, attachFile);

        query.where(question.qno.eq(qno));

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery = query.select(
                question.qno,
                question.title,
                question.content,
                question.writer,
                question.createdDate,
                question.modifiedDate,
                attachFile.fileName,
                answer.ano,
                answer.content,
                answer.writer,
                answer.createdDate
        );

        List<Tuple> results = tupleQuery.fetch();
        Map<Long, QnaReadDTO> dtoMap = new HashMap<>();

        for (Tuple tuple : results) {
            Long questionQno = tuple.get(question.qno);

            // 질문 DTO를 가져오거나 생성
            QnaReadDTO dto = dtoMap.computeIfAbsent(questionQno, key -> QnaReadDTO.builder()
                    .qno(questionQno)
                    .questionTitle(tuple.get(question.title))
                    .questionContent(tuple.get(question.content))
                    .questionWriter(tuple.get(question.writer))
                    .questionCreatedDate(tuple.get(question.createdDate))
                    .questionModifiedDate(tuple.get(question.modifiedDate))
                    .attachFiles(new HashSet<>())  // 중복 방지를 위한 빈 Set 생성
                    .answers(new ArrayList<>())
                    .build());

            // 첨부파일 처리
            String fileName = tuple.get(attachFile.fileName);
            if (fileName != null) {
                dto.getAttachFiles().add(fileName); // 파일 추가
            }

            // 답변 DTO 생성
            Long answerAno = tuple.get(answer.ano);
            if (answerAno != null) {
                QnaReadDTO.AnswerDTO answerDto = new QnaReadDTO.AnswerDTO(
                        answerAno,
                        tuple.get(answer.content),
                        tuple.get(answer.writer),
                        tuple.get(answer.createdDate)
                );

                // 중복된 답변 추가 방지
                if (dto.getAnswers().stream().noneMatch(a -> a.getAno().equals(answerAno))) {
                    dto.getAnswers().add(answerDto); // 답변 추가
                }
            }
        }

        // dtoMap.values()를 사용하여 dtoList에 추가
        List<QnaReadDTO> dtoList = new ArrayList<>(dtoMap.values());
        long total = tupleQuery.fetchCount();

        return PageResponseDTO.<QnaReadDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }


}
