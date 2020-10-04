<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8" lang="en"/>
    <title>Update current meal</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<main>
    <a class="navigate" href="meals?action=list">Return to list</a>
    <section>
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
        <form action="meals?action=update" method="POST">
            <label>Date: <input name="datetime" type="datetime-local" value="${meal.dateTime}" required></label>
            <label>Description: <textarea name="description" rows="3" required>${meal.description}</textarea></label>
            <label>Calories: <input name="calories" type="number" value="${meal.calories}" required></label>
            <input type="hidden" name="id" value="${meal.id}">
            <input type="submit" value="Save/Update meal">
            <input type="reset" value="Reset fields">
        </form>
    </section>
    <a class="navigate" href="index.html">Back to homepage</a>
</main>
</body>
</html>
