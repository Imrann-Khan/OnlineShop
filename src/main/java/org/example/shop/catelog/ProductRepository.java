package org.example.shop.catelog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoriesId(Long categoryId, Pageable pageable);
}
