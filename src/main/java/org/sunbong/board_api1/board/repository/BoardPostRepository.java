package org.sunbong.board_api1.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sunbong.board_api1.board.domain.BoardPost;
import org.sunbong.board_api1.board.repository.search.BoardPostSearch;
@Repository
public interface BoardPostRepository extends JpaRepository<BoardPost, Long>, BoardPostSearch {
}
