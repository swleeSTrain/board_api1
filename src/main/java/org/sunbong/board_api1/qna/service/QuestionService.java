package org.sunbong.board_api1.qna.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.exception.CommonExceptions;
import org.sunbong.board_api1.common.util.FileUtil;
import org.sunbong.board_api1.qna.domain.Question;
import org.sunbong.board_api1.qna.dto.QnaReadDTO;
import org.sunbong.board_api1.qna.dto.QuestionAddDTO;
import org.sunbong.board_api1.qna.dto.QuestionListDTO;
import org.sunbong.board_api1.qna.repository.AnswerRepository;
import org.sunbong.board_api1.qna.repository.QuestionRepository;

import java.io.IOException;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    // SaveFile 의존성 주입
    private final FileUtil fileUtil;  // SaveFile 클래스 주입

    // 이미지 불러오기
    // 저장된 파일명을 URL로 변환
    private String convertToUrl(String fileName) {
        return "/files/" + fileName; // 웹에서 접근할 수 있는 URL 형식으로 변환
    }

    // 질문 리스트
    public PageResponseDTO<QuestionListDTO> list(PageRequestDTO pageRequestDTO) {

        // 페이지 번호가 0보다 작으면 예외 발생
        if (pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }

        // QuestionRepository의 list 메서드를 호출하여 페이징 결과 얻음
        PageResponseDTO<QuestionListDTO> result = questionRepository.questionList(pageRequestDTO);

        // 결과를 그대로 반환
        return result;
    }

    // 질문 등록
    public Long registerQuestion(QuestionAddDTO dto) throws IOException {

        // Question 엔티티 생성
        Question question = Question.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .tags(dto.getTags())
                .build();

        // 업로드할 파일이 있을 경우
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            for (MultipartFile file : dto.getFiles()) {
                // 파일 저장 후 저장된 파일명을 question에 추가
                String savedFileName = fileUtil.saveFile(file);
                question.addFile(savedFileName);  // 저장된 파일명을 추가
            }
        }

        Question savedQuestion = questionRepository.save(question);

        return savedQuestion.getQno();
    }


    // 질문 삭제
    public Long delete(Long id) {

        // 존재 여부 확인 후 삭제
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found with ID: " + id));

        // 질문 ID 반환하기 전에 삭제
        Long qno = question.getQno();
        questionRepository.delete(question);

        return qno; // 삭제된 질문의 ID 반환
    }

    // 질문 수정
    public Long updateQuestion(Long qno, QuestionAddDTO dto) throws IOException {
        Question question = questionRepository.findById(qno)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));

        // 기존 질문을 수정
        Question updatedQuestion = question.toBuilder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .tags(dto.getTags())
                .writer(dto.getWriter())
                .build();

        // 기존 파일 삭제
        if (dto.getAttachFiles() != null && !dto.getAttachFiles().isEmpty()) {
            for (String fileName : dto.getAttachFiles()) {
                fileUtil.deleteFile(fileName); // 여기에서 QnaService의 deleteFile 메서드 호출
            }
        }

        // 새로운 파일 추가
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            for (MultipartFile file : dto.getFiles()) {
                String savedFileName = fileUtil.saveFile(file);
                updatedQuestion.addFile(savedFileName); // 새로운 파일 추가
            }
        }

        // 질문 업데이트
        questionRepository.save(updatedQuestion);

        return updatedQuestion.getQno(); // 업데이트된 질문의 ID 반환
    }




}
