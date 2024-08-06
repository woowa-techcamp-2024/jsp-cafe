function postAPI(url, data, successPath) {
    $.ajax({
        url: url,
        type: 'POST',
        contentType: 'application/x-www-form-urlencoded',
        data: data,
        success: function (response) {
            window.location.href = successPath;
        },
        error: function (xhr, status, error) {
            console.error('Error:', xhr.responseJSON.error);
            alert(xhr.responseJSON.error);
        }
    });
}

function postJsonAPI(url, data, successCallback) {
    $.ajax({
        url: url,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            successCallback(response);
        },
        error: function (xhr, status, error) {
            console.error('Error:', xhr.responseJSON.error);
            alert(xhr.responseJSON.error);
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
            window.location.href = successPath;
        },
        error: function (xhr, status, error) {
            console.error('Error:', xhr.responseJSON.error);
            alert(xhr.responseJSON.error);
        }
    });
}

function deleteAPI(url, successCallback) {
    $.ajax({
        url: url,
        type: 'DELETE',
        success: function (response) {
            successCallback(response)
        },
        error: function (xhr, status, error) {
            console.error('Error:', xhr.responseJSON.error);
            alert(xhr.responseJSON.error);
        }
    });
}