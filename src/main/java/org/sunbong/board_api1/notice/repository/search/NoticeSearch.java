package org.sunbong.board_api1.notice.repository.search;

import org.sunbong.board_api1.common.notice.dto.NoticePageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.notice.dto.NoticeDTO;

public interface NoticeSearch  {

    PageResponseDTO<NoticeDTO> notPinnedList(NoticePageRequestDTO noticePageRequestDTO);

    PageResponseDTO<NoticeDTO> pinnedList(NoticePageRequestDTO noticePageRequestDTO);
}
