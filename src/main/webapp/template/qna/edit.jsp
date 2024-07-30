<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="/static/css/styles.css" rel="stylesheet">
</head>
<body>
<%@ include file="/template/common/header.jsp" %>

<h1>수정 페이지</h1>
<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <form id="updateForm" name="question" action="#">
                <div class="form-group">
                    <label for="title">제목</label>
                    <input form="updateForm" type="text" class="form-control" id="title" name="title" placeholder="제목"/>
                </div>
                <div class="form-group">
                    <label for="contents">내용</label>
                    <textarea form="updateForm" name="contents" id="contents" rows="5" class="form-control"></textarea>
                </div>
                <button type="button"
                        onclick="patch_question()"
                        class="btn btn-success clearfix pull-right">
                    수정하기
                </button>
                <div class="clearfix"/>
            </form>
        </div>
    </div>
</div>

<!-- script references -->
<script src="/static/js/jquery-2.2.0.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/scripts.js"></script>
<script>
    function patch_question() {
        let currentUrl = window.location.href;
        let url = currentUrl.substring(0, currentUrl.lastIndexOf('/form'));

        let form = document.getElementById('updateForm');
        let newForm = new FormData(form);
        let formData = new URLSearchParams(newForm).toString();

        fetch(url, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: formData
        }).then(response => {
            if (response.ok) {
                alert('수정되었습니다.');
                location.href = url;
            } else {
                alert('수정에 실패했습니다.');
            }
        });
    }
</script>
</body>
</html>