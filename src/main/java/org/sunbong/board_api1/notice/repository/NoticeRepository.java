package org.sunbong.board_api1.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.board_api1.notice.domain.Notice;
import org.sunbong.board_api1.notice.repository.search.NoticeSearch;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeSearch {
}
