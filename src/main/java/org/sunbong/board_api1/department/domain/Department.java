package org.sunbong.board_api1.department.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phoneNumber;

    private String description;


    public Department() {
    }

    // 엔티티 내부에서만 수정 가능
    public void update(String name, String phoneNumber, String description) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.description = description;
    }
}