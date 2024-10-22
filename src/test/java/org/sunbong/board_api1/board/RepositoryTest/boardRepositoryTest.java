package org.sunbong.board_api1.board.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sunbong.board_api1.board.domain.BoardPost;
import org.sunbong.board_api1.board.repository.BoardPostRepository;
@SpringBootTest
public class boardRepositoryTest {
    @Autowired
    private BoardPostRepository freeBoardRepository;

    @Test
    public void insertFreeBoard() {
        BoardPost post = BoardPost.builder()
                .title("title")
                .author("author")
                .content("content")
                .build();
        freeBoardRepository.save(post);
    }

    @Test
    public void readListBoard() {

    }
}
