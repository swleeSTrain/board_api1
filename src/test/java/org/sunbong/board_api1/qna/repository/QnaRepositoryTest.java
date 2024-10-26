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

import static org.junit.jupiter.api.Assertions.assertNotNull;


@Log4j2
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QnaRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository qnaRepository;

    @Test
    public void testReadByQno() {
        // Arrange
        Long qno = 1L;  // 테스트에 맞는 qno로 설정
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        // Act
        PageResponseDTO<QnaReadDTO> result = questionRepository.readByQno(qno, pageRequestDTO);

    }


    @Test
    public void testList1() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResponseDTO<QuestionListDTO> result = questionRepository.questionList(pageRequestDTO);

        result.getDtoList().forEach(dto -> {
            log.info("DTO: " + dto);
        });
    }

    @Test
    @Transactional
    @Rollback(false) // 롤백 방지
    public void testRegisterMultipleQuestions() {
        for (int i = 1; i <= 100; i++) {
            // Question 엔티티 생성
            Question question = Question.builder()
                    .title("제목 " + i)
                    .content("내용 " + i)
                    .writer("작성자 " + i)
                    .build();

            // 태그 추가 (예시로 3개의 태그 추가)
            question.addTag("질문" + (i % 5)); // 5개의 태그 그룹 순환
            question.addTag("카테고리" + (i % 3)); // 3개의 카테고리 그룹 순환
            question.addTag("주제" + (i % 4)); // 4개의 주제 그룹 순환

            // 첨부 파일 추가 (예시로 각 질문에 파일 2개씩 추가)
            question.addFile("file" + i + "_1.jpg");
            question.addFile("file" + i + "_2.png");

            // 저장
            questionRepository.save(question);
        }
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
