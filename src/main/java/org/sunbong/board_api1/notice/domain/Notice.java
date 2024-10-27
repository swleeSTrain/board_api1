package org.sunbong.board_api1.notice.domain;


import jakarta.persistence.*;
import lombok.*;
import org.sunbong.board_api1.common.domain.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nno;

    private String title;

    private String content;

    private String writer;

    @Column(name = "is_pinned", nullable = false)
    private int isPinned; // boolean 대신 int로 변경

    // 제목, 내용 업데이트
    public void updateTitleAndContent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 핀 여부를 업데이트
    public void updatePinnedStatus(int isPinned) {
        this.isPinned = isPinned;
    }

}
