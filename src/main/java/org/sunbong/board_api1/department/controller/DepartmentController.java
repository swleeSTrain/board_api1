package org.sunbong.board_api1.department.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sunbong.board_api1.department.dto.DepartmentDTO;
import org.sunbong.board_api1.department.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> createDepartment(@RequestBody DepartmentDTO dto) {
        service.createDepartment(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<DepartmentDTO> getAllDepartments() {
        return service.getAllDepartments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDepartmentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO dto) {
        service.updateDepartment(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        service.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}