<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Fund Transfer</title><link rel="stylesheet" href="css/style.css"/></head>
<body>
<div class="card">
    <h2>Fund Transfer</h2>
    <% if(request.getAttribute("error") != null){ %><p class="error">${error}</p><% } %>
    <form action="fundTransfer" method="post">
        <input type="text"   name="receiverAccount" placeholder="Receiver Account Number" required/>
        <input type="number" name="amount"           placeholder="Amount (₹)" min="1" step="0.01" required/>
        <input type="text"   name="message"          placeholder="Message (optional)" maxlength="50"/>
        <button type="submit">Transfer</button>
    </form>
    <a href="dashboard">← Back</a>
</div>
</body></html>