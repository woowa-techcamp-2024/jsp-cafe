<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 세션에서 에러 메시지를 가져와서 request에 설정
    String sessionErrorMessage = (String) session.getAttribute("errorMessage");
    if (sessionErrorMessage != null) {
        request.setAttribute("sessionErrorMessage", sessionErrorMessage);
        session.removeAttribute("errorMessage"); // 에러 메시지를 한 번만 표시하도록 세션에서 제거
    }
%>

<c:if test="${not empty errorMessage || not empty sessionErrorMessage}">
    <div id="error-message" class="error-message">
        <c:if test="${not empty errorMessage}">
            ${errorMessage}
        </c:if>
        <c:if test="${not empty sessionErrorMessage}">
            ${sessionErrorMessage}
        </c:if>
    </div>
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            const errorMessage = document.getElementById('error-message');
            if (errorMessage) {
                errorMessage.style.display = 'block';
                setTimeout(() => {
                    errorMessage.style.display = 'none';
                }, 3000); // 3초 후에 사라짐
            }
        });
    </script>
</c:if>
