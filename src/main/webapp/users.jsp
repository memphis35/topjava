<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Users</h2>
<section class="login-password">
    <form action="users" method="POST">
        <label>Login:
            <select name="userId">
                <option>1</option>
                <option>2</option>
            </select>
        </label>
        <label>
            Password:
            <input type="password" name="password">
        </label>
        <input type="submit" value="sign in">
    </form>
</section>
</body>
</html>