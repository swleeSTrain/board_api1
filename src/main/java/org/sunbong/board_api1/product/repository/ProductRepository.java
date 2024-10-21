package org.sunbong.board_api1.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.board_api1.product.domain.Product;
import org.sunbong.board_api1.product.repository.search.ProductSearch;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {
}
