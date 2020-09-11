<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<%@include file="header.jsp" %>
<main role="main" class="container">
    <h3>Please fill the form to register a new user</h3><br/>
    <h4 style="color: red">${message}</h4>
    <form method="post" action="${pageContext.request.contextPath}/registration">
        <div class="form-group">
            <label class="col-form-label" for="name">Name:</label><br/>
            <input class="form-control" id="name" name="name" type="text" style="width: 300px" required value=${name}>
        </div>
        <div class="form-group">
            <label class="col-form-label" for="login">Login:</label><br/>
            <input class="form-control" id="login" name="login" type="text" style="width: 300px" required value=${login}>
        </div>
        <div class="form-group">
            <label class="col-form-label" for="passwordEdit">Password:</label><br/>
            <input class="form-control" id="passwordEdit" name="psw" style="width: 300px" type="password" required>
        </div>
        <div class="form-group">
            <label class="col-form-label" for="passwordRepeat">Repeat password:</label><br/>
            <input class="form-control" id="passwordRepeat" name="pswRep" style="width: 300px" type="password" required>
        </div>
        <br/>
        <button class="btn btn-primary" type="submit">Register</button>
    </form>
</main>
</body>
</html>
