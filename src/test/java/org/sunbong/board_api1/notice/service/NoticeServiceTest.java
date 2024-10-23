package org.sunbong.board_api1.notice.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.sunbong.board_api1.notice.dto.NoticeDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;

    @Test
    @Disabled
    public void insertDummyData() {
        List<NoticeDTO> dummyNotices = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            NoticeDTO noticeDTO = NoticeDTO.builder()
                    .noticeTitle("테스트 제목 " + i)
                    .noticeContent("더미 공지 내용 " + i)
                    .startDate(LocalDateTime.now()) // 현재 시간을 시작 시간으로 설정
                    .endDate(LocalDateTime.now().plusMonths(1)) // 한 달 후를 종료 시간으로 설정
                    .priority(0)
                    .isPinned(false)
                    .writer("작성자 " + i) // 작성자 이름
                    .createTime(LocalDateTime.now()) // 생성 시간
                    .updateTime(LocalDateTime.now()) // 업데이트 시간
                    .status(org.sunbong.board_api1.notice.domain.NoticeStatus.PUBLISHED) // 상태는 PUBLISHED로 설정
                    .attachDocuments(new ArrayList<>()) // 첨부파일은 없다고 가정
                    .build();

            dummyNotices.add(noticeDTO);
        }

        // 더미 데이터 저장
        dummyNotices.forEach(noticeDTO -> {
            try {
                noticeService.save(noticeDTO); // 서비스의 save 메소드 호출
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println("100개의 더미 데이터가 성공적으로 삽입되었습니다.");
    }

}