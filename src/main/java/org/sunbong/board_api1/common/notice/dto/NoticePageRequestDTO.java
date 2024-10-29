package org.sunbong.board_api1.common.notice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.sunbong.board_api1.notice.repository.search.SearchType;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NoticePageRequestDTO {

    @Builder.Default
    @Min(value = 1, message = "1도 없어.")
    private int page = 1;

    @Builder.Default
    @Min(value = 10, message = "10도 없어")
    @Max(value = 100, message = "cannot over 100")
    private int size = 30;

    private SearchType searchType;  // 검색 타입 ("title", "writer", "content")
    private String keyword;
}