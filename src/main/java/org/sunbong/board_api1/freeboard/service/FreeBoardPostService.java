package org.sunbong.board_api1.freeboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.freeboard.domain.BoardAttachFile;
import org.sunbong.board_api1.freeboard.domain.FreeBoardPost;
import org.sunbong.board_api1.freeboard.dto.FreeBoardPostListDTO;
import org.sunbong.board_api1.freeboard.repository.FreeBoardPostRepository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreeBoardPostService {

    private final FreeBoardPostRepository freeBoardRepository;

    public Long newPost(FreeBoardPostListDTO freeBoardPost) {

        FreeBoardPost post = FreeBoardPost.builder()
                .title(freeBoardPost.getTitle())
                .author(freeBoardPost.getAuthor())
                .content(freeBoardPost.getContent())
                .build();

        freeBoardRepository.save(post);
        return post.getBno();
    }

    // 게시글 소프트 삭제
    public void softDeletePost(Long bno) {
        // 게시글을 ID로 조회
        FreeBoardPost post = freeBoardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 삭제를 true로 바꿈
        post.setDeleted(true);

        //저장
        freeBoardRepository.save(post);
    }
}
