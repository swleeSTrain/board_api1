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
public class AnswerRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    public void testReadByQno() {

        Long qno = 1L;  // 테스트에 맞는 qno로 설정
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResponseDTO<QnaReadDTO> result = answerRepository.readByQno(qno, pageRequestDTO);

    }

    @Test
    @Transactional
    @Rollback(false)  // 롤백 방지 db에 넣기 위해
    public void testRegisterAnswer() {

        // 등록할 Question 엔티티 찾기
        Question question = questionRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        Answer answer = Answer.builder()
                .content("답변.")
                .writer("hi")
                .question(question)  // 해당 질문에 대한 답변으로 설정
                .build();

        Answer savedAnswer = answerRepository.save(answer);

        System.out.println("Generated Answer ID: " + savedAnswer.getAno());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testAnswerEdit() {

    }

    @Test
    @Transactional
    @Rollback(false)
    public void testAnswerDelete() {

        Long ano = 2L;
        answerRepository.deleteById(ano);

        boolean exists = answerRepository.findById(ano).isPresent();

        assertTrue(!exists, "Answer should be deleted");
    }





}
