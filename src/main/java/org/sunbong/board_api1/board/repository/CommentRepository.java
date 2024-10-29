package org.sunbong.board_api1.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.board_api1.board.domain.Comment;
import org.sunbong.board_api1.board.repository.search.CommentSearch;

public interface CommentRepository extends JpaRepository<Comment, Long> , CommentSearch {
}
