package org.sunbong.board_api1.notice.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.board_api1.notice.dto.NoticePageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.util.search.SearchType;
import org.sunbong.board_api1.notice.domain.Notice;
import org.sunbong.board_api1.notice.domain.QNotice;
import org.sunbong.board_api1.notice.dto.NoticeDTO;

import java.util.ArrayList;
import java.util.List;



@Log4j2
public class NoticeSearchImpl extends QuerydslRepositorySupport implements NoticeSearch {

    private final QNotice notice = QNotice.notice;

    public NoticeSearchImpl() {
        super(Notice.class);
    }

    @Override
    public PageResponseDTO<NoticeDTO> getNoticesWithPinnedFirst(NoticePageRequestDTO requestDTO, SearchType searchType, String keyword) {
        log.info("고정된 공지사항 목록을 먼저 조회합니다.");
        List<NoticeDTO> pinnedList = getPinnedNotices();

        int pinnedCount = pinnedList.size();
        int remainingSize = Math.max(0, requestDTO.getSize() - pinnedCount);

        searchType = (searchType == null) ? SearchType.TITLE_WRITER_CONTENT : searchType;
        BooleanBuilder condition = createSearchCondition(searchType, keyword);

        log.info("고정된 공지사항 이후 일반 공지사항 목록을 조회합니다.");
        PageResponseDTO<NoticeDTO> regularList = getRegularNoticesWithCondition(
                NoticePageRequestDTO.builder()
                        .page(requestDTO.getPage())
                        .size(remainingSize)
                        .build(),
                condition
        );

        List<NoticeDTO> combinedList = new ArrayList<>(pinnedList);
        combinedList.addAll(regularList.getDtoList());
        long total = pinnedCount + regularList.getTotalCount();

        return PageResponseDTO.<NoticeDTO>withAll()
                .dtoList(combinedList)
                .noticePageRequestDTO(requestDTO)
                .totalCount(total)
                .build();
    }

    private List<NoticeDTO> getPinnedNotices() {
        log.info("고정된 공지사항에 대한 쿼리를 실행합니다.");

        JPQLQuery<Notice> query = from(notice)
                .where(notice.isPinned.eq(1))
                .orderBy(notice.createdDate.desc());

        List<NoticeDTO> result = query.select(Projections.bean(NoticeDTO.class,
                notice.nno,
                notice.title,
                notice.writer,
                notice.isPinned
        )).fetch();

        log.info("고정된 공지사항 조회 결과: {}건", result.size());
        return result;
    }

    private PageResponseDTO<NoticeDTO> getRegularNoticesWithCondition(NoticePageRequestDTO requestDTO, BooleanBuilder condition) {
        log.info("일반 공지사항에 대한 쿼리를 실행합니다. 페이지 번호: {}, 페이지 크기: {}", requestDTO.getPage(), requestDTO.getSize());

        Pageable pageable = PageRequest.of(requestDTO.getPage() - 1, requestDTO.getSize(), Sort.by("createdDate").descending());
        JPQLQuery<Notice> query = from(notice)
                .where(condition.and(notice.isPinned.eq(0)));

        this.getQuerydsl().applyPagination(pageable, query);

        List<NoticeDTO> resultList = query.select(Projections.bean(NoticeDTO.class,
                notice.nno,
                notice.title,
                notice.writer,
                notice.isPinned
        )).fetch();

        long total = query.fetchCount();

        log.info("일반 공지사항 조회 결과: {}건", resultList.size());
        log.info("일반 공지사항 총 개수: {}건", total);

        return PageResponseDTO.<NoticeDTO>withAll()
                .dtoList(resultList)
                .noticePageRequestDTO(requestDTO)
                .totalCount(total)
                .build();
    }

    private BooleanBuilder createSearchCondition(SearchType searchType, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            log.info("검색어가 없습니다. 조건 없이 조회합니다.");
            return new BooleanBuilder();
        }

        log.info("검색 조건을 생성합니다. 검색 유형: {}, 검색어: {}", searchType, keyword);
        return searchType.buildCondition(notice, keyword);
    }
}

