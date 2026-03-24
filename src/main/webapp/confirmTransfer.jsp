<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Confirm Transfer</title><link rel="stylesheet" href="css/style.css"/></head>
<body>
<div class="card" style="width: 400px;">
    <h2>Confirm Transfer</h2>
    <p>Please review the details below before completing your transfer.</p>
    <div style="background: #f9f9f9; padding: 15px; border-radius: 5px; margin-bottom: 20px; text-align: left;">
        <p><strong>To:</strong> ${receiverName}</p>
        <p><strong>Account:</strong> ${receiverAccount}</p>
        <p><strong>Amount:</strong> ₹${amount}</p>
        <% if(request.getAttribute("message") != null && !request.getAttribute("message").toString().trim().isEmpty()){ %>
            <p><strong>Message:</strong> ${message}</p>
        <% } %>
    </div>
    
    <form action="fundTransfer" method="post" style="display:inline;">
        <input type="hidden" name="receiverAccount" value="${receiverAccount}"/>
        <input type="hidden" name="amount" value="${amount}"/>
        <input type="hidden" name="message" value="${message}"/>
        <input type="hidden" name="action" value="confirm"/>
        <button type="submit" style="background-color: #28a745;">Confirm Transfer</button>
    </form>
    <a href="fundTransfer" style="display:inline-block; margin-left:15px; color:#555;">Cancel</a>
</div>
</body>
</html>
