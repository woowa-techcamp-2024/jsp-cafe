package com.woowa.cafe.dto.article;

import com.woowa.cafe.domain.Article;
import com.woowa.cafe.domain.Member;

import java.util.ArrayList;
import java.util.List;

public record ArticleDto(Long articleId, String title, String contents, String updatedAt, String writerId,
                         String writerName) {

    public static List<ArticleDto> mapToList(final List<Article> articles, final List<Member> members) {
        List<ArticleDto> articleDtos = new ArrayList<>();

        for (Article article : articles) {
            for (Member member : members) {
                if (article.getWriterId().equals(member.getMemberId())) {
                    articleDtos.add(ArticleDto.of(article, member));
                }
            }
        }

        return articleDtos;
    }

    public static ArticleDto of(final Article article, final Member member) {
        return new ArticleDto(article.getId(), article.getTitle(), article.getContents(),
                article.getFormattedUpdatedAt(), member.getMemberId(), member.getName());
    }
}
