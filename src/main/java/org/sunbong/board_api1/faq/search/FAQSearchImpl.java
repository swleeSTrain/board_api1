package org.sunbong.board_api1.faq.search;


import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.faq.domain.FAQData;
import org.sunbong.board_api1.faq.dto.FAQListDTO;
import org.sunbong.board_api1.faq.domain.QAttachFile;
import org.sunbong.board_api1.faq.domain.QFAQData;

@Log4j2
public class FAQSearchImpl extends QuerydslRepositorySupport implements FAQSearch{

    public FAQSearchImpl() {
        super(FAQData.class);
    }

    @Override
    public Page<FAQData> list(Pageable pageable) {
        QFAQData faqData = QFAQData.fAQData;
        QAttachFile attachFile = QAttachFile.attachFile;

        JPQLQuery<FAQData> query = from(faqData);
        query.leftJoin(faqData.attachFiles, attachFile);
        query.where(faqData.question.contains("가"));
        query.groupBy(faqData);
        Page<FAQData> pageData = new PageImpl<FAQData>(query.fetch(), pageable, query.fetchCount());

        this.getQuerydsl().applyPagination(pageable, query);
                query.select(
                        faqData,
                        attachFile.fileName
                );


        return
    }

    @Override
    public PageResponseDTO<FAQListDTO> listByFno(Long fno, PageRequestDTO pageRequestDTO) {
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize());
        QFAQData faqData = QFAQData.fAQData;
        PageResponseDTO pageDto = PageResponseDTO.withAll()
                .dtoList()
                .pageRequestDTO(pageRequestDTO)
                .totalCount(pageRequest)
                .build();




        )








    }



}
















