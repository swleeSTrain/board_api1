package org.sunbong.board_api1.common.dto;

import lombok.Builder;
import lombok.Data;
import org.sunbong.board_api1.common.notice.dto.NoticePageRequestDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {
    private List<E> dtoList;
    private List<Integer> pageNumList;
    private NoticePageRequestDTO noticePageRequestDTO;
    private boolean prev, next;
    private int totalCount, prevPage, nextPage, totalPage, current;
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, NoticePageRequestDTO noticePageRequestDTO, long totalCount) {
        this.dtoList = dtoList;
        this.noticePageRequestDTO = noticePageRequestDTO;
        this.totalCount = (int)totalCount;
        int end = (int)(Math.ceil( noticePageRequestDTO.getPage() / 10.0 )) * 10;
        int start = end - 9;
        int last = (int)(Math.ceil((totalCount/(double) noticePageRequestDTO.getSize())));
        end = end > last ? last: end;
        this.prev = start > 1;
        this.next = totalCount > end * noticePageRequestDTO.getSize();
        this.pageNumList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
        if(prev)
            this.prevPage = start -1;
        if(next)
            this.nextPage = end + 1;
        this.totalPage = this.pageNumList.size();
        this.current = noticePageRequestDTO.getPage();
    }
}