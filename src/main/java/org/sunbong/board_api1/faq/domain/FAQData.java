package org.sunbong.board_api1.faq.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString(exclude= "attachFiles")
@Getter
@Setter
//@Table(name = "faq_tbl")
public class FAQData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;


    @Column(name = "question")
    @NotBlank(message = "필수 항목입니다.")
    private String question;

    @NotBlank(message = "필수 항목입니다.")
    @Column(name = "answer")
    private String answer;


    @NotNull
    @Column(name = "classificationType")
    @Enumerated(EnumType.STRING)
    private ClassificationType classificationType;

    @Column(name = "attachFiles" )
    @ElementCollection
    @Builder.Default
    private Set<AttachFile> attachFiles = new HashSet<>();

    public void addFile(String filename) {
        attachFiles.add(new AttachFile(attachFiles.size(), filename));
    }

    public void clearFile() { attachFiles.clear(); }


}
