package org.sunbong.board_api1.freeboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sunbong.board_api1.freeboard.domain.FreeBoardPost;
import org.sunbong.board_api1.freeboard.repository.search.FreeBoardPostSearch;
@Repository
public interface FreeBoardPostRepository extends JpaRepository<FreeBoardPost, Long>, FreeBoardPostSearch {
}
