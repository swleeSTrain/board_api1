package org.sunbong.board_api1.department.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {

    private Long dno;
    private String name;
    private String phoneNumber;
    private String description;
}