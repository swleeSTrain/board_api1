package org.sunbong.board_api1.notice.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.board_api1.notice.dto.NoticeDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NoticeServiceTests {

    @Autowired
    private NoticeService noticeService;


    // 등록
    @Test
    @Transactional
    @Commit
    @Disabled
    public void testSave() throws Exception {

        NoticeDTO noticeDTO = NoticeDTO.builder()
                .title("배고프다")
                .content("Test Content")
                .writer("Test Writer")
                .isPinned(true)
                .build();

        NoticeDTO savedNoticeDTO = noticeService.save(noticeDTO);

        assertNotNull(savedNoticeDTO);
        assertEquals("배고프다", savedNoticeDTO.getTitle());
        assertEquals("Test Content", savedNoticeDTO.getContent());
        assertEquals("Test Writer", savedNoticeDTO.getWriter());

    }

    // 조회 실패
    @Test
    public void testFindFail() throws Exception{
        //given
        Long nno = 5000L;

        //when
        Exception exception = assertThrows(Exception.class, () -> {
            noticeService.findById(nno);
        });

        //then
        assertNotNull(exception.getMessage());

        assertEquals("Notice Not Found", exception.getMessage());
    }

    // 조회 성공
    @Test
    public void testFindSuccess() throws Exception{
        //given
        Long nno = 1L;
        //when
        NoticeDTO result = noticeService.findById(nno);
        //then
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals("Test Writer", result.getWriter());
    }

    // 삭제
    @Test
    public void testDelete() throws Exception{
        //given
        NoticeDTO noticeDTO = NoticeDTO.builder()
                .title("Test Title for Deletion")
                .content("Test Content")
                .writer("Test Writer")
                .build();

        NoticeDTO savedNoticeDTO = noticeService.save(noticeDTO);

        Long nno = savedNoticeDTO.getNno();

        //when
        noticeService.deleteById(nno);

        //then
        assertThrows(Exception.class, () -> noticeService.findById(nno));
    }
}