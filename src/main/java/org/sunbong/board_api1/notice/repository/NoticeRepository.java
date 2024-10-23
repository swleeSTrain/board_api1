
package org.sunbong.board_api1.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.sunbong.board_api1.notice.domain.Notice;
import org.sunbong.board_api1.notice.repository.search.NoticeSearch;

public interface NoticeRepository extends CrudRepository<Notice, Long>, NoticeSearch {

    // 제목, 내용, 작성자 중 하나라도 일치하는 항목을 검색
    @Query("SELECT n FROM Notice n WHERE " +
            "(:title IS NULL OR n.noticeTitle LIKE %:title%) AND " +
            "(:content IS NULL OR n.noticeContent LIKE %:content%) AND " +
            "(:writer IS NULL OR n.writer LIKE %:writer%)")
    Page<Notice> findByTitleAndContentAndWriter(
            String title, String content, String writer, Pageable pageable);
}

