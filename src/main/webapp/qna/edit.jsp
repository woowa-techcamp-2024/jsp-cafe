<%@ page import="org.example.jspcafe.question.Question" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>

<%
    // request에서 "question" 객체를 가져옵니다.
    Question question = (Question) request.getAttribute("question");
    if (question == null) {
        out.println("질문을 찾을 수 없습니다.");
        return;
    }
    String questionId = String.valueOf(question.getId());
    String title = question.getTitle();
    String contents = question.getContents();
%>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <form id="edit-question-form" name="question" method="post">
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" class="form-control" id="title" name="title" placeholder="제목" value="<%= title %>"/>
                </div>
                <div class="form-group">
                    <label for="contents">내용</label>
                    <textarea name="contents" id="contents" rows="5" class="form-control"><%= contents %></textarea>
                </div>
                <button type="button" id="submit-edit" class="btn btn-success clearfix pull-right">수정하기</button>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp" %>

<script>
    document.getElementById('submit-edit').addEventListener('click', function() {
        const questionId = '<%= questionId %>'; // JSP에서 question ID를 받아옴
        const title = document.getElementById('title').value;
        const contents = document.getElementById('contents').value;

        const url = `/questions/`+questionId+`?title=`+encodeURIComponent(title)+`&contents=`+encodeURIComponent(contents);

        fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
            .then(response => {
                if (response.ok) {
                    // 성공 처리 (예: 페이지를 리로드하거나 사용자에게 알림)
                    alert('질문이 성공적으로 수정되었습니다.');
                    window.location.href = `/questions/`+questionId; // 수정 후 질문 페이지로 이동
                } else {
                    // 실패 처리 (예: 사용자에게 오류 메시지 표시)
                    alert('질문 수정에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('질문 수정 중 오류가 발생했습니다.');
            });
    });
</script>
