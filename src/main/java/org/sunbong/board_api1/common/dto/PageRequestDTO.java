package org.sunbong.board_api1.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    @Min(value = 1, message = "1도 없어.")  // 페이지 최소값 설정
    private int page = 1;

    @Builder.Default
    @Min(value = 10, message = "10도 없어")  // 페이지 크기 최소값
    @Max(value = 100, message = "cannot over 100")  // 페이지 크기 최대값
    private int size = 10;

    // Pageable 객체를 생성하여 반환하는 메서드
    public Pageable getPageable(Sort sort) {
        // Spring Data JPA는 페이지 번호를 0부터 시작하므로 1 감소
        return PageRequest.of(this.page - 1, this.size, sort);
    }

    public Pageable getPageable() {
        // 기본 정렬: createTime 필드 내림차순
        return PageRequest.of(this.page - 1, this.size, Sort.by("createTime").descending());
    }
}
