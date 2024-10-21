package org.sunbong.board_api1.member.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sunbong.board_api1.faq.domain.FAQData;
import org.sunbong.board_api1.faq.repository.FAQRepository;


@SpringBootTest
@Log4j2
public class gitTest {

    @Autowired
    FAQRepository faqRepository;

    @Test
    public void test() {
       log.info("안녕하세요");
       log.info("안녕하세요!!");
       log.info("안녕하세요");
    }

    @Test
    public void faqTest(){
        FAQData faqData = FAQData.builder()
                .question("1. 물건을 환불 할려고 하는데 어떻게 하면될까요?")
                .answer("Q. 고객센터로 환불 신청 하거나 전화주시면 됩니다")
                .writer("관리자")
                .build();

        faqRepository.save(faqData);

    }
}
