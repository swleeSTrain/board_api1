package org.sunbong.board_api1.board.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.board_api1.board.domain.QBoardAttachFile;
import org.sunbong.board_api1.board.dto.BoardPostReadDTO;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.board.domain.BoardPost;;
import org.sunbong.board_api1.board.domain.QBoardPost;
import org.sunbong.board_api1.board.dto.BoardPostListDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
public class BoardPostSearchImpl extends QuerydslRepositorySupport implements BoardPostSearch {
    public BoardPostSearchImpl() {
        super(BoardPost.class);
    }

    @Override
    public PageResponseDTO<BoardPostListDTO> listByBno(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("bno").descending()
        );

        QBoardPost qFreeBoardPost = QBoardPost.boardPost;
        JPQLQuery<BoardPost> query = from(qFreeBoardPost)
                .where(qFreeBoardPost.delflag.eq(false));

        // 검색 조건 추가
        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType();

        if (keyword != null && type != null) {
            if (type.equals("title")) {
                query.where(qFreeBoardPost.title.containsIgnoreCase(keyword));
            } else if (type.equals("content")) {
                query.where(qFreeBoardPost.content.containsIgnoreCase(keyword));
            } else if (type.equals("author")) {
                query.where(qFreeBoardPost.author.containsIgnoreCase(keyword));
            } else if (type.equals("all")) {
                query.where(
                        qFreeBoardPost.title.containsIgnoreCase(keyword)
                                .or(qFreeBoardPost.content.containsIgnoreCase(keyword))
                                .or(qFreeBoardPost.author.containsIgnoreCase(keyword))
                );
            }
        }

        this.getQuerydsl().applyPagination(pageable, query);

        List<BoardPost> resultList = query.fetch();

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
                                .map(Collections::singletonList)
                                .orElse(Collections.emptyList()))
                        .build())
                .collect(Collectors.toList());

        long total = query.fetchCount();

        return PageResponseDTO.<BoardPostListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }


    @Override
    public PageResponseDTO<BoardPostReadDTO> readByBno(Long bno, PageRequestDTO pageRequestDTO) {
        log.info("Reading board post by bno: {}", bno);

        QBoardPost qBoardPost = QBoardPost.boardPost;
        QBoardAttachFile qBoardAttachFile = QBoardAttachFile.boardAttachFile;

        // 필요한 데이터만 선택하여 조회
        JPQLQuery<Tuple> query = from(qBoardPost)
                .leftJoin(qBoardPost.boardAttachFiles, qBoardAttachFile)
                .where(qBoardPost.bno.eq(bno)
                        .and(qBoardPost.delflag.eq(false)))
                .select(qBoardPost.bno, qBoardPost.title, qBoardPost.author, qBoardPost.content,
                        qBoardPost.createTime, qBoardPost.updateTime, qBoardAttachFile.fileName);

        List<Tuple> resultList = query.fetch();

        // DTO 변환
        List<BoardPostReadDTO> dtoList = resultList.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(qBoardPost.bno),
                        Collectors.collectingAndThen(Collectors.toList(), tuples -> {
                            BoardPostReadDTO.BoardPostReadDTOBuilder builder = BoardPostReadDTO.builder()
                                    .bno(tuples.get(0).get(qBoardPost.bno))
                                    .title(tuples.get(0).get(qBoardPost.title))
                                    .author(tuples.get(0).get(qBoardPost.author))
                                    .content(tuples.get(0).get(qBoardPost.content))
                                    .createTime(tuples.get(0).get(qBoardPost.createTime))
                                    .updateTime(tuples.get(0).get(qBoardPost.updateTime));

                            List<String> filenames = tuples.stream()
                                    .map(tuple -> tuple.get(qBoardAttachFile.fileName))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toList());

                            List<String> fileUrls = filenames.stream()
                                    .map(filename -> "/api/v1/files/" + filename)
                                    .collect(Collectors.toList());

                            return builder.filename(filenames).fileUrls(fileUrls).build();
                        })
                ))
                .values().stream().collect(Collectors.toList());

        return PageResponseDTO.<BoardPostReadDTO>withAll()
                .dtoList(dtoList)
                .totalCount(dtoList.size())
                .pageRequestDTO(pageRequestDTO)
                .build();
    }



}
