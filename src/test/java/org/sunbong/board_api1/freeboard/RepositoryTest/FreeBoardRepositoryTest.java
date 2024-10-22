package org.sunbong.board_api1.freeboard.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sunbong.board_api1.freeboard.domain.FreeBoardPost;
import org.sunbong.board_api1.freeboard.repository.FreeBoardPostRepository;
@SpringBootTest
public class FreeBoardRepositoryTest {
    @Autowired
    private FreeBoardPostRepository freeBoardRepository;

    @Test
    public void insertFreeBoard() {
        FreeBoardPost post = FreeBoardPost.builder()
                .title("title")
                .author("author")
                .content("content")
                .build();
        freeBoardRepository.save(post);
    }
}
