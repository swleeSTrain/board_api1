package org.sunbong.board_api1.common.util.search;

import com.querydsl.core.BooleanBuilder;
import org.sunbong.board_api1.notice.domain.QNotice;

public enum SearchType {
    TITLE {
        public BooleanBuilder buildCondition(QNotice notice, String keyword) {
            return new BooleanBuilder(notice.title.containsIgnoreCase(keyword));
        }
    },
    WRITER {
        public BooleanBuilder buildCondition(QNotice notice, String keyword) {
            return new BooleanBuilder(notice.writer.containsIgnoreCase(keyword));
        }
    },
    CONTENT {
        public BooleanBuilder buildCondition(QNotice notice, String keyword) {
            return new BooleanBuilder(notice.content.containsIgnoreCase(keyword));
        }
    },
    TITLE_AND_WRITER {
        public BooleanBuilder buildCondition(QNotice notice, String keyword) {
            return new BooleanBuilder(notice.title.containsIgnoreCase(keyword)
                    .or(notice.writer.containsIgnoreCase(keyword)));
        }
    },
    TITLE_AND_CONTENT {
        public BooleanBuilder buildCondition(QNotice notice, String keyword) {
            return new BooleanBuilder(notice.title.containsIgnoreCase(keyword)
                    .or(notice.content.containsIgnoreCase(keyword)));
        }
    },
    WRITER_AND_CONTENT {
        public BooleanBuilder buildCondition(QNotice notice, String keyword) {
            return new BooleanBuilder(notice.writer.containsIgnoreCase(keyword)
                    .or(notice.content.containsIgnoreCase(keyword)));
        }
    },
    TITLE_WRITER_CONTENT {
        public BooleanBuilder buildCondition(QNotice notice, String keyword) {
            return new BooleanBuilder(notice.title.containsIgnoreCase(keyword)
                    .or(notice.writer.containsIgnoreCase(keyword))
                    .or(notice.content.containsIgnoreCase(keyword)));
        }
    };

    // 추상 메서드 정의
    public abstract BooleanBuilder buildCondition(QNotice notice, String keyword);

    // fromString 메서드 추가
    public static SearchType fromString(String type) {
        try {
            return SearchType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid search type: " + type);
        }
    }
}
