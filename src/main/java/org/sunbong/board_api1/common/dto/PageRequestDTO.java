package org.sunbong.board_api1.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    @Min(value = 1, message = "1도 없어.")
    private int page = 1;

    @Builder.Default
    @Min(value = 10, message = "10도 없어")
    @Max(value = 100, message = "cannot over 100")
    private int size = 10;

}