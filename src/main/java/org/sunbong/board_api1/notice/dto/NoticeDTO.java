package org.sunbong.board_api1.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sunbong.board_api1.notice.domain.Notice;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoticeDTO {
    private Long nno;
    private String title;
    private String content;
    private String writer;

    private int isPinned; // boolean 대신 int로 변경

    // DTO -> 엔티티 변환 (등록)
    public Notice toEntity() {
        return Notice.builder()
                .title(this.title)
                .content(this.content)
                .writer(this.writer)
                .isPinned(this.isPinned)
                .build();
    }

    // 기존 엔티티 업데이트 (수정)
    public Notice toUpdatedEntity(Notice existingNotice) {
        existingNotice.updateTitleAndContent(
                this.title != null ? this.title : existingNotice.getTitle(),
                this.content != null ? this.content : existingNotice.getContent()
        );
        existingNotice.updatePinnedStatus(this.isPinned);

        return existingNotice;
    }
}
