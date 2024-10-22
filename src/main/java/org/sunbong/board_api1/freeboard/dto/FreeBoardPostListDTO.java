package org.sunbong.board_api1.freeboard.dto;

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
public class FreeBoardPostListDTO {
    private Long bno;

    private String title;

    private String author;

    private String content;

    private List<String> fileName;


}
