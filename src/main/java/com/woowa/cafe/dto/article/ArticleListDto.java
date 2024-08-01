package com.woowa.cafe.dto.article;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.domain.Member;

import java.util.ArrayList;
import java.util.List;

public record ArticleListDto(Long articleId, String title, String contents, String updatedAt, String writerId,
                             String writerName, Long replyCount) {

    public static List<ArticleListDto> mapToList(final List<Article> articles, final List<Member> members) {
        List<ArticleListDto> articleDtos = new ArrayList<>();

        for (Article article : articles) {
            for (Member member : members) {
                if (article.getWriterId().equals(member.getMemberId())) {
                    articleDtos.add(ArticleListDto.of(article, member));
                }
            }
        }

        return articleDtos;
    }

    public static ArticleListDto of(final Article article, final Member member) {
        return new ArticleListDto(article.getId(), article.getTitle(), article.getContents(),
                article.getFormattedUpdatedAt(), member.getMemberId(), member.getName(), article.getReplyCount());
    }
}
