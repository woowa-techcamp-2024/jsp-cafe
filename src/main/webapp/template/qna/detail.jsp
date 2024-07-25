<%@ page import="woowa.cafe.dto.QuestionInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="/static/css/styles.css" rel="stylesheet">
</head>
<body>
<%@ include file="/template/common/header.jsp" %>
<div class="navbar navbar-default" id="subnav">
    <div class="col-md-12">
        <div class="navbar-header">
            <a href="#" style="margin-left:15px;" class="navbar-btn btn btn-default btn-plus dropdown-toggle"
               data-toggle="dropdown"><i class="glyphicon glyphicon-home" style="color:#dd1111;"></i> Home <small><i
                    class="glyphicon glyphicon-chevron-down"></i></small></a>
            <ul class="nav dropdown-menu">
                <li><a href="/static/user/profile.html"><i class="glyphicon glyphicon-user" style="color:#1111dd;"></i>
                    Profile</a></li>
                <li class="nav-divider"></li>
                <li><a href="#"><i class="glyphicon glyphicon-cog" style="color:#dd1111;"></i> Settings</a></li>
            </ul>

            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse2">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse2">
            <ul class="nav navbar-nav navbar-right">
                <li class="active"><a href="/static/index.html">Posts</a></li>
                <li><a href="/static/user/login.html" role="button">로그인</a></li>
                <li><a href="/static/user/form.html" role="button">회원가입</a></li>
                <li><a href="#" role="button">로그아웃</a></li>
                <li><a href="#" role="button">개인정보수정</a></li>
            </ul>
        </div>
    </div>
</div>

<% QuestionInfo question = (QuestionInfo) request.getAttribute("question"); %>
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title"><%=question.title()%>
                </h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture"
                                 class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="" class="article-author-name"><%=question.authorName()%></a>
                            <a href="" class="article-header-time" title="퍼머링크">
                                <%=question.getPostTime()%>
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>

                    <div class="article-doc">

                        <% for (String content : question.content().split("\n")) { %>
                        <p><%=content%>
                        </p>
                        <% } %>
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article" href="/questions/423/form">수정</a>
                            </li>
                            <li>
                                <form class="form-delete" action="/questions/423" method="POST">
                                    <input type="hidden" name="_method" value="DELETE">
                                    <button class="link-delete-article" type="submit">삭제</button>
                                </form>
                            </li>
                            <li>
                                <a class="link-modify-article" href="/index.html">목록</a>
                            </li>
                        </ul>
                    </div>
                </article>

                <div class="qna-comment">
                    <%--          <div class="qna-comment-slipp">--%>
                    <%--            <p class="qna-comment-count"><strong>2</strong>개의 의견</p>--%>
                    <%--            <div class="qna-comment-slipp-articles">--%>

                    <%--              <article class="article" id="answer-1405">--%>
                    <%--                <div class="article-header">--%>
                    <%--                  <div class="article-header-thumb">--%>
                    <%--                    <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">--%>
                    <%--                  </div>--%>
                    <%--                  <div class="article-header-text">--%>
                    <%--                    <a href="/users/1/자바지기" class="article-author-name">자바지기</a>--%>
                    <%--                    <a href="#answer-1434" class="article-header-time" title="퍼머링크">--%>
                    <%--                      2016-01-12 14:06--%>
                    <%--                    </a>--%>
                    <%--                  </div>--%>
                    <%--                </div>--%>
                    <%--                <div class="article-doc comment-doc">--%>
                    <%--                  <p>이 글만으로는 원인 파악하기 힘들겠다. 소스 코드와 설정을 단순화해서 공유해 주면 같이 디버깅해줄 수도 있겠다.</p>--%>
                    <%--                </div>--%>
                    <%--                <div class="article-util">--%>
                    <%--                  <ul class="article-util-list">--%>
                    <%--                    <li>--%>
                    <%--                      <a class="link-modify-article" href="/questions/413/answers/1405/form">수정</a>--%>
                    <%--                    </li>--%>
                    <%--                    <li>--%>
                    <%--                      <form class="delete-answer-form" action="/questions/413/answers/1405" method="POST">--%>
                    <%--                        <input type="hidden" name="_method" value="DELETE">--%>
                    <%--                        <button type="submit" class="delete-answer-button">삭제</button>--%>
                    <%--                      </form>--%>
                    <%--                    </li>--%>
                    <%--                  </ul>--%>
                    <%--                </div>--%>
                    <%--              </article>--%>
                    <%--              <article class="article" id="answer-1406">--%>
                    <%--                <div class="article-header">--%>
                    <%--                  <div class="article-header-thumb">--%>
                    <%--                    <img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">--%>
                    <%--                  </div>--%>
                    <%--                  <div class="article-header-text">--%>
                    <%--                    <a href="/users/1/자바지기" class="article-author-name">자바지기</a>--%>
                    <%--                    <a href="#answer-1434" class="article-header-time" title="퍼머링크">--%>
                    <%--                      2016-01-12 14:06--%>
                    <%--                    </a>--%>
                    <%--                  </div>--%>
                    <%--                </div>--%>
                    <%--                <div class="article-doc comment-doc">--%>
                    <%--                  <p>이 글만으로는 원인 파악하기 힘들겠다. 소스 코드와 설정을 단순화해서 공유해 주면 같이 디버깅해줄 수도 있겠다.</p>--%>
                    <%--                </div>--%>
                    <%--                <div class="article-util">--%>
                    <%--                  <ul class="article-util-list">--%>
                    <%--                    <li>--%>
                    <%--                      <a class="link-modify-article" href="/questions/413/answers/1405/form">수정</a>--%>
                    <%--                    </li>--%>
                    <%--                    <li>--%>
                    <%--                      <form class="form-delete" action="/questions/413/answers/1405" method="POST">--%>
                    <%--                        <input type="hidden" name="_method" value="DELETE">--%>
                    <%--                        <button type="submit" class="delete-answer-button">삭제</button>--%>
                    <%--                      </form>--%>
                    <%--                    </li>--%>
                    <%--                  </ul>--%>
                    <%--                </div>--%>
                    <%--              </article>--%>
                    <%--              <form class="submit-write">--%>
                    <%--                <div class="form-group" style="padding:14px;">--%>
                    <%--                  <textarea class="form-control" placeholder="Update your status"></textarea>--%>
                    <%--                </div>--%>
                    <%--                <button class="btn btn-success pull-right" type="button">답변하기</button>--%>
                    <%--                <div class="clearfix" />--%>
                    <%--              </form>--%>
                    <%--            </div>--%>
                    <%--          </div>--%>
                </div>
            </div>
        </div>
    </div>
</div>

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

<!-- script references -->
<script src="/static/js/jquery-2.2.0.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/scripts.js"></script>
</body>
</html>
