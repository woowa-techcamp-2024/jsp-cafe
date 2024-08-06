<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="<c:url value='/static/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/static/css/styles.css'/>" rel="stylesheet">
</head>
<body>
<%@ include file="../Header.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title">${post.title}</h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture"
                                 class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/users/${post.userId}" class="article-author-name">${post.username}</a>
                            <a href="/questions/${post.id}" class="article-header-time" title="퍼머링크">
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc">
                        ${post.contents}
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <c:if test="${isAuthor}">
                                    <a class="link-modify-article" href="/questions/${post.id}/form">수정</a>
                                </c:if>
                            </li>
                            <li>
                                <c:if test="${isAuthor}">
                                    <button class="link-delete-article" onclick="deletePost(${post.id})">삭제</button>
                                </c:if>
                            </li>
                            <li>
                                <a class="link-modify-article" href="/">목록</a>
                            </li>
                        </ul>
                    </div>
                </article>

                <!-- 댓글 섹션 -->
                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <div class="qna-comment-slipp-articles">
                            <ul id="replyList">
                                <!-- 댓글 목록이 여기에 동적으로 추가됩니다 -->
                            </ul>
                        </div>
                        <form class="submit-write">
                            <div class="form-group" style="padding:14px;">
                                <textarea class="form-control" id="replyContents" placeholder="댓글을 입력하세요"
                                          rows="4"></textarea>
                            </div>
                            <button class="btn btn-success pull-right" type="button" onclick="addReply(${post.id})">댓글
                                쓰기
                            </button>
                            <div class="clearfix"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function deletePost(postId) {
        if (confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
            var xhr = new XMLHttpRequest();
            xhr.open('DELETE', '/questions/' + postId, true);
            xhr.onload = function() {
                if (xhr.status === 200) {
                    var redirectUrl = xhr.getResponseHeader('X-Redirect-Location');
                    if (redirectUrl) {
                        alert(xhr.responseText);
                        window.location.href = redirectUrl;
                    }
                } else {
                    alert('게시글 삭제에 실패했습니다: ' + xhr.responseText);
                }
            };
            xhr.onerror = function() {
                alert('네트워크 오류가 발생했습니다.');
            };
            xhr.send();
        }
    }
</script>
<script>
    var currentPage = 1;
    var isLoading = false;
    // postId를 전역 변수로 선언
    var postId;
    var PAGE_SIZE = 5;

    window.onload = function () {
        // postId 값을 설정
        postId = ${post.id}
        console.log('Post ID:', postId); // postId 값 확인
        loadReplies(currentPage);
    }

    function loadReplies(page) {
        if (isLoading) return;
        isLoading = true;

        var url = '/reply/' + postId + '?page=' + page;
        console.log('Loading replies from: ' + url);

        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.onload = function() {
            isLoading = false;
            if (xhr.status === 200) {
                console.log('Received response for page ' + page);
                var replyList = document.getElementById('replyList');

                // 더보기 버튼 제거
                removeLoadMoreButton();

                // 새로운 댓글들 추가
                var newContent = xhr.responseText;
                var tempDiv = document.createElement('div');
                tempDiv.innerHTML = newContent;
                var newReplies = tempDiv.querySelectorAll('li');

                if (page === 1) {
                    replyList.innerHTML = '';
                }

                newReplies.forEach(function(reply) {
                    replyList.appendChild(reply);
                });

                // 새로 받은 댓글 수가 PAGE_SIZE와 같으면 "더 보기" 버튼 추가
                if (newReplies.length === PAGE_SIZE) {
                    addLoadMoreButton(page + 1);
                }

                currentPage = page;
                console.log('Current page updated to ' + currentPage);
            } else {
                console.error('댓글을 불러오는데 실패했습니다.');
            }
        };
        xhr.onerror = function() {
            isLoading = false;
            console.error('네트워크 오류가 발생했습니다.');
        };
        xhr.send();
    }

    function removeLoadMoreButton() {
        var existingLoadMoreBtn = document.getElementById('load-more-btn');
        if (existingLoadMoreBtn) {
            existingLoadMoreBtn.remove();
        }
    }

    function addLoadMoreButton(nextPage) {
        var loadMoreBtn = document.createElement('button');
        loadMoreBtn.id = 'load-more-btn';
        loadMoreBtn.textContent = '더 보기';
        loadMoreBtn.onclick = function() {
            loadReplies(nextPage);
        };
        document.getElementById('replyList').after(loadMoreBtn);
    }

    function addReply() {
        var contents = document.getElementById('replyContents').value;
        if (!contents.trim()) {
            alert('댓글 내용을 입력해주세요.');
            return;
        }

        var formData = new URLSearchParams();
        formData.append('contents', contents);

        fetch('/reply/' + postId, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData.toString()
        })
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('Server responded with ' + response.status);
                }
                return response.text();
            })
            .then(function(html) {
                document.getElementById('replyContents').value = '';

                // 새 댓글 추가 후 첫 페이지부터 다시 로드
                currentPage = 1;
                loadReplies(currentPage);
            })
            .catch(function(error) {
                console.error('Error:', error);
                alert('댓글 추가에 실패했습니다: ' + error.message);
            });
    }

    function editReply(replyId) {
        var newContents = prompt("수정할 내용을 입력하세요:");
        if (newContents) {
            var xhr = new XMLHttpRequest();
            xhr.open('PUT', '/reply/' + replyId, true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onload = function () {
                if (xhr.status === 200) {
                    document.querySelector('#reply-' + replyId + ' p').textContent = xhr.responseText;
                } else {
                    alert('댓글 수정에 실패했습니다');
                }
            };
            xhr.onerror = function () {
                alert('네트워크 오류가 발생했습니다.');
            };
            xhr.send('contents=' + encodeURIComponent(newContents));
        }
    }

    function deleteReply(replyId) {
        if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
            var xhr = new XMLHttpRequest();
            xhr.open('DELETE', '/reply/' + replyId, true);
            xhr.onload = function () {
                if (xhr.status === 200) {
                    var replyElement = document.getElementById('reply-' + replyId);
                    replyElement.parentNode.removeChild(replyElement);
                } else {
                    alert('댓글 삭제에 실패했습니다: ' + xhr.responseText);
                }
            };
            xhr.onerror = function () {
                alert('네트워크 오류가 발생했습니다.');
            };
            xhr.send();
        }
    }
</script>
</script>
<script src="<c:url value='/js/jquery-2.2.0.min.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/js/scripts.js'/>"></script>
</body>
</html>