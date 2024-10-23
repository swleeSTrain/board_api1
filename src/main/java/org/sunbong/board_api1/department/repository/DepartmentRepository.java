package org.sunbong.board_api1.department.repository;

import org.sunbong.board_api1.department.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}