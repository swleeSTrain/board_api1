package org.sunbong.board_api1.board.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "boardPost")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    private String content;

    @CreationTimestamp
    private LocalDateTime createTime;//생성날짜
    @UpdateTimestamp
    private LocalDateTime updateTime;//수정날짜

    @ManyToOne(fetch = FetchType.LAZY)
    private BoardPost boardPost;



}
