<%@ page import="codesquad.jspcafe.common.DefaultHTMLData" %><%--
  Created by IntelliJ IDEA.
  User: KyungMin Lee
  Date: 24. 7. 23.
  Time: 오전 11:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE>
<html lang="ko">
<head>
    <%=DefaultHTMLData.getHtmlHead()%>
</head>
<body>
<%=DefaultHTMLData.getNaviBar()%>
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list">
                <li>
                    <div class="wrap">
                        <div class="main">
                            <strong class="subject">
                                <a href="./qna/show.html">국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는
                                    뭘까?</a>
                            </strong>
                            <div class="auth-info">
                                <i class="icon-add-comment"></i>
                                <span class="time">2016-01-15 18:47</span>
                                <a href="./user/profile.html" class="author">자바지기</a>
                            </div>
                            <div class="reply" title="댓글">
                                <i class="icon-reply"></i>
                                <span class="point">8</span>
                            </div>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="wrap">
                        <div class="main">
                            <strong class="subject">
                                <a href="./qna/show.html">runtime 에 reflect 발동 주체 객체가 뭔지 알 방법이
                                    있을까요?</a>
                            </strong>
                            <div class="auth-info">
                                <i class="icon-add-comment"></i>
                                <span class="time">2016-01-05 18:47</span>
                                <a href="./user/profile.html" class="author">김문수</a>
                            </div>
                            <div class="reply" title="댓글">
                                <i class="icon-reply"></i>
                                <span class="point">12</span>
                            </div>
                        </div>
                    </div>
                </li>
            </ul>
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6 text-center">
                    <ul class="pagination center-block" style="display:inline-block;">
                        <li><a href="#">«</a></li>
                        <li><a href="#">1</a></li>
                        <li><a href="#">2</a></li>
                        <li><a href="#">3</a></li>
                        <li><a href="#">4</a></li>
                        <li><a href="#">5</a></li>
                        <li><a href="#">»</a></li>
                    </ul>
                </div>
                <div class="col-md-3 qna-write">
                    <a href="${pageContext.request.contextPath}/qna/form.html"
                       class="btn btn-primary pull-right"
                       role="button">질문하기</a>
                </div>
            </div>
        </div>
    </div>
</div>
<%=DefaultHTMLData.getScripts()%>
</body>
</html>
