package org.sunbong.board_api1.notice.repository.search;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.board_api1.notice.dto.NoticeDTO;
import org.sunbong.board_api1.notice.service.NoticeService;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DummyInsertTest {

    @Autowired
    private NoticeService noticeService;

    @Test
    @Transactional
    @Commit
    @Disabled
    public void testSaveMultiple() throws Exception {

        // 500개의 NoticeDTO 생성 및 저장
        IntStream.rangeClosed(1, 55).forEach(i -> {

            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .title("Test Title " + i)
                    .content("Test Content " + i)
                    .writer("홍길동 " + i)
                    .build();

            NoticeDTO savedNoticeDTO = null;
            try {
                savedNoticeDTO = noticeService.save(noticeDTO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // 저장된 결과 검증
            assertNotNull(savedNoticeDTO);
            assertEquals("Test Title " + i, savedNoticeDTO.getTitle());
            assertEquals("Test Content " + i, savedNoticeDTO.getContent());
            assertEquals("홍길동 " + i, savedNoticeDTO.getWriter());
        });
    }
}
