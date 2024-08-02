<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@include file="/WEB-INF/share/header.jsp" %>
<body>
<%@include file="/WEB-INF/share/navbar.jsp" %>
<%@include file="/WEB-INF/share/sub_navbar.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title"><c:out value="${article.title}"/></h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture"
                                 class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/users/profile/<c:out value="${article.author.id}"/>" class="article-author-name">
                                <c:out value=" ${article.author.name}"/>
                            </a>
                            <a href="/todo" class="article-header-time" title="퍼머링크">
                                ${article.createdAt}
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc" style="margin-top: 28px;">
                        <c:out value="${article.contents}"/>
                    </div>
                    <c:if test="${article.isMine}">
                        <div class="article-util">
                            <ul class="article-util-list">
                                <li>
                                    <a class="link-modify-article"
                                       href="/qna/<c:out value="${article.id}"/>/form">수정</a>
                                </li>
                                <li>
                                    <form class="form-delete" action="/qna/<c:out value="${article.id}"/>"
                                          method="POST">
                                        <input type="hidden" name="_method" value="DELETE">
                                        <button class="link-delete-article" type="submit">삭제</button>
                                    </form>
                                </li>
                                <li>
                                    <a class="link-modify-article" href="/">목록</a>
                                </li>
                            </ul>
                        </div>
                    </c:if>
                </article>
                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <p class="qna-comment-count"><strong>0</strong>개의 의견</p>
                        <form class="submit-write answer-write" name="answer" method="POST" action="/qna/${article.id}/replies">
                            <div class="form-group" style="padding:14px;">
                                <textarea name="contents" id="contents" class="form-control" placeholder="답변을 작성해보세요."></textarea>
                            </div>
                            <button class="btn btn-success pull-right" type="submit">답변하기</button>
                            <div class="clearfix"></div>
                        </form>
                        <div class="qna-comment-slipp-articles">
                            <c:forEach var="reply" items="${replies}" varStatus="status">
                                <article class="article" id="answer-1405">
                                    <div class="article-header">
                                        <div class="article-header-thumb">
                                            <img src="https://graph.facebook.com/v2.3/1324855987/picture"
                                                 class="article-author-thumb" alt="">
                                        </div>
                                        <div class="article-header-text">
                                            <a href="/users/profile/${reply.author.id}"
                                               class="article-author-name">${reply.author.name}</a>
                                            <a href="#answer-1434" class="article-header-time" title="퍼머링크">
                                                    ${reply.createdAt}
                                            </a>
                                        </div>
                                    </div>
                                    <div class="article-doc comment-doc">
                                        <p><c:out value="${reply.contents}"/></p>
                                    </div>
                                    <c:if test="${reply.isMine}">
                                        <div class="article-util">
                                            <ul class="article-util-list">
                                                <li>
                                                    <a class="link-modify-article"
                                                       href="/questions/413/answers/1405/form">수정</a>
                                                </li>
                                                <li>
                                                    <form class="delete-answer-form"
                                                          action="/qna/${article.id}/replies/${reply.id}"
                                                          method="POST">
                                                        <input type="hidden" name="_method" value="DELETE">
                                                        <button type="submit" class="delete-answer-button">삭제</button>
                                                    </form>
                                                </li>
                                            </ul>
                                        </div>
                                    </c:if>
                                </article>
                            </c:forEach>
                            <%--                            <article class="article" id="answer-1406">--%>
                            <%--                                <div class="article-header">--%>
                            <%--                                    <div class="article-header-thumb">--%>
                            <%--                                        <img src="https://graph.facebook.com/v2.3/1324855987/picture"--%>
                            <%--                                             class="article-author-thumb" alt="">--%>
                            <%--                                    </div>--%>
                            <%--                                    <div class="article-header-text">--%>
                            <%--                                        <a href="/users/1/자바지기" class="article-author-name">자바지기</a>--%>
                            <%--                                        <a href="#answer-1434" class="article-header-time" title="퍼머링크">--%>
                            <%--                                            2016-01-12 14:06--%>
                            <%--                                        </a>--%>
                            <%--                                    </div>--%>
                            <%--                                </div>--%>
                            <%--                                <div class="article-doc comment-doc">--%>
                            <%--                                    <p>이 글만으로는 원인 파악하기 힘들겠다. 소스 코드와 설정을 단순화해서 공유해 주면 같이 디버깅해줄 수도 있겠다.</p>--%>
                            <%--                                </div>--%>
                            <%--                                <div class="article-util">--%>
                            <%--                                    <ul class="article-util-list">--%>
                            <%--                                        <li>--%>
                            <%--                                            <a class="link-modify-article"--%>
                            <%--                                               href="/questions/413/answers/1405/form">수정</a>--%>
                            <%--                                        </li>--%>
                            <%--                                        <li>--%>
                            <%--                                            <form class="form-delete" action="/questions/413/answers/1405"--%>
                            <%--                                                  method="POST">--%>
                            <%--                                                <input type="hidden" name="_method" value="DELETE">--%>
                            <%--                                                <button type="submit" class="delete-answer-button">삭제</button>--%>
                            <%--                                            </form>--%>
                            <%--                                        </li>--%>
                            <%--                                    </ul>--%>
                            <%--                                </div>--%>
                            <%--                            </article>--%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/share/footer.jsp" %>


<script type="text/template" id="answerTemplate">
    <article id="reply-{0}" class="article">
        <div class="article-header">
            <div class="article-header-thumb">
                <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
            </div>
            <div class="article-header-text">
                <a href="#" class="article-author-name">{1}</a>
                <div class="article-header-time">{2}</div>
            </div>
        </div>
        <div class="article-doc comment-doc">
            {3}
        </div>
        <div class="article-util">
            <ul class="article-util-list">
                <li>
                    <a class="link-modify-article" href="/api/qna/updateAnswer/${article.id}">수정</a>
                </li>
                <li>
                    <form class="delete-answer-form" action="/api/questions/${article.id}/answers/{4}" method="POST">
                        <input type="hidden" name="_method" value="DELETE">
                        <button type="submit" class="delete-answer-button">삭제</button>
                    </form>
                </li>
            </ul>
        </div>
    </article>
</script>

<script>
    $(".submit-write button[type=submit]").click(addAnswer);

    function addAnswer(e) {
        e.preventDefault();

        var url = $(".answer-write").attr("action");

        var contents = $("#contents").val();
        var data = JSON.stringify({ contents: contents });

        console.log(data)
        $.ajax({
            type : 'POST',
            url : url,
            data : data,
            contentType: 'application/json; charset=utf-8',
            dataType : 'json',
            error: function () {
                alert("error");
            },
            success : function (data, status) {
                var answerTemplate = $("#answerTemplate").html();
                var template = answerTemplate.format(data.id, data.author.userId, data.createdAt, data.contents, data.id);
                console.log(template)
                $(".qna-comment-slipp-articles").append(template);
                $("textarea[name=contents]").val("");

                var tagId = "reply-" + data.id;
                window.location.hash = tagId;

                $("#" + tagId).css("background-color", "yellow");
                setTimeout(function () {
                    $("#" + tagId).css("background-color", "");
                }, 2000); // 2초 후 원래 배경색으로 돌아옴
                // 해당 댓글로 이동
            }
        });

    }

    $(".delete-answer-form button[type='submit']").click(deleteAnswer);

</script>

</body>
</html>