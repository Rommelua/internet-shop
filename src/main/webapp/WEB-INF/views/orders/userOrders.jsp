<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User orders</title>
</head>
<body>
<%@include file="/WEB-INF/views/header.jsp" %>
<main role="main" class="container">
    <h3>Your orders</h3>
    <table border="2">
        <tr>
            <th>Order id</th>
            <th>User id</th>
            <th>Details</th>
        </tr>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>
                    <c:out value="${order.id}"/>
                </td>
                <td>
                    <c:out value="${order.userId}"/>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/orders/details?id=${order.id}">Details</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</main>
</body>
</html>
