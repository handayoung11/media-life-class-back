package com.jin.miracle.miracle;

import com.jin.miracle.file.FileType;
import com.jin.miracle.miracle.dto.MiracleFeedResDTO;
import com.jin.miracle.user.entity.Users;
import com.jin.miracle.util.FileUtils;
import com.jin.miracle.util.UserUtil;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.jin.miracle.miracle.QMiracle.miracle;
import static com.jin.miracle.miracle_like.QMiracleLike.miracleLike;

@RequiredArgsConstructor
public class MiracleDslRepoImpl implements MiracleDslRepo {

    private final JPAQueryFactory queryFactory;
    private final UserUtil userUtil;

    @Transactional
    public MiracleFeedResDTO searchMiracles(Pageable pageable) {

        BooleanExpression likedExpr = likedExpr();
        List<Tuple> res = queryFactory
                .select(miracle,
                        miracleLike.countDistinct(),
                        likedExpr)
                .from(miracle)
                .join(miracle.user).fetchJoin()
                .leftJoin(miracle.likes, miracleLike)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .groupBy(miracle.id)
                .orderBy(miracle.miracleDate.desc(), miracle.id.desc())
                .fetch();

        boolean hasNext = false;
        if (res.size() > pageable.getPageSize()) {
            hasNext = true;
            res.remove(res.size() - 1);
        }

        List<MiracleFeedResDTO.FeedsDTO> feeds = res.stream().map(t -> {
            Miracle m = t.get(miracle);
            return MiracleFeedResDTO.FeedsDTO.builder()
                    .id(m.getId())
                    .author(m.getUser().getNickname())
                    .avatarUrl(FileUtils.getPath(FileType.USER_DIR, m.getUser().getProfile()))
                    .exerMin(m.getExerMin())
                    .studyMin(m.getStudyMin())
                    .mediaMin(m.getMediaMin())
                    .memo(m.getTodayMemo())
                    .likes(t.get(miracleLike.countDistinct()))
                    .liked(t.get(likedExpr))
                    .comments(new ArrayList<>())
                    .createdAt(m.getCreatedAt())
                    .build();
        }).toList();


        return MiracleFeedResDTO.builder()
                .feeds(feeds)
                .currentPage(pageable.getPageNumber())
                .hasNext(hasNext)
                .build();
    }

    private BooleanExpression likedExpr() {
        Users user = userUtil.getLogerOrNull();
        BooleanExpression likedExpr = (user != null ?
                JPAExpressions.selectOne()
                        .from(miracleLike)
                        .where(
                                miracleLike.miracle.eq(miracle),
                                miracleLike.user.eq(user)
                        )
                        .exists()
                : Expressions.asBoolean(false))
                .as("liked");
        return likedExpr;
    }
}
