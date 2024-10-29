package org.sunbong.board_api1.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.board_api1.faq.domain.ClassificationType;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQAddDTO {

    private String question;
    private String answer;

    // 분류
    private ClassificationType classificationType;

    //저장된 파일명 (파일 업로드 후 서버에 저장된 파일 이름)
    private Set<String> attachFiles;

    //업로드할 파일 (사용자가 실제로 업로드한 파일 목록)
    private List<MultipartFile> files;


}
