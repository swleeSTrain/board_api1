package org.sunbong.board_api1.member.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sunbong.board_api1.faq.domain.FAQData;
import org.sunbong.board_api1.faq.domain.QFAQData;
import org.sunbong.board_api1.faq.service.FAQService;

@Log4j2
@SpringBootTest
public class FAQTests {

    @Autowired
    private FAQService faqService;

    @Test
    public void searchTest() {







    }
}
