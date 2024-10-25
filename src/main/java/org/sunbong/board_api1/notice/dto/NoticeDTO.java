package org.sunbong.board_api1.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoticeDTO {
    private Long nno;
    private String title;
    private String content;
    private String writer;

    private boolean isPinned;
}
