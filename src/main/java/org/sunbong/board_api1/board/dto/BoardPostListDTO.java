package org.sunbong.board_api1.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardPostListDTO {
    private Long bno;
    private String title;
    private String author;

    private List<String> fileName;


}
