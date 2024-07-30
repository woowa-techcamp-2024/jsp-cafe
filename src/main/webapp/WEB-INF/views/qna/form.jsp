<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<jsp:include page="../snippet/meta_header.jsp"/>
<body>
<jsp:include page="../snippet/navigation.jsp"/>
<jsp:include page="../snippet/header.jsp"/>

<c:set var="signInUser" value="${sessionScope.signInUser}"/>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <form name="question" method="post" action="${pageContext.request.contextPath}/questions">
                <input class="form-control" id="writer" name="writer" type="hidden" value="${signInUser.name}"
                       readonly/>
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" class="form-control" id="title" name="title" placeholder="제목"/>
                </div>
                <div class="form-group">
                    <label for="contents">내용</label>
                    <textarea name="contents" id="contents" rows="5" class="form-control"></textarea>
                </div>
                <input class="form-control" id="writerId" name="writerId" value="${signInUser.id}" type="hidden"/>
                <button type="submit" class="btn btn-success clearfix pull-right">질문하기</button>
                <div class="clearfix"/>
            </form>
        </div>
    </div>
</div>

<!-- script references -->
<jsp:include page="../snippet/script.jsp"/>
<script>
    $(document).ready(function () {
        $('#question').on('submit', function (e) {
            $.ajax({
                error: function (xhr, status, error) {
                    let errorMessage = xhr.responseText
                    alert(status + ": " + errorMessage)
                    window.location.href = '/questions/' + ${editedQuestion.id};
                }
            });
        })
    });
</script>
</body>
</html>
