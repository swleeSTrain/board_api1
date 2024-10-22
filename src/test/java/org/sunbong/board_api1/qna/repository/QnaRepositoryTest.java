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


@Log4j2
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QnaRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QnaRepository qnaRepository;

    @Test
    public void testRead() {

        Long qno = 1L; // 조회할 질문의 ID

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResponseDTO<QnaReadDTO> result = qnaRepository.readByQno(qno, pageRequestDTO);

        result.getDtoList().forEach(dto -> {
            log.info("DTO: " + dto);
        });
    }


    @Test
    public void testList1() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResponseDTO<QuestionListDTO> result = questionRepository.list(pageRequestDTO);

        result.getDtoList().forEach(dto -> {
            log.info("DTO: " + dto);
        });
    }

    @Test
    @Transactional
    @Rollback(false)  // 이 애너테이션을 추가하여 롤백 방지
    public void testRegisterQuestion() {
        // given: Question 엔티티 생성
        Question question = Question.builder()
                .title("여보세요 나야")
                .content("거기 잘지내니")
                .writer("임창정")
                .build();

        // 첨부 파일 추가
        question.addFile("소주한잔.jpg");

        // when: QnaRepository를 이용해 저장
        Question savedQuestion = questionRepository.save(question);


    }

    @Test
    @Transactional
    @Rollback(false)  // 롤백 방지
    public void testRegisterAnswer() {

        // given: 등록할 Question 엔티티 찾기
        Question question = questionRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        Answer answer = Answer.builder()
                .content("답변.")
                .writer("hi")
                .question(question)  // 해당 질문에 대한 답변으로 설정
                .build();

        Answer savedAnswer = qnaRepository.save(answer);

        System.out.println("Generated Answer ID: " + savedAnswer.getAno());
    }



}
