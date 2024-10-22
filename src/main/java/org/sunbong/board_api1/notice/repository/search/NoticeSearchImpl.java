package org.sunbong.board_api1.notice.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.notice.domain.Notice;
import org.sunbong.board_api1.notice.domain.NoticeStatus;
import org.sunbong.board_api1.notice.domain.QAttachedDocument;
import org.sunbong.board_api1.notice.domain.QNotice;
import org.sunbong.board_api1.notice.dto.NoticeListDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class NoticeSearchImpl extends QuerydslRepositorySupport implements NoticeSearch {

    public NoticeSearchImpl() {
        super(Notice.class);
    }

    @Override
    public PageResponseDTO<NoticeListDTO> noticeList(PageRequestDTO pageRequestDTO) {
        log.info("-------------Notice List Query-------------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("createTime").descending()
        );

        QNotice notice = QNotice.notice;
        QAttachedDocument document = QAttachedDocument.attachedDocument;

        // 고정된 공지사항 쿼리
        List<Notice> pinnedNotices = from(notice)
                .where(notice.isPinned.eq(true))
                .orderBy(notice.priority.desc(), notice.updateTime.desc())
                .fetch();

        // 일반 공지사항 쿼리
        JPQLQuery<Notice> query = from(notice)
                .leftJoin(notice.attachDocuments, document)
                .where(notice.status.eq(NoticeStatus.PUBLISHED)
                        .and(notice.isPinned.eq(false)))
                .orderBy(
                        notice.priority.desc(),
                        notice.updateTime.desc(),
                        notice.createTime.desc()
                );

        JPQLQuery<Notice> pageableQuery = getQuerydsl().applyPagination(pageable, query);

        List<Notice> noticeList = pageableQuery.fetch();

        // 고정된 공지사항을 일반 공지사항 앞에 추가
        List<Notice> combinedNotices = new ArrayList<>(pinnedNotices);
        combinedNotices.addAll(noticeList);

        if (combinedNotices.isEmpty()) {
            return PageResponseDTO.<NoticeListDTO>withAll()
                    .dtoList(new ArrayList<>())
                    .pageRequestDTO(pageRequestDTO)
                    .totalCount(0L)
                    .build();
        }

        List<NoticeListDTO> dtoList = combinedNotices.stream()
                .map(noticeEntity -> NoticeListDTO.builder()
                        .noticeNo(noticeEntity.getNoticeNo())
                        .noticeTitle(noticeEntity.getNoticeTitle())
                        .noticeContent(noticeEntity.getNoticeContent())
                        .writer(noticeEntity.getWriter())
                        .createTime(noticeEntity.getCreateTime())
                        .updateTime(noticeEntity.getUpdateTime())
                        .attachDocuments(noticeEntity.getAttachDocuments() != null && !noticeEntity.getAttachDocuments().isEmpty() ? "첨부파일 있음" : "첨부파일 없음")
                        .priority(noticeEntity.getPriority())
                        .isPinned(Boolean.TRUE.equals(noticeEntity.getIsPinned()))
                        .build())
                .collect(Collectors.toList());

        long total = query.fetchCount();

        return PageResponseDTO.<NoticeListDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(total)
                .build();
    }

}

