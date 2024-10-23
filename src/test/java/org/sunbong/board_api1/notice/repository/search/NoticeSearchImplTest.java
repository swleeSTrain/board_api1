package org.sunbong.board_api1.notice.repository.search;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.notice.repository.NoticeRepository;

import static org.junit.jupiter.api.Assertions.*;
@Log4j2
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NoticeSearchImplTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    public void noticeList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

    }
}