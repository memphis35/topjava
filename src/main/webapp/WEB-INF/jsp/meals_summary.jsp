<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html>
<head>
    <meta charset="UTF-8" lang="EN">
    <title>Meals Summary</title>
    <link rel="stylesheet" href="css/styles.css"/>
</head>
<body>
<main>
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
                <c:if test="${meal.getExcess()}">
                    <tr class="exceed">
                </c:if>
                <c:if test="${!meal.getExcess()}">
                    <tr class="not-exceed">
                </c:if>
                <td>${meal.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm"))}</td>
                <td>${meal.getDescription()}</td>
                <td>${meal.getCalories()}</td>
                <td>UPDATE</td>
                <td>DELETE</td>
                </tr>
            </c:forEach>
        </table>
    </section>
</main>
</body>
</html>
