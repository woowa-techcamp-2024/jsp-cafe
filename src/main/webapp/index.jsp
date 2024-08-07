<%--
  Created by IntelliJ IDEA.
  User: KyungMin Lee
  Date: 24. 7. 23.
  Time: 오후 5:08
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE>
<html lang="ko">
<jsp:include page="/WEB-INF/jsp/component/headers.jsp"/>
<body>
<jsp:include page="/WEB-INF/jsp/component/navbar.jsp"/>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default qna-list">
            <ul class="list" id="questions-top">

            </ul>
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6 text-center">
                    <ul class="pagination center-block" style="display:inline-block;">

                    </ul>
                </div>
                <div class="col-md-3 qna-write">
                    <a href="${pageContext.request.contextPath}/questions"
                       class="btn btn-primary pull-right"
                       role="button">질문하기</a>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/component/scripts.jsp"/>
</body>
<script>
  $(document).ready(function () {
    let totalQuestions = 0;
    let currentPage = 1;
    let questionsPerPage = 15;

    initializePage();

    function initializePage() {
      fetchTotalQuestions();
    }

    function fetchTotalQuestions() {
      $.ajax({
        url: '/questions/list',
        type: 'GET',
        success: function (response) {
          totalQuestions = parseInt(response, 10);
          if (isNaN(totalQuestions)) {
            console.error('Invalid response format for total questions');
            return;
          }
          fetchQuestions(currentPage);
          updatePagination();
        },
        error: function (xhr, status, error) {
          console.error('Error fetching total questions:', error);
        }
      });
    }

    function fetchQuestions(page) {
      $.ajax({
        url: '/questions/list',
        type: 'GET',
        data: {page: page},
        success: function (response) {
          displayQuestions(response.data);
        },
        error: function (xhr, status, error) {
          console.error('Error fetching questions:', error);
        }
      });
    }

    // 질문 목록 표시
    function displayQuestions(questions) {
      let questionsList = $('#questions-top');
      questionsList.empty();

      $.each(questions, function (index, question) {
        applyQuestionToDOM(question)
      });
    }

    // 페이지네이션 업데이트
    function updatePagination() {
      let totalPages = Math.ceil(totalQuestions / questionsPerPage);
      let paginationElement = $('.pagination');
      paginationElement.empty();

      let pageGroup = Math.ceil(currentPage / 5);
      let lastPage = Math.min(pageGroup * 5, totalPages);
      let firstPage = Math.max(lastPage - 4, 1);

      // 이전 페이지 그룹으로 이동
      if (firstPage > 1) {
        paginationElement.append(createPageItem('«', firstPage - 1));
      }

      // 페이지 번호
      for (let i = firstPage; i <= lastPage; i++) {
        paginationElement.append(createPageItem(i, i, i === currentPage));
      }

      // 다음 페이지 그룹으로 이동
      if (lastPage < totalPages) {
        paginationElement.append(createPageItem('»', lastPage + 1));
      }
    }

    // 페이지 아이템 생성
    function createPageItem(text, page, isActive) {
      let li = $('<li>').addClass(isActive ? 'active' : '');
      let a = $('<a>').attr('href', '#').text(text);
      a.click(function (e) {
        e.preventDefault();
        goToPage(page);
      });
      return li.append(a);
    }

    // 페이지 이동
    function goToPage(page) {
      currentPage = page;
      fetchQuestions(currentPage);
      updatePagination();
    }

  })


</script>
</html>
