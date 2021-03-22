<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <title>Работа мечты</title>
</head>
<body>
<div class="container pt-3">
    <div class="row">
        <a class="nav-link" href="<c:url value='/index.do'/>">Главная</a>
        <c:set var="userName" value="${user.name}"/>
        <c:if test="${userName != null}">
            <a class="nav-link" href="<%=request.getContextPath()%>/leave.do">${userName} | Выйти</a>
        </c:if>
        <div class="card" style="width: 100%">
            <div class="card-header">Кандидаты</div>
            <div class="card-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col"></th>
                        <th scope="col">Имя</th>
                        <th scope="col">Фото</th>
                        <th scope="col">Город</th>
                        <th scope="col"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        Map<Integer, String> cities = (Map<Integer, String>) request.getAttribute("cities");
                        Map<Integer, String> photoCandidates = (Map<Integer, String>) request.getAttribute("photo");
                        List<Candidate> candidates = (List<Candidate>) request.getAttribute("candidates");
                        for (Candidate candidate : candidates) {
                            int id = candidate.getId();
                            String photo = photoCandidates.get(id);
                            String city = cities.get(candidate.getId());
                    %>
                    <tr>
                        <th><a href="<%=request.getContextPath()%>/candidate/edit.jsp?id=<%=id%>&city=<%=city%>"><i
                                class="fa fa-edit mr-3"></i></a></th>
                        <th><%=candidate.getName()%>
                        </th>
                        <th>
                            <% if (candidate.getPhotoId() == 0) { %>
                            <a href="<%=request.getContextPath()%>/upload?id=<%=id%>">Загрузить</a>
                            <% } else { %>
                            <p><img src="<%=request.getContextPath()%>/show?photo=<%=photo%>" width="100px"
                                    height="100px"/></p>
                            <p><a href="<%=request.getContextPath()%>/show?photo=<%=photo%>"> Скачать фото</a></p>
                            <% } %>
                        </th>
                        <th><%=city%>
                        </th>
                        <th><a href="<%=request.getContextPath()%>/deleteCandidate?id=<%=id%>&photo=<%=photo%>">Удалить
                            кандидата</a></th>
                    </tr>
                    <%}%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>