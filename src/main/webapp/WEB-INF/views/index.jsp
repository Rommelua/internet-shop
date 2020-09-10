<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Internet shop</h1>
<ul>
    <li><a href="${pageContext.request.contextPath}/registration">Registration</a></li>
    <li><a href="${pageContext.request.contextPath}/products/all">All products</a></li>
    <li><a href="${pageContext.request.contextPath}/shopping-carts/products/">Shopping cart</a></li>
    <li><a href="${pageContext.request.contextPath}/orders/user">User orders</a></li>
    <li><a href="${pageContext.request.contextPath}/products/manage">Manage products</a></li>
    <li><a href="${pageContext.request.contextPath}/users/all">All users</a></li>
    <li><a href="${pageContext.request.contextPath}/orders/all">All orders</a></li>
    <li><a href="${pageContext.request.contextPath}/inject">Inject mock data to DB</a></li>
</ul>
</body>
</html>
