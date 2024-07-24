String.prototype.format = function () {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};

document.addEventListener('DOMContentLoaded', function () {
    var currentPage = window.location.pathname;
    var menuItems = document.querySelectorAll('#nav-menu li a');

    menuItems.forEach(function (item) {
        if (item.getAttribute('href') === currentPage) {
            item.parentElement.classList.add('active');
        }
    });
});