package org.sunbong.board_api1.notice.repository;

import org.springframework.data.repository.CrudRepository;
import org.sunbong.board_api1.notice.domain.Notice;
import org.sunbong.board_api1.notice.repository.search.NoticeSearch;

public interface NoticeRepository extends CrudRepository<Notice, Long>, NoticeSearch {
}
