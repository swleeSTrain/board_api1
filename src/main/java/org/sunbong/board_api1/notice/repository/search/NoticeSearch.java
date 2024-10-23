package org.sunbong.board_api1.notice.repository.search;

import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.common.dto.PageResponseDTO;
import org.sunbong.board_api1.notice.dto.NoticeListDTO;

public interface NoticeSearch {
    PageResponseDTO<NoticeListDTO> noticeList(PageRequestDTO pageRequestDTO);
}
