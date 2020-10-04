<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
<head>
    <meta charset="UTF-8" lang="EN">
    <title>Meals Summary</title>
    <link rel="stylesheet" href="css/styles.css"/>
</head>
<body>
<main>
    <a class="navigate" href="meals?action=create">Add new meal</a>
    <section class="meal-table">
        <table class="meal-summary">
            <tr class="header">
                <td>Date</td>
                <td>Description</td>
                <td>Calories</td>
                <td></td>
                <td></td>
            </tr>
            <jsp:useBean id="mealResultList" scope="request" type="java.util.List"/>
            <c:forEach items="${mealResultList}" var="meal">
                <c:if test="${meal.excess}">
                    <tr class="exceed">
                </c:if>
                <c:if test="${!meal.excess}">
                    <tr class="not-exceed">
                </c:if>
                <td>${meal.getFormattedDate()}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">UPDATE</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">DELETE</a></td>
                </tr>
            </c:forEach>
        </table>
        <a class="navigate" href="index.html">Back to homepage</a>
    </section>
</main>
</body>
</html>
