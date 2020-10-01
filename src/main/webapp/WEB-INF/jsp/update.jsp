<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8" lang="en"/>
    <title>Update current meal</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<a class="navigate" href="mealApp?action=list">Back to list</a>
<main>
    <section>
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
        <form action="mealApp?action=update" method="POST">
            <label>Date: <input name="datetime" type="datetime-local" value="${meal.dateTime}"></label>
            <label>Description: <textarea name="description" rows="3">${meal.description}</textarea></label>
            <label>Calories: <input name="calories" type="number" value="${meal.calories}"></label>
            <input type="submit" value="Update meal">
            <input type="reset" value="Reset fields">
        </form>
    </section>
</main>
</body>
</html>
