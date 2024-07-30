<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../template/header.jsp"%>
<%@ include file="../template/nav.jsp"%>
<div class="container" id="main">
   <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
      <div class="panel panel-default content-main">
          <form id="question" name="question" method="post" action="${pageContext.request.contextPath}/questions/${question.id}/form">
              <div class="form-group">
                  <label for="title">제목</label>
                  <input type="text" class="form-control" id="title" name="title" placeholder="제목" value="${question.title}"/>
              </div>
              <div class="form-group">
                  <label for="content">내용</label>
                  <textarea name="content" id="content" rows="5" class="form-control">${question.content}</textarea>
              </div>
              <button type="button" class="btn btn-success clearfix pull-right" onclick="updateQuestion()">수정하기</button>
              <div class="clearfix" />
          </form>
        </div>
    </div>
</div>

<%@ include file="../template/footer.jsp"%>

<script>
    // form 찾아서 put 요청 날리는 함수
    const updateQuestion = () => {
        // form 데이터 직렬화
        var formData = $('#question').serializeArray().reduce(function(obj, item) {
            obj[item.name] = item.value;
            return obj;
        }, {});

        // jQuery ajax를 사용하여 PUT 요청 보내기
        $.ajax({
            url: '/questions/${question.id}/form',
            type: 'PUT',
            data: JSON.stringify(formData),
            success: function(response) {
                window.location.redirect('/');
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
                // 에러 처리 로직
            }
        });
    }

</script>
