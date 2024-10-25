package org.sunbong.board_api1.notice.repository.search;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.common.notice.dto.NoticePageRequestDTO;
import org.sunbong.board_api1.notice.dto.NoticeDTO;
import org.sunbong.board_api1.notice.repository.NoticeRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Log4j2
public class NoticeSearchImplTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    public void testSearchByTitle() throws Exception{

        //given
        NoticePageRequestDTO requestDTO = new NoticePageRequestDTO();

        requestDTO.setSearchType("title");
        requestDTO.setKeyword("공지");
        requestDTO.setPage(3);
        requestDTO.setSize(30);

        //when
        PageResponseDTO<NoticeDTO> result = noticeRepository.notPinnedList(requestDTO);

        assertThat(result.getDtoList()).isNotEmpty();
        assertThat(result.getDtoList()).allMatch(notice -> notice.getTitle().contains("공지"));

        //then
        log.info("--------------------------------------");
        log.info("testSearchByTitle - Result size: {}", result.getDtoList().size());
    }
}