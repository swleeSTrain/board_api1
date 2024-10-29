package org.sunbong.board_api1.faq.PageImpl;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.sunbong.board_api1.faq.dto.FAQListDTO;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class ageImpl<FAQListDTO> implements Page<FAQListDTO> {

    private final List<org.sunbong.board_api1.faq.dto.FAQListDTO> content;
    private final Pageable pageable;
    private final long total;

    public PageImpl(List<org.sunbong.board_api1.faq.dto.FAQListDTO> content, Pageable pageable, long total) {
        this.content = content;
        this.pageable = pageable;
        this.total = total;
    }


    @Override
    public int getTotalPages() {
        return pageable.getPageSize() == 0 ? 1 : (int) Math.ceil((double));
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public Page map(Function converter) {
        return null;
    }

    @Override
    public int getNumber() {
        return pageable.getPageNumber();
    }

    @Override
    public int getSize() {
        return pageable.getPageSize();
    }

    @Override
    public int getNumberOfElements() {
        return 0;
    }

    @Override
    public List getContent() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }



    @Override
    public Sort getSort() {
        return null;
    }

    @Override
    public boolean isFirst() {
        return false;
    }

    @Override
    public boolean isLast() {
        return false;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public Pageable nextPageable() {
        return null;
    }

    @Override
    public Pageable previousPageable() {
        return null;
    }

    @NotNull
    @Override
    public Iterator iterator() {
        return null;
    }
}
