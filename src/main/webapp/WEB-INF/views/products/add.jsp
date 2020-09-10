<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add product</title>
</head>
<body>
<h2>Please fill the form to add a new product</h2>
<h4 style="color: red">${message}</h4>
<form method="post" action="${pageContext.request.contextPath}/products/add">
    <div class="form-group">
        <label class="col-form-label" for="name">Name:</label><br/>
        <input class="form-control" id="name" name="name" type="text" required="required">
    </div>
    <div class="form-group">
        <label class="col-form-label" for="price">Price:</label><br/>
        <input class="form-control" id="price" name="price" type="text" required="required">
    </div>
    <button class="btn btn-primary" type="submit">Add product</button>
</form>
<a href="${pageContext.request.contextPath}/">Back to main page</a>
</body>
</html>
