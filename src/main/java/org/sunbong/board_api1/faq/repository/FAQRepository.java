package org.sunbong.board_api1.faq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.board_api1.faq.domain.FAQData;

public interface FAQRepository extends JpaRepository<FAQData,Long> {

}
