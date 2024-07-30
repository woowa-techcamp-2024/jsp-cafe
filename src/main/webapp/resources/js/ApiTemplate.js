function getAPI(url, successCallback, failCallback) {
    $.ajax({
        url: url,
        type: 'GET',
        success: function (response) {
            console.log('Success:', response);
            successCallback();
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
            failCallback();
        }
    });
}

function postAPI(url, data, successPath) {
    $.ajax({
        url: url,
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        data: data,
        success: function (response) {
            console.log('Success:', response);
            window.location.href = successPath;
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
            alert('에러 발생: ' + xhr.responseJSON.error);
        }
    });
}

function postJsonAPI(url, data, successPath) {
    $.ajax({
        url: url,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            console.log('Success:', response);
            window.location.href = successPath;
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
            alert('에러 발생: ' + xhr.responseJSON.error);
        }
    });
}

function putAPI(url, data, successPath) {
    $.ajax({
        url: url,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            console.log('Success:', response);
            window.location.href = successPath;
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
            alert('에러 발생: ' + xhr.responseJSON.error);
        }
    });
}

function deleteAPI(url, successPath) {
    $.ajax({
        url: url,
        type: 'DELETE',
        success: function (response) {
            console.log('Success:', response);
            window.location.href = successPath;
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
            alert('에러 발생: ' + xhr.responseJSON.error);
        }
    });
}