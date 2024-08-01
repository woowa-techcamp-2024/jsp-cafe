<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="/template/component/head.jsp"></jsp:include>
<body>
<div>
    <jsp:include page="/template/component/nav.jsp"></jsp:include>
</div>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <form name="question" method="post" action="${pageContext.request.contextPath}/questions">
                <input type="hidden" name="id" value="${article.id}" />
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" class="form-control" id="title" name="title" placeholder="제목" value="${article.title}"/>
                </div>
                <div class="form-group">
                    <label for="contents">내용</label>
                    <textarea name="contents" id="contents" rows="5" class="form-control">${article.contents}</textarea>
                </div>

                <c:choose>
                    <c:when test="${not empty article.id}">
                        <button type="button" onclick="sendPut()" class="btn btn-success clearfix pull-right">수정하기</button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" onclick="sendPost()" class="btn btn-success clearfix pull-right">질문하기</button>
                    </c:otherwise>
                </c:choose>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
</div>

<div>
    <jsp:include page="/template/component/script.jsp"></jsp:include>
</div>

<script>
    function sendPost() {
        var title = $("#title").val().trim();
        var contents = $("#contents").val().trim();

        // 유효성 검증
        if (title === '' || contents === '') {
            showToast("제목과 내용은 반드시 입력되야 합니다.");
            return;
        }
        if (title.length > 30) {
            showToast("제목은 30글자를 초과 할 수 없습니다.");
            return;
        }
        if (contents.length > 200) {
            showToast("내용은 200글자를 초과 할 수 없습니다.");
            return;
        }

        // POST 전송
        var data = {
            title: title,
            contents: contents
        };
        $.ajax({
            url: '${pageContext.request.contextPath}/questions',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(response) {
                console.log('POST Success:', response);
                window.location.href = '${pageContext.request.contextPath}';
            },
            error: function(xhr, status, error) {
                console.log('POST Error:', xhr);
                alert("게시글 작성에 실패했습니다.");
            }
        });
    }

    function sendPut() {
        var title = $("#title").val().trim();
        var contents = $("#contents").val().trim();

        // 유효성 검증
        if (title === '' || contents === '') {
            showToast("제목과 내용은 반드시 입력되야 합니다.");
            return;
        }
        if (title.length > 30) {
            showToast("제목은 30글자를 초과 할 수 없습니다.");
            return;
        }
        if (contents.length > 200) {
            showToast("내용은 200글자를 초과 할 수 없습니다.");
            return;
        }

        // PUT 전송
        var data = {
            id: $("input[name='id']").val(),
            title: title,
            contents: contents
        };

        $.ajax({
            url: '${pageContext.request.contextPath}/questions/${article.id}',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(response, textStatus, xhr) {
                location.href = '${pageContext.request.contextPath}/questions/${article.id}';
            },
            error: function(xhr, status, error) {
                console.log('PUT Error:', error);
                alert("게시글 수정에 실패했습니다.");
            }
        });
    }
</script>

</body>
</html>

