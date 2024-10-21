package org.sunbong.board_api1.qna.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.sunbong.board_api1.common.dto.PageRequestDTO;
import org.sunbong.board_api1.qna.domain.Question;
import org.sunbong.board_api1.qna.dto.QnaListDTO;

@Log4j2
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QnaRepositoryTest {

    @Autowired
    private QnaRepository qnaRepository;

    @Test
    public void testList1() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<QnaListDTO> result = qnaRepository.list(pageable); // 반환 타입을 Page<QnaListDTO>로 변경

        // 결과 출력
        result.getContent().forEach(dto -> {
            log.info("DTO: " + dto);
        });
    }


//    @Test
//    public void testList2() {
//        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();
//        log.info(qnaRepository.listByCno(1L, pageRequestDTO));
//    }

}
