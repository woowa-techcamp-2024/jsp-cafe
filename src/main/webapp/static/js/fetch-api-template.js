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

async function httpPost(url, event) {
    event.preventDefault();
    event.stopPropagation();

    const formData = new FormData(event.target);
    const formObject = {};
    formData.forEach((value, key) => {
        formObject[key] = value;
    });

    const jsonString = JSON.stringify(formObject);
    try {
        const response = await $.ajax(url, {
            type: 'POST',
            cors: false,
            data: jsonString,
            contentType: 'application/json',
            processData: false
        });
        console.log("성공 response : " + response);
        return response;
    } catch (xhr) {
        let message;
        try {
            message = JSON.parse(xhr.responseText)['message'];
        } catch (e) {
            message = "실패";
        }
        throw message;
    }
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

async function httpGetAjax(url) {
    try {
        const response = await $.ajax(url, {
            type: 'GET',
            cors: false,
            processData: false
        });
        console.log("성공 response : " + response);
        return response;
    } catch (xhr) {
        let message;
        try {
            message = JSON.parse(xhr.responseText)['message'];
        } catch (e) {
            message = "실패";
        }
        throw message;
    }
}

async function httpDelete(url) {
    try {
        const response = await $.ajax(url, {
            type: 'DELETE',
            contentType: 'application/json',
            processData: false,
        });
        console.log("성공 response : " + response);
        return response;
    } catch (xhr) {
        if (xhr.getResponseHeader('Content-Type').includes('application/json')) {
            let message;
            try {
                message = JSON.parse(xhr.responseText)['message'];
            } catch (e) {
                message = xhr.responseText;
            }
            throw message;
        } else {
            document.body.innerHTML = xhr.responseText;
            history.pushState(null, '', url);
        }
    }
}

window.addEventListener('popstate', function (event) {
    httpGet(location.pathname);
});
