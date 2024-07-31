<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 상세</title>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen">

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="container mx-auto mt-8 px-4">
    <div class="bg-white shadow rounded-lg p-6 mb-8">
        <h2 class="text-2xl font-bold mb-4">${article.title()}</h2>
        <div class="mb-4 text-sm text-gray-600">
            <span>작성자: ${article.nickname()}</span> |
            <span>작성일: ${fn:substring(article.createAt(), 0, 4)}년 ${fn:substring(article.createAt(), 5, 7)}월 ${fn:substring(article.createAt(), 8, 10)}일</span>
        </div>
        <div class="border-t border-b py-4 mb-4">
            <p>${article.content()}</p>
        </div>
        <div class="flex justify-end space-x-2">
            <a href="<c:url value="/articles/${article.id()}/form"/>"
               class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded">수정</a>
            <button id="deleteButton" class="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded">삭제
            </button>
            <a href="/" class="bg-gray-500 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded">목록</a>
        </div>
    </div>

    <!-- 댓글 섹션 -->
    <div class="bg-white shadow rounded-lg p-6">
        <h3 class="text-xl font-bold mb-4">댓글</h3>

        <!-- 댓글 목록 -->
        <div id="replyList" class="space-y-4 mb-6">
            <!-- 댓글이 여기에 동적으로 추가됩니다 -->
        </div>

        <!-- 댓글 작성 폼 -->
        <form id="replyForm" class="space-y-4">
            <div>
                <label for="replyContent" class="block text-sm font-medium text-gray-700">댓글 내용</label>
                <textarea id="replyContent" name="content" rows="3"
                          class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"></textarea>
            </div>
            <div>
                <button type="submit" class="bg-indigo-500 hover:bg-indigo-600 text-white font-bold py-2 px-4 rounded">
                    댓글 작성
                </button>
            </div>
        </form>
    </div>
</main>

<script>
    // 게시글 삭제 기능
    document.getElementById('deleteButton').addEventListener('click', async function (e) {
        e.preventDefault();

        if (confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
            try {
                const response = await axios.delete('<c:url value="/articles/${article.id()}"/>');
                alert('게시글이 성공적으로 삭제되었습니다.');
                window.location.href = '/';  // 메인 페이지로 리다이렉트
            } catch (error) {
                console.error('Error:', error);
                if (error.response) {
                    const errorCode = error.response.status;
                    window.location.href = '/error/' + errorCode;
                }
            }
        }
    });
</script>

<script>
    function formatDate(dateArray) {
        const [year, month, day, hour, minute, second] = dateArray;
        const date = new Date(year, month - 1, day, hour, minute, second);
        const options = {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            hour12: false
        };
        return date.toLocaleString('ko-KR', options);
    }

    function createReplyElement(reply) {
        const replyDiv = document.createElement('div');
        replyDiv.className = 'bg-gray-50 rounded-lg p-4 mb-4';
        replyDiv.id = 'reply-' + reply.id;

        const headerDiv = document.createElement('div');
        headerDiv.className = 'flex justify-between items-center mb-2';

        const nicknameP = document.createElement('p');
        nicknameP.className = 'font-semibold';
        nicknameP.textContent = reply.nickname;

        const dateP = document.createElement('p');
        dateP.className = 'text-sm text-gray-500';
        dateP.textContent = formatDate(reply.createAt);

        headerDiv.appendChild(nicknameP);
        headerDiv.appendChild(dateP);

        const contentP = document.createElement('p');
        contentP.className = 'mb-2 reply-content';
        contentP.textContent = reply.content;

        const buttonDiv = document.createElement('div');
        buttonDiv.className = 'flex justify-end space-x-2';

        const editButton = document.createElement('button');
        editButton.className = 'text-blue-500 hover:text-blue-700';
        editButton.textContent = '수정';
        editButton.onclick = function() { editReply(reply.id); };

        const deleteButton = document.createElement('button');
        deleteButton.className = 'text-red-500 hover:text-red-700';
        deleteButton.textContent = '삭제';
        deleteButton.onclick = function() { deleteReply(reply.id); };

        buttonDiv.appendChild(editButton);
        buttonDiv.appendChild(deleteButton);

        replyDiv.appendChild(headerDiv);
        replyDiv.appendChild(contentP);
        replyDiv.appendChild(buttonDiv);

        return replyDiv;
    }

    async function loadReplies() {
        try {
            const response = await axios.get('<c:url value="/reply"/>', {
                params: { articleId: '<c:out value="${article.id()}"/>' }
            });
            const replyList = document.getElementById('replyList');
            replyList.innerHTML = '';
            response.data.forEach(reply => {
                replyList.appendChild(createReplyElement(reply));
            });
        } catch (error) {
            console.error('댓글을 불러오는 중 오류가 발생했습니다:', error);
            if (error.response) {
                const errorCode = error.response.status;
                window.location.href = '/error/' + errorCode;
            }
        }
    }

    document.getElementById('replyForm').addEventListener('submit', async function(e) {
        e.preventDefault();
        const content = document.getElementById('replyContent').value;
        try {
            await axios.post('<c:url value="/reply"/>', {
                articleId: '<c:out value="${article.id()}"/>',
                content: content
            });
            document.getElementById('replyContent').value = '';
            loadReplies();
        } catch (error) {
            console.error('댓글 작성 중 오류가 발생했습니다:', error);
        }
    });

    function editReply(replyId) {
        const replyDiv = document.getElementById('reply-' + replyId);
        const contentP = replyDiv.querySelector('.reply-content');
        const currentContent = contentP.textContent;

        const textarea = document.createElement('textarea');
        textarea.className = 'mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50';
        textarea.value = currentContent;

        const saveButton = document.createElement('button');
        saveButton.className = 'bg-green-500 hover:bg-green-600 text-white font-bold py-1 px-2 rounded mr-2';
        saveButton.textContent = '저장';

        const cancelButton = document.createElement('button');
        cancelButton.className = 'bg-gray-500 hover:bg-gray-600 text-white font-bold py-1 px-2 rounded';
        cancelButton.textContent = '취소';

        const buttonDiv = replyDiv.querySelector('div:last-child');
        buttonDiv.innerHTML = '';
        buttonDiv.appendChild(saveButton);
        buttonDiv.appendChild(cancelButton);

        contentP.replaceWith(textarea);

        saveButton.onclick = async function() {
            try {
                await axios.put('<c:url value="/reply"/>', {
                    id: replyId,
                    content: textarea.value
                });
                loadReplies();
            } catch (error) {
                console.error('댓글 수정 중 오류가 발생했습니다:', error);
                if (error.response) {
                    const errorCode = error.response.status;
                    window.location.href = '/error/' + errorCode;
                }
            }
        };

        cancelButton.onclick = function() {
            loadReplies();
        };
    }

    async function deleteReply(replyId) {
        if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
            try {
                await axios.delete('<c:url value="/reply/"/>' + replyId);
                loadReplies();
            } catch (error) {
                console.error('댓글 삭제 중 오류가 발생했습니다:', error);
                if (error.response) {
                    const errorCode = error.response.status;
                    window.location.href = '/error/' + errorCode;
                }
            }
        }
    }

    window.addEventListener('load', loadReplies);
</script>
</body>
</html>
