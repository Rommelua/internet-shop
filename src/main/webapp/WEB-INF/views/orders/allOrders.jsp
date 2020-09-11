<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All orders</title>
</head>
<body>
<%@include file="/WEB-INF/views/header.jsp" %>
<main role="main" class="container">
    <h3>All orders</h3>
    <table border="2">
        <tr>
            <th>ID</th>
            <th>User id</th>
            <th>Delete</th>
        </tr>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>
                    <a href="${pageContext.request.contextPath}/orders/details?id=${order.id}"><c:out
                            value="${order.id}"/></a>
                </td>
                <td>
                    <c:out value="${order.userId}"/>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/orders/delete?id=${order.id}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</main>
</body>
</html>
