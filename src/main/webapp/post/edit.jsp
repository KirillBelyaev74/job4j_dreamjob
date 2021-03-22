<%@ page import="ru.job4j.dream.model.Post" %>
<%@ page import="ru.job4j.dream.store.PsqlPost" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="<c:url value="/style.css"/>"/>

    <title>Работа мечты</title>
</head>
<%
    Post post = new Post(0, "", "", null);
    String id = request.getParameter("id");
    if (id != null) {
        post = PsqlPost.instOf().getPostById(id);
    }
%>
<body>
<div class="container pt-3">
    <div class="row">
        <a class="nav-link" href="<c:url value='/index.do'/>">Главная</a>
        <c:set var="userName" value="${user.name}"/>
        <c:if test="${userName != null}">
            <a class="nav-link" href="<%=request.getContextPath()%>/leave.do">${userName} | Выйти</a>
        </c:if>
        <div class="card" style="width: 100%">
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/posts.do?id=<%=post.getId()%>" method="post">
                    <div class="form-group">
                        <label>Название вакансии</label>
                        <input type="text" class="form-control" name="name" id="name" value="<%=post.getName()%>">
                    </div>
                    <div class="form-group">
                        <label>Описание вакансии</label>
                        <input type="text" class="form-control" name="description" id="description"
                               value="<%=post.getDescription()%>">
                    </div>
                    <button type="submit" class="btn btn-primary" onclick="validatePost()">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>