String.prototype.format = function () {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};

function logout(event) {
    event.preventDefault(); // 기본 링크 동작을 막습니다.

    // 로그아웃 요청을 보내기 위한 폼 요소를 만듭니다.
    var form = document.createElement('form');
    form.method = 'POST';
    form.action = '/users/logout';

    document.body.appendChild(form);
    form.submit(); // 폼을 제출하여 POST 요청을 보냅니다.
}

document.addEventListener('DOMContentLoaded', function () {
    var currentPage = window.location.pathname;
    var menuItems = document.querySelectorAll('#nav-menu li a');

    menuItems.forEach(function (item) {
        if (item.getAttribute('href') === currentPage) {
            item.parentElement.classList.add('active');
        }
    });
});