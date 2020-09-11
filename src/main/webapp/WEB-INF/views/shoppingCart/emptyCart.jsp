<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ShoppingCart</title>
</head>
<body>
<%@include file="/WEB-INF/views/header.jsp" %>
<main role="main" class="container">
<h3>ShoppingCart</h3>
<h4>Your sopping cart is empty, but you can fix it!</h4>
<a href="${pageContext.request.contextPath}/products/all">All products</a><br/>
</main>
</body>
</html>
