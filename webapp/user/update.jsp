<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="kr">
<head>
    <%@ include file="/include/header.jsp" %>
</head>
<body>
<%@ include file="/include/navigation.jsp" %>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><h4>Profiles</h4></div>
            <div class="panel-body">
                <div class="well well-sm">
                    <div class="media">
                        <a class="thumbnail pull-left" href="#">
                            <img class="media-object" src="../images/80-text.png">
                        </a>
                        <form name="question" method="post" action="/user/update">
                            <input type="hidden" id="userId" name="userId" value="${user.userId}">
                            <div class="media-body">
                                <h4 class="media-heading">
                                    <input class="form-control" id="name" name="name" value="${user.name}" placeholder="${user.name}">
                                </h4>
                                <p>
                                    <a href="#" class="btn btn-xs btn-default">
                                        <span class="glyphicon glyphicon-envelope"></span>&nbsp;
                                        <input class="form-control" id="email" name="email" value="${user.email}" placeholder="${user.email}">
                                    </a>
                                </p>
                            </div>
                            <button type="submit" class="btn btn-success clearfix pull-right">회원 수정</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="/include/footer.jsp" %>

</body>
</html>