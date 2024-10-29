package org.sunbong.board_api1.notice.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.board_api1.notice.dto.NoticePageResponseDTO;
import org.sunbong.board_api1.notice.dto.NoticePageRequestDTO;
import org.sunbong.board_api1.notice.dto.NoticeDTO;
import org.sunbong.board_api1.common.util.search.SearchType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Log4j2
public class NoticeServiceTests {

    @Autowired
    private NoticeService noticeService;

    // 공지사항 등록 테스트
    @Test
    @Commit
    @Disabled
    public void testSave() throws Exception {
        NoticeDTO noticeDTO = NoticeDTO.builder()
                .title("Test Title")
                .content("Test Content")
                .writer("Test Writer")
                .isPinned(0)
                .build();

        NoticeDTO savedNoticeDTO = noticeService.save(noticeDTO);

        assertNotNull(savedNoticeDTO);
        assertEquals("Test Title", savedNoticeDTO.getTitle());
        assertEquals("Test Content", savedNoticeDTO.getContent());
        assertEquals("Test Writer", savedNoticeDTO.getWriter());
    }

    // 공지사항 조회 성공/실패 테스트
    @Test
    public void testFindNotice() throws Exception {
        // 실패하는 조회 테스트
        Long invalidNno = 9999L;
        Exception exception = assertThrows(Exception.class, () -> noticeService.findById(invalidNno));
        assertEquals("Notice with ID " + invalidNno + " not found.", exception.getMessage());

        // 성공하는 조회 테스트
        NoticeDTO savedNoticeDTO = noticeService.save(NoticeDTO.builder()
                .title("Existing Notice")
                .content("Content")
                .writer("Writer")
                .isPinned(0)
                .build());

        NoticeDTO foundNoticeDTO = noticeService.findById(savedNoticeDTO.getNno());
        assertNotNull(foundNoticeDTO);
        assertEquals(savedNoticeDTO.getTitle(), foundNoticeDTO.getTitle());
    }

    // 공지사항 삭제 테스트
    @Test
    public void testDelete() throws Exception {
        NoticeDTO noticeDTO = NoticeDTO.builder()
                .title("Notice for Deletion")
                .content("Content")
                .writer("Writer")
                .isPinned(0)
                .build();

        NoticeDTO savedNoticeDTO = noticeService.save(noticeDTO);
        Long nno = savedNoticeDTO.getNno();
        noticeService.deleteById(nno);

        assertThrows(Exception.class, () -> noticeService.findById(nno));
    }

    // 공지사항 수정 테스트
    @Test
    public void testUpdateNotice() throws Exception {
        NoticeDTO noticeDTO = NoticeDTO.builder()
                .title("Original Title")
                .content("Original Content")
                .writer("User1")
                .isPinned(0)
                .build();

        NoticeDTO savedNoticeDTO = noticeService.save(noticeDTO);
        Long nno = savedNoticeDTO.getNno();

        NoticeDTO updatedNoticeDTO = NoticeDTO.builder()
                .title("Updated Title")
                .content("Updated Content")
                .isPinned(1)
                .build();

        NoticeDTO result = noticeService.updateNotice(nno, updatedNoticeDTO);

        assertNotNull(result);
        assertEquals(nno, result.getNno());
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Content", result.getContent());
        assertEquals(1, result.getIsPinned(), "고정 상태가 업데이트되어야 합니다.");
    }

    @Test
    public void testGetAllNoticesWithPinnedFirst() {
        // Given
        NoticePageRequestDTO requestDTO = NoticePageRequestDTO.builder()
                .page(1)
                .size(10)  // 최대 페이지 크기
                .build();

        // When
        NoticePageResponseDTO<NoticeDTO> response = noticeService.getAllNoticesWithPinnedFirst(requestDTO);

        // Then
        assertNotNull(response, "응답이 null이 아닙니다.");

        // 고정 공지 개수와 일반 공지 개수를 합하여 size 범위 내에 있는지 확인
        int pinnedCount = (int) response.getDtoList().stream().filter(notice -> notice.getIsPinned() == 1).count();
        int regularCount = (int) response.getDtoList().stream().filter(notice -> notice.getIsPinned() == 0).count();

        // 고정 공지와 일반 공지가 합쳐져서 size 이내인지 확인
        assertTrue(pinnedCount + regularCount <= requestDTO.getSize(), "결과 리스트의 크기가 페이지 크기를 넘지 않아야 합니다.");

        // 고정 공지가 리스트 상단에 위치하고, 일반 공지가 그 아래에 위치하는지 확인
        boolean hasPinned = false;
        for (NoticeDTO notice : response.getDtoList()) {
            if (notice.getIsPinned() == 1) {
                hasPinned = true;
            } else if (hasPinned) {
                assertEquals(0, notice.getIsPinned(), "고정 공지 아래에는 일반 공지가 와야 합니다.");
            }
        }
    }


    @Test
    public void testSearchNoticesWithPinnedFirst() {
        // Given: 검색 조건 설정
        NoticePageRequestDTO requestDTO = NoticePageRequestDTO.builder()
                .page(1)
                .size(10)
                .searchType(SearchType.TITLE) // 수정된 부분
                .keyword("test")
                .build();

        // When: 서비스에서 공지사항 조회
        NoticePageResponseDTO<NoticeDTO> response = noticeService.searchNotices(requestDTO.getSearchType(), "test", requestDTO);

        // Then: 응답이 null이 아닌지 확인
        assertThat(response).isNotNull();

        // 검색어가 포함된 공지사항만 나오는지 확인 (대소문자 무시)
        for (NoticeDTO notice : response.getDtoList()) {
            if (notice.getIsPinned() == 0) {
                assertThat(notice.getTitle().toLowerCase()).contains("test")
                        .withFailMessage("검색 조건에 맞는 공지사항만 나와야 합니다.");
            }
        }

        // 전체 개수 로그
        log.info("검색 결과 총 개수: {}", response.getTotalCount());
    }

}
