package org.sunbong.board_api1.board.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "boardPost")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;
    private String writer;
    private String content;

    @CreationTimestamp
    private LocalDateTime createTime;//생성날짜
    @UpdateTimestamp
    private LocalDateTime updateTime;//수정날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bno")
    private BoardPost boardPost;

}
