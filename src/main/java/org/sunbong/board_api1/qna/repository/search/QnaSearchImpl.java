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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
public class QnaSearchImpl extends QuerydslRepositorySupport implements QnaSearch {

    public QnaSearchImpl() {
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

        // Create query
        JPQLQuery<Question> query = from(question);
        query.leftJoin(answer).on(answer.question.eq(question));
        query.leftJoin(question.attachFiles, attachFile); // join attachFiles

        query.where(question.qno.eq(qno));

        // Apply pagination
        this.getQuerydsl().applyPagination(pageable, query);

        // Fetch the selected data
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
        List<QnaReadDTO> dtoList = new ArrayList<>();

        for (Tuple tuple : results) {
            Long questionQno = tuple.get(question.qno);
            String questionTitle = tuple.get(question.title);
            String questionContent = tuple.get(question.content);
            String questionWriter = tuple.get(question.writer);
            LocalDateTime questionCreatedDate = tuple.get(question.createdDate);
            LocalDateTime questionModifiedDate = tuple.get(question.modifiedDate);

            // 파일명 처리, null 체크 없을 수도 있으니까 사진이
            String fileName = tuple.get(attachFile.fileName);
            Set<String> attachFiles = (fileName != null) ? Set.of(fileName) : Collections.emptySet();  // 파일이 없으면 빈 값으로 설정

            Long answerAno = tuple.get(answer.ano);
            String answerContent = tuple.get(answer.content);
            String answerWriter = tuple.get(answer.writer);
            LocalDateTime answerCreatedDate = tuple.get(answer.createdDate);

            QnaReadDTO dto = QnaReadDTO.builder()
                    .qno(questionQno)
                    .questionTitle(questionTitle)
                    .questionContent(questionContent)
                    .questionWriter(questionWriter)
                    .questionCreatedDate(questionCreatedDate)
                    .questionModifiedDate(questionModifiedDate)
                    .attachFiles(attachFiles)  // 파일 목록 추가
                    .ano(answerAno)
                    .answerContent(answerContent)
                    .answerWriter(answerWriter)
                    .answerCreatedDate(answerCreatedDate)
                    .build();

            dtoList.add(dto);
        }

        long total = tupleQuery.fetchCount();

        return PageResponseDTO.<QnaReadDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
