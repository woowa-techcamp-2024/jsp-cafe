function httpPut(url, event) {
    event.preventDefault();
    event.stopPropagation();

    const formData = new FormData(event.target);
    const formObject = {};
    formData.forEach((value, key) => {
        formObject[key] = value;
    });

    const jsonString = JSON.stringify(formObject);
    $.ajax(url, {
        type: 'PUT',
        cors: false,
        data: jsonString,
        contentType: 'application/json',
        processData: false,
        success: function (data) {
            $('#message').hide();
            console.log("성공 Response :" + data['redirect']);
            window.location.href = data['redirect'];
        },
        error: function (xhr, textStatus, errorThrown) {
            try {
                message = JSON.parse(xhr.responseText)['message'];
            } catch (e) {
                messagse = "실패"
            }
            errorDiv = $('#message');
            errorDiv.text(message);
            errorDiv.show();
        },
    });
}

function httpGet(url) {
    fetch(url, {
        method: 'GET',
    }).then(response => {
        return response.text();
    }).then(html => {
        document.body.innerHTML = html;
        history.pushState(null, '', url);
    })
}

function httpDelete(url) {
    $.ajax(url, {
        type: 'DELETE',
        contentType: 'application/json',
        processData: false,
        success: function (data) {
            window.location.href = data['redirect'];
        },
        error: function (xhr, textStatus, errorThrown) {
            document.body.innerHTML = xhr.responseText;
            history.pushState(null, '', url);
        }
    });
}

window.addEventListener('popstate', function (event) {
    httpGet(location.pathname);
});
