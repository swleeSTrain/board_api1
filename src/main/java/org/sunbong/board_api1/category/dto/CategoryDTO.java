package org.sunbong.board_api1.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDTO {

    private Long cno;

    @NotNull
    private String cname;



}
