package org.sunbong.board_api1.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeListDTO {
    private Long noticeNo;
    private String noticeTitle;
    private String noticeContent;
    private String writer;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String attachDocuments;
    private int priority;
    private boolean isPinned;
}