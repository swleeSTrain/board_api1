package org.sunbong.board_api1.cart.dto;

import lombok.Data;

@Data
public class CartDetailsListDTO {

    private Long pno;
    private String pname;
    private int price;
    private long reviewCnt;
    private String fileName;
    private int qty;

    public CartDetailsListDTO(Long pno, String pname, int price, long reviewCnt, String fileName, int qty) {
        this.pno = pno;
        this.pname = pname;
        this.price = price;
        this.reviewCnt = reviewCnt;
        this.fileName = fileName;
        this.qty = qty;
    }
}
