<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8" lang="en">
    <title>Create a new meal</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<main>
    <a class="navigate" href="mealApp">Back to list</a>
    <section>
        <form action="mealApp?action=save" method="POST">
            <label>Date: <input name="datetime" type="datetime-local" value="" required></label>
            <label>Description: <textarea name="description" rows="3"></textarea></label>
            <label>Calories: <input name="calories" type="number" value="" required></label>
            <input type="submit" value="Save meal">
            <input type="reset" value="Reset fields">
        </form>
    </section>
</main>

</body>
</html>
