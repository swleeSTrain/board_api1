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
        List<NoticeDTO> pinnedList = getPinnedNotices();
        int pinnedCount = pinnedList.size();

        int remainingSize = Math.max(0, requestDTO.getSize() - pinnedCount);

        searchType = (searchType == null) ? SearchType.TITLE_WRITER_CONTENT : searchType;

        BooleanBuilder condition = createSearchCondition(searchType, keyword);

        PageResponseDTO<NoticeDTO> regularList = getRegularNoticesWithCondition(
                NoticePageRequestDTO.builder()
                        .page(requestDTO.getPage())
                        .size(remainingSize)
                        .build(),
                condition);

        List<NoticeDTO> combinedList = new ArrayList<>(pinnedList);

        combinedList.addAll(regularList.getDtoList());

        long total = pinnedCount + regularList.getTotalCount();

        return PageResponseDTO.<NoticeDTO>withAll()
                .dtoList(combinedList)
                .noticePageRequestDTO(requestDTO)
                .totalCount(total)
                .build();
    }


    // 고정된 공지사항 목록 조회
    private List<NoticeDTO> getPinnedNotices() {
        JPQLQuery<Notice> query = from(notice)
                .where(notice.isPinned.eq(1))
                .orderBy(notice.createdDate.desc());

        return query.select(Projections.bean(NoticeDTO.class,
                notice.nno,
                notice.title,
                notice.writer,
                notice.isPinned
        )).fetch();
    }

    // 검색 조건을 적용한 일반 공지사항 조회
    private PageResponseDTO<NoticeDTO> getRegularNoticesWithCondition(NoticePageRequestDTO requestDTO, BooleanBuilder condition) {
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

        return PageResponseDTO.<NoticeDTO>withAll()
                .dtoList(resultList)
                .noticePageRequestDTO(requestDTO)
                .totalCount(total)
                .build();
    }

    private BooleanBuilder createSearchCondition(SearchType searchType, String keyword) {

        if (keyword == null || keyword.isEmpty()) {

            return new BooleanBuilder(); // 검색어가 없으면 빈 조건 반환
        }

        return searchType.buildCondition(notice, keyword);
    }
}
