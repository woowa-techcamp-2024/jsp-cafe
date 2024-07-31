<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 수정</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body class="bg-gray-100 min-h-screen">

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="container mx-auto mt-8 px-4">
    <div class="bg-white shadow rounded-lg p-6">
        <h2 class="text-2xl font-bold mb-6">게시글 수정</h2>
        <form id="editForm" onsubmit="handleSubmit(event)">
            <div class="mb-4">
                <label for="title" class="block text-sm font-medium text-gray-700">제목</label>
                <input type="text" id="title" name="title" value="${article.title()}"
                       class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-teal-500 focus:ring focus:ring-teal-500 focus:ring-opacity-50">
            </div>
            <div class="mb-4">
                <label for="content" class="block text-sm font-medium text-gray-700">내용</label>
                <textarea id="content" name="content" rows="10"
                          class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-teal-500 focus:ring focus:ring-teal-500 focus:ring-opacity-50">${article.content()}</textarea>
            </div>
            <div class="flex justify-end space-x-2">
                <button type="submit"
                        class="bg-teal-500 hover:bg-teal-600 text-white font-bold py-2 px-4 rounded">
                    수정
                </button>
                <a href="/"
                   class="bg-gray-500 hover:bg-gray-600 text-white font-bold py-2 px-4 rounded">취소</a>
            </div>
        </form>
    </div>
</main>

<script>
  async function handleSubmit(event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);
    const data = Object.fromEntries(formData);

    try {
      const response = await axios.put('<c:url value="/articles/${article.id()}"/>', data, {
        headers: {
          'Content-Type': 'application/json'
        }
      });

      alert('게시글이 성공적으로 수정되었습니다.');
      window.location.href = '/';  // 메인 페이지로 리다이렉트
    } catch (error) {
      alert('게시글 수정에 실패했습니다. 다시 시도해주세요.');
      console.error('Error:', error);
    }
  }
</script>

</body>
</html>
