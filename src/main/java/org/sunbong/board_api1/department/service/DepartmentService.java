package org.sunbong.board_api1.department.service;


import org.springframework.stereotype.Service;
import org.sunbong.board_api1.department.domain.Department;
import org.sunbong.board_api1.department.dto.DepartmentDTO;
import org.sunbong.board_api1.department.repository.DepartmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    // CREATE: 부서 생성
    public void createDepartment(DepartmentDTO dto) {
        Department department = new Department();
        department.update(dto.getName(), dto.getPhoneNumber(), dto.getDescription());
        repository.save(department);
    }

    // READ: 모든 부서 조회
    public List<DepartmentDTO> getAllDepartments() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // READ: 특정 부서 조회
    public DepartmentDTO getDepartmentById(Long id) {
        Department department = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("부서를 찾을 수 없습니다."));
        return convertToDTO(department);
    }

    // UPDATE: 부서 수정
    public void updateDepartment(Long id, DepartmentDTO dto) {
        Department department = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("부서를 찾을 수 없습니다."));
        department.update(dto.getName(), dto.getPhoneNumber(), dto.getDescription());
        repository.save(department);
    }

    // DELETE: 부서 삭제
    public void deleteDepartment(Long id) {
        repository.deleteById(id);
    }

    // 엔티티 → DTO 변환 메서드
    private DepartmentDTO convertToDTO(Department department) {
        return DepartmentDTO.builder()
                .dno(department.getId())
                .name(department.getName())
                .phoneNumber(department.getPhoneNumber())
                .description(department.getDescription())
                .build();
    }
}