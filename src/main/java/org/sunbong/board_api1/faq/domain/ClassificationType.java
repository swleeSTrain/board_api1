package org.sunbong.board_api1.faq.domain;

public enum ClassificationType {

    DELIVERY_CONFIRMATION("배송확인"),
    CANCELLATION("취소"),
    REFUND("환불"),
    EXCHANGE_REQUEST("교환신청"),
    RETURN_REQUEST("반품신청"),
    PURCHASE_ORDER("발주"),
    INVENTORY_MANAGEMENT("재고관리");

   private final String name;

   ClassificationType(String name) {
       this.name = name;
   }

   public String getName() {
       return name;
   }

}
