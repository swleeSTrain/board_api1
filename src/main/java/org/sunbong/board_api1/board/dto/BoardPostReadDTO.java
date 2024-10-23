package org.sunbong.board_api1.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardPostReadDTO {
    private Long bno;

    private String title;

    private String author;

    private String content;

    private List<String> filename; // 첨부파일 목록을 문자열 리스트로 처리
    private List<String> fileUrls;

}
