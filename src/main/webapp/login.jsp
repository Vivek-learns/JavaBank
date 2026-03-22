<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>JavaBank – Login</title><link rel="stylesheet" href="css/style.css"/></head>
<body>
<div class="card">
    <h2>JavaBank Login</h2>
    <% if("registered".equals(request.getParameter("msg"))){ %>
        <p class="success">Registered successfully! Please login.</p>
    <% } %>
    <% if(request.getAttribute("error") != null){ %>
        <p class="error">${error}</p>
    <% } %>
    <form action="login" method="post">
        <input type="email"    name="email"    placeholder="Email"    required/>
        <input type="password" name="password" placeholder="Password" required/>
        <button type="submit">Login</button>
    </form>
    <p>Don't have an account? <a href="register">Register</a></p>
</div>
</body></html>