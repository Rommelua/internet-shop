<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Product added</title>
</head>
<body>
<%@include file="/WEB-INF/views/header.jsp" %>
<main role="main" class="container">
    <h4>Product has been added to your Shopping cart</h4>
    <a href="${pageContext.request.contextPath}/products/all">Continue shopping</a> or
    <a href="${pageContext.request.contextPath}/shopping-carts/products/">Go to Shopping cart</a><br/>
</main>
</body>
</html>
