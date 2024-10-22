package org.sunbong.board_api1.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardAttachFile {

    private int ord;

    private String fileName;
}
