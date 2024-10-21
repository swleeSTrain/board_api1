package org.sunbong.board_api1.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.product.dto.ProductListDTO;
import org.sunbong.board_api1.product.service.ProductService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/product")
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("permitAll()")//처음엔 여기다 집어넣기
public class ProductController {

    private final ProductService productService;

    //@PreAuthorize("permitAll()")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//여긴 나중에 안쓰면 코멘트 처리
    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> list(
            @Validated PageRequestDTO requestDTO,
//            Principal principal
            @AuthenticationPrincipal Object principal
    ) {
        log.info("--------------------------Product Controller list");
        log.info("==============================");
        log.info(principal);
        return ResponseEntity.ok(productService.list(requestDTO));
    }

}
