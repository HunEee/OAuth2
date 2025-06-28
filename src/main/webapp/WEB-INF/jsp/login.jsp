<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>login page</title>
</head>
<body>
    login page
    <hr>
    <form action="/loginProc" method="post" name="loginForm">
        <input id="username" type="text" name="username" placeholder="id"/>
        <input id="password" type="password" name="password" placeholder="password"/>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/> <!-- csrf 토큰 발급  -->
        <input type="submit" value="login"/>
    </form>
</body>
</html>