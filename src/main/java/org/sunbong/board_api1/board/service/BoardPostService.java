package org.sunbong.board_api1.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.board_api1.board.domain.BoardPost;
import org.sunbong.board_api1.board.dto.BoardPostAddDTO;
import org.sunbong.board_api1.board.dto.BoardPostListDTO;
import org.sunbong.board_api1.board.dto.BoardPostReadDTO;
import org.sunbong.board_api1.board.repository.BoardPostRepository;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.exception.CommonExceptions;

import java.util.List;
import java.util.Map;

import static org.sunbong.board_api1.board.util.UploadUtil.saveFileAndCreateThumbnail;

@Service
@RequiredArgsConstructor
public class BoardPostService {

    private final BoardPostRepository boardRepository;

    public PageResponseDTO<BoardPostReadDTO> readByBno(Long bno, PageRequestDTO pageRequestDTO) {
        // 페이지 번호가 0보다 작으면 예외 발생
        if (pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }
        PageResponseDTO<BoardPostReadDTO> result = boardRepository.readByBno(bno, pageRequestDTO);
        return result;
    }

    public PageResponseDTO<BoardPostListDTO> listByBno(PageRequestDTO pageRequestDTO) {
        if (pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }
        PageResponseDTO<BoardPostListDTO> result = boardRepository.listByBno(pageRequestDTO);
        return result;
    }

    public Long newPost(BoardPostAddDTO boardPost, List<MultipartFile> files) {

        BoardPost post = BoardPost.builder()
                .title(boardPost.getTitle())
                .author(boardPost.getAuthor())
                .content(boardPost.getContent())
                .build();


        if(boardPost.getFiles() != null && !boardPost.getFiles().isEmpty()) {
            for (MultipartFile file : files) {
                Map<String, String> fileData = saveFileAndCreateThumbnail(file);
                String savedFileName = fileData.get("savedFileName");
                String thumbnailFileName = fileData.get("thumbnailFileName");
                post.addBoardAttachFile(savedFileName); // Notice 엔티티에 파일 추가
                post.addBoardAttachFile(thumbnailFileName);
            }
        }

        boardRepository.save(post);
        return post.getBno();
    }

    public void updatePost(Long bno, BoardPostAddDTO boardPost, List<MultipartFile> files) {
        BoardPost post = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        BoardPost originpost = BoardPost.builder()
                .bno(post.getBno())
                .title(post.getTitle())
                .author(post.getAuthor())
                .content(post.getContent())
                .createTime(post.getCreateTime())
                .build();
        BoardPost update = originpost.toBuilder()
                .title(boardPost.getTitle())
                .content(boardPost.getContent())
                .build();

        if(boardPost.getFiles() != null && !boardPost.getFiles().isEmpty()) {
            for (MultipartFile file : files) {
                Map<String, String> fileData = saveFileAndCreateThumbnail(file);
                String savedFileName = fileData.get("savedFileName");
                String thumbnailFileName = fileData.get("thumbnailFileName");
                update.addBoardAttachFile(savedFileName); // Notice 엔티티에 파일 추가
                update.addBoardAttachFile(thumbnailFileName);
            }
        }
        boardRepository.save(update);
    }


    // 게시글 소프트 삭제
    public void softDeletePost(Long bno) {
        // 게시글을 ID로 조회
        BoardPost post = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));


        // 삭제를 true로 바꿈
        BoardPost delete = BoardPost.builder()
                .bno(post.getBno())
                .title(post.getTitle())
                .author(post.getAuthor())
                .content(post.getContent())
                .createTime(post.getCreateTime())
                .updateTime(post.getUpdateTime())
                .delflag(true)
                .build();


        //저장
        boardRepository.save(delete);
    }
}
