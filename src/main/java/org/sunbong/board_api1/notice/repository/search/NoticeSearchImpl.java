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

        // Pageable 생성
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("createTime").descending()
        );

        QNotice notice = QNotice.notice;
        QAttachedDocument document = QAttachedDocument.attachedDocument;

        // Notice 엔티티에서 attachDocuments에 대해 조인
        JPQLQuery<Notice> query = from(notice);
        query.leftJoin(notice.attachDocuments, document);

        // 중요도에 따라 공지사항 고정 및 상태 업데이트
        query.where(notice.status.eq(NoticeStatus.PUBLISHED)
                .and(document.ord.eq(0).or(document.isNull())));
        query.orderBy(
                notice.isPinned.desc(),    // 고정 여부
                notice.priority.desc(),    // 중요도
                notice.updateTime.desc(),  // 최근 수정 시간
                notice.createTime.desc()   // 최근 작성 시간
        );

        // 페이징 처리 적용
        JPQLQuery<Notice> pageableQuery = getQuerydsl().applyPagination(pageable, query);

        // 결과 가져오기
        List<Notice> noticeList = pageableQuery.fetch();

        if (noticeList.isEmpty()) {
            return PageResponseDTO.<NoticeListDTO>withAll()
                    .dtoList(new ArrayList<>())
                    .pageRequestDTO(pageRequestDTO)
                    .totalCount(0L)
                    .build();
        }

        // Notice 엔티티를 DTO로 변환
        List<NoticeListDTO> dtoList = noticeList.stream()
                .map(noticeEntity -> NoticeListDTO.builder()
                        .noticeNo(noticeEntity.getNoticeNo())
                        .noticeTitle(noticeEntity.getNoticeTitle())
                        .noticeContent(noticeEntity.getNoticeContent())
                        .writer(noticeEntity.getWriter())
                        .createTime(noticeEntity.getCreateTime())
                        .updateTime(noticeEntity.getUpdateTime())
                        .attachDocuments(noticeEntity.getAttachDocuments() != null && !noticeEntity.getAttachDocuments().isEmpty() ? "첨부파일 있음" : "첨부파일 없음")
                        .priority(noticeEntity.getPriority()) // 중요도
                        .isPinned(Boolean.TRUE.equals(noticeEntity.getIsPinned()))    // 고정 여부
                        .build())
                .collect(Collectors.toList());

        long total = query.fetchCount();

        // PageResponseDTO로 반환
        return PageResponseDTO.<NoticeListDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(total)
                .build();
    }
}



