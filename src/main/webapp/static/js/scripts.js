String.prototype.format = function () {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};


function formatTime(createdTime) {
    // createdTime이 이미 배열인지 확인
    const timeArray = Array.isArray(createdTime) ? createdTime : createdTime.split(',');

    const [year, month, day, hour, minute, second] = timeArray;

    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`;
}
