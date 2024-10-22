package org.sunbong.board_api1.freeboard.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Embeddable
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class BoardAttachFile {

    private int ord;
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    private FreeBoardPost freeBoardPost;
}
