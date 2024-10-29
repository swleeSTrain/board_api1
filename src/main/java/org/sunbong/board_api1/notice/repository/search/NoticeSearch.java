package org.sunbong.board_api1.notice.repository.search;

import org.sunbong.board_api1.notice.dto.NoticePageRequestDTO;
import org.sunbong.board_api1.notice.dto.NoticePageResponseDTO;
import org.sunbong.board_api1.common.util.search.SearchType;
import org.sunbong.board_api1.notice.dto.NoticeDTO;

public interface NoticeSearch  {

    NoticePageResponseDTO<NoticeDTO> getNoticesWithPinnedFirst(NoticePageRequestDTO requestDTO, SearchType searchType, String keyword);
}
