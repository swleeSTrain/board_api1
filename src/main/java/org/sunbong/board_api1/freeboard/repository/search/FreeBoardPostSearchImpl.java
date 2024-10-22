package org.sunbong.board_api1.freeboard.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.freeboard.domain.FreeBoardPost;
import org.sunbong.board_api1.freeboard.domain.QFreeBoardPost;
import org.sunbong.board_api1.freeboard.dto.FreeBoardPostListDTO;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class FreeBoardPostSearchImpl extends QuerydslRepositorySupport implements FreeBoardPostSearch {
    public FreeBoardPostSearchImpl() {
        super(FreeBoardPost.class);
    }

    @Override
    public Page<FreeBoardPost> boardlist(Pageable pageable) {
        log.info("-----------boardlist------------");
        QFreeBoardPost qFreeBoardPost = QFreeBoardPost.freeBoardPost;
        JPQLQuery<FreeBoardPost> query = from(qFreeBoardPost)
                .where(qFreeBoardPost.deleted.eq(false));

        this.getQuerydsl().applyPagination(pageable, query);
        JPQLQuery<FreeBoardPost> tupleQuery =
                query.select(
                        qFreeBoardPost
                );

        // QueryDSL로 쿼리 실행
        List<FreeBoardPost> resultList =tupleQuery.fetch();
        long total = tupleQuery.fetchCount();
        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public PageResponseDTO<FreeBoardPostListDTO> listByBno(long bno, PageRequestDTO pageRequestDTO) {
        // Pageable 객체 생성 (PageRequestDTO에서 페이지 정보 및 정렬 정보 가져옴)
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,   // 페이지 번호 (0부터 시작하므로 -1)
                pageRequestDTO.getSize(),       // 페이지 크기
                Sort.by("bno").descending() // bno로 정렬
        );
        QFreeBoardPost qFreeBoardPost = QFreeBoardPost.freeBoardPost;
        // 기본 쿼리 생성 (FreeBoardPost에서 bno 기준으로 필터링)
        JPQLQuery<FreeBoardPost> query = from(qFreeBoardPost)
                .where(qFreeBoardPost.bno.eq(bno)
                        .and(qFreeBoardPost.deleted.eq(false)));
        // 페이징 처리 적용
        this.getQuerydsl().applyPagination(pageable, query);

        // 쿼리 실행 후 결과를 가져옴
        List<FreeBoardPost> resultList = query.fetch();

        // DTO로 변환 (Entity -> DTO)
        List<FreeBoardPostListDTO> dtoList = resultList.stream()
                .map(post -> FreeBoardPostListDTO.builder()
                        .bno(post.getBno())
                        .title(post.getTitle())
                        .author(post.getAuthor())
                        .content(post.getContent())
                        .build())
                .collect(Collectors.toList());

        // 전체 데이터 개수 구하기
        long total = query.fetchCount();

        // PageResponseDTO로 페이징 정보와 DTO 리스트 반환
        return PageResponseDTO.<FreeBoardPostListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
