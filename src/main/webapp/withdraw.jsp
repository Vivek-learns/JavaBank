<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Withdraw</title><link rel="stylesheet" href="css/style.css"/></head>
<body>
<div class="card">
    <h2>Withdraw Money</h2>
    <% if(request.getAttribute("error") != null){ %><p class="error">${error}</p><% } %>
    <form action="withdraw" method="post">
        <input type="number" name="amount" placeholder="Amount (₹)" min="1" step="0.01" required/>
        <button type="submit">Withdraw</button>
    </form>
    <a href="dashboard">← Back</a>
</div>
</body></html>