<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test="${not empty errorMessage}">
    <div id="error-message" class="error-message">
            ${errorMessage}
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
