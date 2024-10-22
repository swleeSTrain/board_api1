package org.sunbong.board_api1.qna.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.exception.CommonExceptions;
import org.sunbong.board_api1.qna.domain.Answer;
import org.sunbong.board_api1.qna.domain.Question;
import org.sunbong.board_api1.qna.dto.AnswerAddDTO;
import org.sunbong.board_api1.qna.dto.QnaReadDTO;
import org.sunbong.board_api1.qna.dto.QuestionListDTO;
import org.sunbong.board_api1.qna.dto.QuestionAddDTO;
import org.sunbong.board_api1.qna.repository.QnaRepository;
import org.sunbong.board_api1.qna.repository.QuestionRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class QnaService {

    private final QuestionRepository questionRepository;
    private final QnaRepository qnaRepository;

    // 파일 저장 경로를 설정 파일에서 읽어옴
    @Value("${org.sunbong.upload.path}")
    private String uploadDir;

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

    // 이미지 불러오기
    // 저장된 파일명을 URL로 변환
    private String convertToUrl(String fileName) {
        return "/files/" + fileName; // 웹에서 접근할 수 있는 URL 형식으로 변환
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

    @Transactional
    public Long registerAnswer(AnswerAddDTO dto) {

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
                String savedFileName = saveFile(file);
                question.addFile(savedFileName);  // 저장된 파일명을 추가
            }
        }

        Question savedQuestion = questionRepository.save(question);

        return savedQuestion.getQno();
    }

    // 파일 저장 메서드
    private String saveFile(MultipartFile file) throws IOException {
        try {
            // 파일 저장 경로 설정
            Path uploadPath = Paths.get(uploadDir);

            // 디렉토리가 없으면 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // UUID로 고유한 파일 이름을 생성
            String originalFileName = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName; // UUID 추가
            Path filePath = uploadPath.resolve(fileName);

            // 파일 저장
            Files.copy(file.getInputStream(), filePath);

            return fileName; // 고유한 파일 이름 반환
        } catch (IOException e) {
            log.error("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
            throw new IOException("파일 저장 실패", e); // 예외 처리
        }
    }

    // 삭제
    public Long delete(Long id) {
        // 존재 여부 확인 후 삭제
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found with ID: " + id));

        // 질문 ID 반환하기 전에 삭제
        Long qno = question.getQno();
        questionRepository.delete(question);

        return qno; // 삭제된 질문의 ID 반환
    }

}