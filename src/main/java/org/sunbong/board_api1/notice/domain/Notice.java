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
    @Builder.Default
    private boolean isPinned = Boolean.FALSE;

}
