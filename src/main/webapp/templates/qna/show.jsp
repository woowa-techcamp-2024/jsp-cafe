<%@ page import="com.hyeonuk.jspcafe.article.domain.Article" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../component/header.jsp"%>
<!DOCTYPE html>
<html lang="kr">
<body>
<div class="container" id="main">
    <%
        Article article = (Article)request.getAttribute("article");
        Member member = (Member)session.getAttribute("member");
    %>
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
          <header class="qna-header">
              <h2 class="qna-title"><%=article.getTitle()%></h2>
          </header>
          <div class="content-main">
              <article class="article">
                  <div class="article-header">
                      <div class="article-header-thumb">
                          <img src="https://graph.facebook.com/v2.3/100000059371774/picture" class="article-author-thumb" alt="">
                      </div>
                      <div class="article-header-text">
                          <a href="${pageContext.request.contextPath}/members/<%=article.getWriter().getMemberId()%>" class="article-author-name"><%=article.getWriter().getNickname()%></a>
                          <a href="/questions/413" class="article-header-time" title="퍼머링크">
                              2015-12-30 01:47
                              <i class="icon-link"></i>
                          </a>
                      </div>
                  </div>
                  <div class="article-doc">
                      <p><%=article.getContents()%></p>
                  </div>
                  <div class="article-util">
                      <ul class="article-util-list">
                          <%
                              if(article.getWriter().getId().equals(member.getId())){
                          %>
                          <li>
                              <a class="link-modify-article" href="/questions/<%=article.getId()%>/form">수정</a>
                          </li>
                          <li>
                              <form class="form-delete" action="/questions/<%=article.getId()%>" method="POST">
                                  <input type="hidden" name="_method" value="DELETE">
                                  <button class="link-delete-article" type="submit">삭제</button>
                              </form>
                          </li>
                         <%
                             }
                         %>
                          <li>
                              <a class="link-modify-article" href="/">목록</a>
                          </li>
                      </ul>
                  </div>
              </article>

              <div class="qna-comment">
                  <div class="qna-comment-slipp">
                      <div id="comment-box" class="qna-comment-slipp-articles">
<%--                          <article class="article" id="answer-1405">--%>
<%--                              <div class="article-header">--%>
<%--                                  <div class="article-header-thumb">--%>
<%--                                      <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">--%>
<%--                                  </div>--%>
<%--                                  <div class="article-header-text">--%>
<%--                                      <a href="/users/1/자바지기" class="article-author-name">자바지기</a>--%>
<%--                                      <a href="#answer-1434" class="article-header-time" title="퍼머링크">--%>
<%--                                          2016-01-12 14:06--%>
<%--                                      </a>--%>
<%--                                  </div>--%>
<%--                              </div>--%>
<%--                              <div class="article-doc comment-doc">--%>
<%--                                  <p>이 글만으로는 원인 파악하기 힘들겠다. 소스 코드와 설정을 단순화해서 공유해 주면 같이 디버깅해줄 수도 있겠다.</p>--%>
<%--                              </div>--%>
<%--                              <div class="article-util">--%>
<%--                                  <ul class="article-util-list">--%>
<%--                                      <li>--%>
<%--                                          <a class="link-modify-article" href="/questions/413/answers/1405/form">수정</a>--%>
<%--                                      </li>--%>
<%--                                      <li>--%>
<%--                                          <form class="delete-answer-form" action="/questions/413/answers/1405" method="POST">--%>
<%--                                              <input type="hidden" name="_method" value="DELETE">--%>
<%--                                              <button type="submit" class="delete-answer-button">삭제</button>--%>
<%--                                          </form>--%>
<%--                                      </li>--%>
<%--                                  </ul>--%>
<%--                              </div>--%>
<%--                          </article>--%>
<%--                          <article class="article" id="answer-1406">--%>
<%--                              <div class="article-header">--%>
<%--                                  <div class="article-header-thumb">--%>
<%--                                      <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">--%>
<%--                                  </div>--%>
<%--                                  <div class="article-header-text">--%>
<%--                                      <a href="/users/1/자바지기" class="article-author-name">자바지기</a>--%>
<%--                                      <a href="#answer-1434" class="article-header-time" title="퍼머링크">--%>
<%--                                          2016-01-12 14:06--%>
<%--                                      </a>--%>
<%--                                  </div>--%>
<%--                              </div>--%>
<%--                              <div class="article-doc comment-doc">--%>
<%--                                  <p>이 글만으로는 원인 파악하기 힘들겠다. 소스 코드와 설정을 단순화해서 공유해 주면 같이 디버깅해줄 수도 있겠다.</p>--%>
<%--                              </div>--%>
<%--                              <div class="article-util">--%>
<%--                                  <ul class="article-util-list">--%>
<%--                                      <li>--%>
<%--                                          <a class="link-modify-article" href="/questions/413/answers/1405/form">수정</a>--%>
<%--                                      </li>--%>
<%--                                      <li>--%>
<%--                                          <form class="form-delete" action="/questions/413/answers/1405" method="POST">--%>
<%--                                              <input type="hidden" name="_method" value="DELETE">--%>
<%--                                              <button type="submit" class="delete-answer-button">삭제</button>--%>
<%--                                          </form>--%>
<%--                                      </li>--%>
<%--                                  </ul>--%>
<%--                              </div>--%>
<%--                          </article>--%>
                          <form class="submit-write">
                              <div class="form-group" style="padding:14px;">
                                  <textarea class="form-control" placeholder="Update your status" id="replyContents"></textarea>
                              </div>
                              <button class="btn btn-success pull-right" id="replyPostBtn" type="button">답변하기</button>
                              <div class="clearfix" />
                          </form>
                      </div>
                  </div>
              </div>
          </div>
        </div>
    </div>
</div>
<script>
    get(`/replies/<%=article.getId()%>`)
        .then(replies => {
            replies.forEach(reply=>appendReplyDocument(reply));
        })
        .catch(error=>{
            console.error("error : ",error);
        });

    document.querySelector("#replyPostBtn").addEventListener("click",(e)=>{
        const value = document.querySelector("#replyContents").value;
        post("/replies",{"contents":value,"articleId":<%=article.getId()%>})
            .then(reply =>{
                document.querySelector('#replyContents').value = "";
                appendReplyDocument(reply);
            })
            .catch(exception =>{
                console.error(exception);
                alert(exception);
            })
    })

    function appendReplyDocument(reply) {

        const element = createArticle(reply);
        const delBtn = element.querySelector('#delBtn');
        delBtn.addEventListener("click",(e)=>{
           del(`/replies/\${reply.replyId}`)
               .then(response => {
                   document.querySelector('#comment-box').removeChild(element);
               })
               .catch(exception=>{
                   console.log(exception);
                   alert("자신의 댓글이 아닙니다.");
               })

        });
        document.querySelector('#comment-box').appendChild(element);
    }
    function createArticle(reply) {
        // article 요소 생성 및 클래스, ID 설정
        const article = document.createElement('article');
        article.className = 'article';
        article.id = `answer-\${reply.id}`; // reply 객체에 id가 있다고 가정

        // article-header 생성
        const articleHeader = document.createElement('div');
        articleHeader.className = 'article-header';

        // article-header-thumb 생성 및 img 추가
        const articleHeaderThumb = document.createElement('div');
        articleHeaderThumb.className = 'article-header-thumb';
        const authorThumb = document.createElement('img');
        authorThumb.className = 'article-author-thumb';
        authorThumb.src = 'https://graph.facebook.com/v2.3/1324855987/picture'; // 여기에 실제 URL 사용
        authorThumb.alt = '';
        articleHeaderThumb.appendChild(authorThumb);

        // article-header-text 생성 및 내용 추가
        const articleHeaderText = document.createElement('div');
        articleHeaderText.className = 'article-header-text';
        const authorLink = document.createElement('a');
        authorLink.href = `/members/\${reply.memberId}`;
        authorLink.className = 'article-author-name';
        authorLink.textContent = reply.memberNickname;
        const timeLink = document.createElement('a');
        timeLink.href = `#answer-\${reply.id}`;
        timeLink.className = 'article-header-time';
        timeLink.title = '퍼머링크';
        timeLink.textContent = '2016-01-12 14:06'; // 여기서도 실제 시간을 사용
        articleHeaderText.appendChild(authorLink);
        articleHeaderText.appendChild(timeLink);

        // article-header에 요소 추가
        articleHeader.appendChild(articleHeaderThumb);
        articleHeader.appendChild(articleHeaderText);

        // article-doc 생성 및 내용 추가
        const articleDoc = document.createElement('div');
        articleDoc.className = 'article-doc comment-doc';
        const p = document.createElement('p');
        p.textContent = reply.contents;
        articleDoc.appendChild(p);

        // article-util 생성
        const articleUtil = document.createElement('div');
        articleUtil.className = 'article-util';
        const utilList = document.createElement('ul');
        utilList.className = 'article-util-list';

        // 수정 링크
        const modifyItem = document.createElement('li');
        const modifyLink = document.createElement('a');
        modifyLink.className = 'link-modify-article';
        modifyLink.href = `/questions/413/answers/\${reply.id}/form`;
        modifyLink.textContent = '수정';
        modifyItem.appendChild(modifyLink);

        // 삭제 버튼
        const deleteItem = document.createElement('li');
        const deleteButton = document.createElement('input');
        deleteButton.type = 'button';
        deleteButton.id = 'delBtn';
        deleteButton.className = 'delete-answer-button';
        deleteButton.value = '삭제';
        deleteItem.appendChild(deleteButton);

        // utilList에 수정 및 삭제 추가
        utilList.appendChild(modifyItem);
        utilList.appendChild(deleteItem);

        // articleUtil에 utilList 추가
        articleUtil.appendChild(utilList);

        // article에 모든 요소 추가
        article.appendChild(articleHeader);
        article.appendChild(articleDoc);
        article.appendChild(articleUtil);

        return article;
    }
</script>
<script type="text/template" id="answerTemplate">
    <article class="article">
        <div class="article-header">
            <div class="article-header-thumb">
                <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">
            </div>
            <div class="article-header-text">
                <a href="#" class="article-author-name">{0}</a>
                <div class="article-header-time">{1}</div>
            </div>
        </div>
        <div class="article-doc comment-doc">
            {2}
        </div>
        <div class="article-util">
            <ul class="article-util-list">
                <li>
                    <a class="link-modify-article" href="/api/qna/updateAnswer/{3}">수정</a>
                </li>
                <li>
                    <form class="delete-answer-form" action="/api/questions/{3}/answers/{4}" method="POST">
                        <input type="hidden" name="_method" value="DELETE">
                        <button type="submit" class="delete-answer-button">삭제</button>
                    </form>
                </li>
            </ul>
        </div>
    </article>
</script>
</body>
</html>