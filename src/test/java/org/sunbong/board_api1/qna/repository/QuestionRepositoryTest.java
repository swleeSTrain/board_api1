package org.sunbong.board_api1.qna.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.qna.domain.Answer;
import org.sunbong.board_api1.qna.domain.Question;
import org.sunbong.board_api1.qna.dto.QnaReadDTO;
import org.sunbong.board_api1.qna.dto.QuestionListDTO;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Log4j2
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    public void testQuestionSearch() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .keyword("제목")
                .type("title&content&writer")
                .build();

        PageResponseDTO<QuestionListDTO> result = questionRepository.questionList(pageRequestDTO);

        assertNotNull(result);
        result.getDtoList().forEach(dto -> log.info("Search Result DTO: " + dto));
    }

    @Test
    public void testQuestionList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResponseDTO<QuestionListDTO> result = questionRepository.questionList(pageRequestDTO);

        result.getDtoList().forEach(dto -> {
            log.info("DTO: " + dto);
        });
    }

    @Test
    @Transactional
    @Rollback(false) // 롤백 방지
    public void testQuestionRegister() {
        for (int i = 1; i <= 100; i++) {

            // Question 엔티티 생성
            Question question = Question.builder()
                    .title("제목 " + i)
                    .content("내용 " + i)
                    .writer("작성자 " + i)
                    .build();

            question.addTag("질문");
            question.addTag("불만");

            question.addFile("file" + i + "_1.jpg");
            question.addFile("file" + i + "_2.png");

            // 저장
            questionRepository.save(question);
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testQuestionEdit() {
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testQuestionDelete() {
        Long qno = 1L;
        questionRepository.deleteById(qno);

        boolean exists = questionRepository.findById(qno).isPresent();

        assertTrue(!exists, "Question should be deleted");
    }



}
