package org.sunbong.board_api1.board.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.board_api1.board.dto.BoardPostReadDTO;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.board.domain.BoardPost;;
import org.sunbong.board_api1.board.domain.QBoardPost;
import org.sunbong.board_api1.board.dto.BoardPostListDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class BoardPostSearchImpl extends QuerydslRepositorySupport implements BoardPostSearch {
    public BoardPostSearchImpl() {
        super(BoardPost.class);
    }

    @Override
    public PageResponseDTO<BoardPostListDTO> listByBno(PageRequestDTO pageRequestDTO) {
        // Pageable 객체 생성 (PageRequestDTO에서 페이지 정보 및 정렬 정보 가져옴)
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,   // 페이지 번호 (0부터 시작하므로 -1)
                pageRequestDTO.getSize(),       // 페이지 크기
                Sort.by("bno").descending() // bno로 정렬
        );
        QBoardPost qFreeBoardPost = QBoardPost.boardPost;
        // 기본 쿼리 생성 (FreeBoardPost에서 bno 기준으로 필터링)
        JPQLQuery<BoardPost> query = from(qFreeBoardPost)
                .where(qFreeBoardPost.delflag.eq(false));
        // 페이징 처리 적용
        this.getQuerydsl().applyPagination(pageable, query);

        // 쿼리 실행 후 결과를 가져옴
        List<BoardPost> resultList = query.fetch();

        // DTO로 변환 (Entity -> DTO)
        List<BoardPostListDTO> dtoList = resultList.stream()
                .map(post -> BoardPostListDTO.builder()
                        .bno(post.getBno())
                        .title(post.getTitle())
                        .author(post.getAuthor())
                        .fileName(post.getBoardAttachFiles()
                                .stream()
                                .map(file -> file.getFileName())
                                .filter(fileName -> fileName.startsWith("s_"))
                                .findFirst()
                                .map(Collections::singletonList) // 파일이 있으면 해당 파일 이름으로 리스트 생성
                                .orElse(Collections.emptyList())) // 파일이 없으면 빈 리스트 반환
                        .build())
                .collect(Collectors.toList());

        // 전체 데이터 개수 구하기
        long total = query.fetchCount();

        // PageResponseDTO로 페이징 정보와 DTO 리스트 반환
        return PageResponseDTO.<BoardPostListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public PageResponseDTO<BoardPostReadDTO> readByBno(Long bno, PageRequestDTO pageRequestDTO) {
        log.info("--------------------------read by bno---------------------------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("bno").descending()
        );

        QBoardPost qBoardPost = QBoardPost.boardPost;

        JPQLQuery<BoardPost> query = from(qBoardPost)
                .where(qBoardPost.bno.eq(bno)
                        .and(qBoardPost.delflag.eq(false)));

        List<BoardPost> resultList = query.fetch();

        List<BoardPostReadDTO> dtoList = resultList.stream()
                .map(post -> BoardPostReadDTO.builder()
                        .bno(post.getBno())
                        .title(post.getTitle())
                        .author(post.getAuthor())
                        .content(post.getContent())
                        .filename(post.getBoardAttachFiles() // 파일 이름만 추출
                                .stream()
                                .map(file -> file.getFileName())
                                .collect(Collectors.toList()))
                        .fileUrls(post.getBoardAttachFiles() // 파일 URL 생성
                                .stream()
                                .map(file -> "/api/v1/files/" + file.getFileName())  // 파일 요청 URL
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        long total = query.fetchCount();

        return PageResponseDTO.<BoardPostReadDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

}
