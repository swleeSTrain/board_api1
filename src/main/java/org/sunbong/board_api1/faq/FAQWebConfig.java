package org.sunbong.board_api1.faq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.sunbong.board_api1.faq.search.FAQSearchImpl;
import org.sunbong.board_api1.faq.service.FAQService;

//@Configuration
public class FAQWebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://10.10.10.54:13306")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Bean
    public FAQSearchImpl faqSearch() {
        return new FAQSearchImpl();
    }
}
