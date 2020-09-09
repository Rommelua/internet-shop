<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<h2>Please fill the form to register a new user</h2>
<h4 style="color: red">${message}</h4>
<form method="post" action="${pageContext.request.contextPath}/registration">
    <div class="form-group">
        <label class="col-form-label" for="name">Name:</label>
        <input class="form-control" id="name" name="name" type="text" required="required" value=${name}>
    </div>
    <div class="form-group">
        <label class="col-form-label" for="login">Login:</label>
        <input class="form-control" id="login" name="login" type="text" required="required" value=${login}>
    </div>
    <div class="form-group">
        <label class="col-form-label" for="passwordEdit">Password:</label>
        <input class="form-control" id="passwordEdit" name="psw" type="password" required="required">
    </div>
    <div class="form-group">
        <label class="col-form-label" for="passwordRepeat">Repeat password:</label>
        <input class="form-control" id="passwordRepeat" name="pswRep" type="password" required="required">
    </div>
    <button class="btn btn-primary" type="submit">Register</button>
</form>
<a href="${pageContext.request.contextPath}/">Back to main page</a>
</body>
</html>
