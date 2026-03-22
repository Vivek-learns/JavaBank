<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>JavaBank – Register</title><link rel="stylesheet" href="css/style.css"/></head>
<body>
<div class="card">
    <h2>Create Account</h2>
    <% if(request.getAttribute("error") != null){ %>
        <p class="error">${error}</p>
    <% } %>
    <form action="register" method="post">
        <input type="text"     name="fullName"        placeholder="Full Name"        required/>
        <input type="email"    name="email"           placeholder="Email"            required/>
        <input type="password" name="password"        placeholder="Password"         required/>
        <input type="password" name="confirmPassword" placeholder="Confirm Password" required/>
        <button type="submit">Register</button>
    </form>
    <p>Already have an account? <a href="login">Login</a></p>
</div>
</body></html>