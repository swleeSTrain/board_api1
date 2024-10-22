package org.sunbong.board_api1.qna.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.exception.CommonExceptions;
import org.sunbong.board_api1.qna.domain.Answer;
import org.sunbong.board_api1.qna.domain.Question;
import org.sunbong.board_api1.qna.dto.AnswerRegisterDTO;
import org.sunbong.board_api1.qna.dto.QnaReadDTO;
import org.sunbong.board_api1.qna.dto.QuestionListDTO;
import org.sunbong.board_api1.qna.dto.QuestionAddDTO;
import org.sunbong.board_api1.qna.repository.QnaRepository;
import org.sunbong.board_api1.qna.repository.QuestionRepository;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class QnaService {

    private final QuestionRepository questionRepository;
    private final QnaRepository qnaRepository;

    public PageResponseDTO<QnaReadDTO> readByQno(Long qno, PageRequestDTO pageRequestDTO) {

        // 페이지 번호가 0보다 작으면 예외 발생
        if (pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }

        // QuestionRepository 또는 QnaRepository의 readByQno 메서드를 호출하여 페이징 결과 얻음
        PageResponseDTO<QnaReadDTO> result = qnaRepository.readByQno(qno, pageRequestDTO);

        // 결과를 그대로 반환
        return result;
    }

    public PageResponseDTO<QuestionListDTO> list(PageRequestDTO pageRequestDTO) {

        // 페이지 번호가 0보다 작으면 예외 발생
        if (pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }

        // QuestionRepository의 list 메서드를 호출하여 페이징 결과 얻음
        PageResponseDTO<QuestionListDTO> result = questionRepository.list(pageRequestDTO);

        // 결과를 그대로 반환
        return result;
    }

    public Long registerQuestion(QuestionAddDTO dto) {

        Question question = Question.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .tags(dto.getTags())
                .build();

        if (dto.getAttachFiles() != null) {
            dto.getAttachFiles().forEach(fileName -> question.addFile(fileName));
        }

        Question savedQuestion = questionRepository.save(question);

        return savedQuestion.getQno();
    }

    @Transactional
    public Long registerAnswer(AnswerRegisterDTO dto) {

        // 질문(Question) 찾기
        Question question = questionRepository.findById(dto.getQno())
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));


        Answer answer = Answer.builder()
                .content(dto.getContent())
                .writer(dto.getWriter())
                .question(question)  // 해당 답변이 속한 질문 설정
                .build();

        Answer savedAnswer = qnaRepository.save(answer);

        return savedAnswer.getAno();  // 등록된 답변의 ID 반환
    }

}