<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-8">
        <h2 class="mt-5">Post List</h2>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Title</th>
                <th>Author</th>
                <th>Date</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="article" items="${articleList}">
                <tr onclick="location.href='${pageContext.request.contextPath}/questions/${article.id}'"
                    style="cursor: pointer;">
                    <td><c:out value="${article.title}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${article.userId == 0}">
                                Unknown
                            </c:when>
                            <c:otherwise>
                                <c:out value="${article.userId}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><c:out value="${article.createdDate}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>
