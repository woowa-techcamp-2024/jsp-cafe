<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 정보 수정</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body class="bg-gray-100 min-h-screen flex flex-col">
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="flex-grow flex items-center justify-center">
    <div class="w-full max-w-md">
        <h2 class="text-3xl font-bold text-center mb-8">회원 정보 수정</h2>

        <div id="passwordError"
             class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4"
             role="alert">
            <strong class="font-bold">오류!</strong>
            <span class="block sm:inline">현재 비밀번호가 일치하지 않습니다.</span>
        </div>

        <form id="updateForm" class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="email">
                    이메일
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                       id="email" name="email" type="email" value="<c:out value='${user.email()}'/>"
                       required>
            </div>
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="nickname">
                    닉네임
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                       id="nickname" name="nickname" type="text"
                       value="<c:out value='${user.nickname()}'/>" required>
            </div>
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="currentPassword">
                    현재 비밀번호
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
                       id="currentPassword" name="currentPassword" type="password" required>
            </div>
            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="newPassword">
                    새 비밀번호
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
                       id="newPassword" name="newPassword" type="password">
                <p class="text-gray-600 text-xs italic">비밀번호를 변경하지 않으려면 비워두세요.</p>
            </div>
            <div class="mb-6">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="confirmPassword">
                    새 비밀번호 확인
                </label>
                <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
                       id="confirmPassword" name="confirmPassword" type="password">
            </div>
            <div class="flex items-center justify-between">
                <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                        type="submit">
                    수정 완료
                </button>
                <a class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800"
                   href="<c:url value='/users/${user.id()}'/>">
                    취소
                </a>
            </div>
        </form>
    </div>
</main>

<script>
  document.getElementById('updateForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const formData = new FormData(this);
    const data = Object.fromEntries(formData.entries());

    // 새 비밀번호가 비어있으면 현재 비밀번호로 설정
    if (!data.newPassword) {
      data.newPassword = data.currentPassword;
    } else {
      // 새 비밀번호가 입력된 경우, 확인 비밀번호와 일치하는지 검증
      if (data.newPassword !== data.confirmPassword) {
        alert('새 비밀번호와 확인 비밀번호가 일치하지 않습니다.');
        return;
      }
    }

    // 확인 비밀번호 필드는 서버로 전송하지 않음
    delete data.confirmPassword;

    axios.put('<c:url value="/users/${user.id()}"/>', data, {
      maxRedirects: 0, // 자동 리다이렉트를 비활성화
    })
    .then(response => {
      // 성공적으로 업데이트된 경우 (이 블록은 실행되지 않을 가능성이 높습니다)
      window.location.href = '<c:url value="/users/${user.id()}"/>';
    })
    .catch(error => {
      if (error.response) {
        if (error.response.status === 401) {
          // 303 상태 코드를 받으면 Location 헤더의 URL로 리다이렉트
          window.location.href = error.response.headers.location;
        } else {
          // 다른 오류 상태 코드 처리
          console.error('Error:', error.response.data);
          alert('오류가 발생했습니다. 다시 시도해주세요.');
        }
      } else if (error.request) {
        // 요청은 보냈지만 응답을 받지 못한 경우
        console.error('No response:', error.request);
        alert('서버로부터 응답이 없습니다. 네트워크 연결을 확인하세요.');
      } else {
        // 요청 설정 중 오류가 발생한 경우
        console.error('Error:', error.message);
        alert('요청 중 오류가 발생했습니다. 다시 시도해주세요.');
      }
    });
  });
</script>

</body>
</html>
