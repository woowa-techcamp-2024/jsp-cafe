<%@ page import="com.woowa.cafe.domain.Member" %>
<%@ page import="com.woowa.cafe.repository.user.MemberRepository" %>
<%@ page import="java.util.Optional" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<div class="navbar navbar-default" id="subnav">
    <div class="col-md-12">
        <div class="navbar-header">
            <a href="#" style="margin-left:15px;" class="navbar-btn btn btn-default btn-plus dropdown-toggle"
               data-toggle="dropdown"><i class="glyphicon glyphicon-home" style="color:#dd1111;"></i> Home <small><i
                    class="glyphicon glyphicon-chevron-down"></i></small></a>
            <ul class="nav dropdown-menu">
                <% MemberRepository memberRepository = (MemberRepository) application.getAttribute("memberRepository"); %>
                <% if (session.getAttribute("memberId") != null) { %>
                <% Optional<Member> member = memberRepository.findById((String) session.getAttribute("memberId"));
                    if (member.isPresent()) { %>
                <li><a href="<%="/user/" + member.get().getMemberId()%>"><i class=" glyphicon glyphicon-user"
                                                                            style="color:#1111dd;"></i>
                    Profile</a></li>
                <% }
                }%>
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
                <li class="active"><a href="/">Posts</a></li>
                <% if (session.getAttribute("memberId") == null || memberRepository.findById((String) session.getAttribute("memberId")).isEmpty()) { %>
                <li><a href="/user/login" role="button">로그인</a></li>
                <li><a href="/user" role="button">회원가입</a></li>
                <% } else { %>
                <li><a href="#" role="button">로그아웃</a></li>
                <li><a href="#" role="button">개인정보수정</a></li>
                <% } %>
            </ul>
        </div>
    </div>
</div>
