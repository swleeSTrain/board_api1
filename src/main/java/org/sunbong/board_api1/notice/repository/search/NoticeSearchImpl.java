package org.sunbong.board_api1.notice.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.board_api1.common.notice.dto.NoticePageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.notice.domain.Notice;
import org.sunbong.board_api1.notice.domain.QNotice;
import org.sunbong.board_api1.notice.dto.NoticeDTO;

import java.util.List;

@Log4j2
public class NoticeSearchImpl extends QuerydslRepositorySupport implements NoticeSearch {

    public NoticeSearchImpl() {
        super(Notice.class);
    }

    private final QNotice notice = QNotice.notice;

    //일반
    @Override
    public PageResponseDTO<NoticeDTO> notPinnedList(NoticePageRequestDTO noticePageRequestDTO) {

        long pinnedCount = getPinnedCount();

        log.info("Pinned notices count: {}", pinnedCount);

        int adjustedSize = noticePageRequestDTO.getSize() - (int) pinnedCount;

        Pageable pageable = PageRequest.of(noticePageRequestDTO.getPage() - 1, adjustedSize, Sort.by("createdDate").descending());


        BooleanBuilder searchCondition = getSearchCondition(noticePageRequestDTO);

        JPQLQuery<Notice> query = getNoticeQuery(false)
                .where(searchCondition);

        this.getQuerydsl().applyPagination(pageable, query);



        return getPageResponseDTO(query, noticePageRequestDTO);
    }


    // 고정
    @Override
    public PageResponseDTO<NoticeDTO> pinnedList(NoticePageRequestDTO noticePageRequestDTO) {

        JPQLQuery<Notice> query = getNoticeQuery(true);


        return getPageResponseDTO(query, noticePageRequestDTO);
    }



    //고정글 개수
    private long getPinnedCount() {
        JPQLQuery<Notice> pinnedQuery = from(notice)
                .where(notice.isPinned.eq(true));

        return pinnedQuery.fetchCount();
    }



    // 공통
    private PageResponseDTO<NoticeDTO> getPageResponseDTO(JPQLQuery<Notice> query, NoticePageRequestDTO noticePageRequestDTO) {

        JPQLQuery<NoticeDTO> dtoQuery = query.select(
                Projections.bean(NoticeDTO.class,
                        notice.nno,
                        notice.title,
                        notice.writer
                ));


        List<NoticeDTO> resultList = dtoQuery.fetch();

        long total = dtoQuery.fetchCount();

        log.info("Query executed. Fetched {} results, total count: {}", resultList.size(), total);

        return PageResponseDTO.<NoticeDTO>withAll()
                .dtoList(resultList)
                .noticePageRequestDTO(noticePageRequestDTO)
                .totalCount(total)
                .build();
    }

    // 공통 JPQLQuery
    private JPQLQuery<Notice> getNoticeQuery(boolean isPinned) {
        return from(notice)
                .where(notice.isPinned.eq(isPinned))
                .orderBy(notice.nno.desc());
    }

    // 검색
    private BooleanBuilder getSearchCondition(NoticePageRequestDTO noticePageRequestDTO) {
        BooleanBuilder builder = new BooleanBuilder();

        if (noticePageRequestDTO.getKeyword() != null && !noticePageRequestDTO.getKeyword().isEmpty()) {
            String searchType = noticePageRequestDTO.getSearchType();
            String keyword = noticePageRequestDTO.getKeyword();

            if ("title".equals(searchType)) {
                builder.and(notice.title.containsIgnoreCase(keyword));
            } else if ("writer".equals(searchType)) {
                builder.and(notice.writer.containsIgnoreCase(keyword));
            } else if ("content".equals(searchType)) {
                builder.and(notice.content.containsIgnoreCase(keyword));
            } else if ("titleAndWriter".equals(searchType)) {
                builder.and(notice.title.containsIgnoreCase(keyword)
                        .or(notice.writer.containsIgnoreCase(keyword)));
            }
        }

        return builder;
    }
}
