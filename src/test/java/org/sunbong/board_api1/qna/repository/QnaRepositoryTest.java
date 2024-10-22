package org.sunbong.board_api1.qna.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.board_api1.qna.domain.Answer;
import org.sunbong.board_api1.qna.domain.Question;
import org.sunbong.board_api1.qna.dto.QnaReadDTO;
import org.sunbong.board_api1.qna.dto.QuestionListDTO;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

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

        // When: 질문과 해당하는 답변 목록 조회
        Page<QnaReadDTO> result = qnaRepository.readByQno(qno, PageRequest.of(0, 10));

        // Then: 결과 출력 및 검증
        result.getContent().forEach(dto -> {
            log.info("DTO: " + dto);
        });
    }


    @Test
    public void testList1() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<QuestionListDTO> result = questionRepository.list(pageable); // 반환 타입을 Page<QnaListDTO>로 변경

        // 결과 출력
        result.getContent().forEach(dto -> {
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
