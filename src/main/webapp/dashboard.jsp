<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="bank.model.User" %>
<!DOCTYPE html>
<html>
<head><title>JavaBank – Dashboard</title><link rel="stylesheet" href="css/style.css"/></head>
<body>
<%
    if(session.getAttribute("userId") == null){ response.sendRedirect("login"); return; }
    User user = (User) request.getAttribute("user");
    String toast = (String) session.getAttribute("toast");
    if(toast != null) session.removeAttribute("toast");
%>
<div class="dashboard">
    <div class="navbar">
        <h2>JavaBank</h2>
            <div class="user-info">
                Welcome, <%= user.getFullName() %> | 
                <% if ("admin@javabank.com".equals(session.getAttribute("email"))) { %>
                    <a href="admin" style="color: #ffc107; font-weight: bold; margin-right: 15px;">Admin Panel</a> | 
                <% } %>
                <a href="logout">Logout</a>
            </div>
    </div>

    <% if(toast != null){ %><div class="toast"><%= toast %></div><% } %>

    <div class="balance-card">
        <p>Account No: <strong><%= user.getAccountNumber() %></strong></p>
        <h3>Balance: ₹<%= String.format("%.2f", user.getBalance()) %></h3>
    </div>

    <div class="actions">
        <a href="deposit"            class="btn">💰 Deposit</a>
        <a href="withdraw"           class="btn">💸 Withdraw</a>
        <a href="fundTransfer"       class="btn">🔁 Fund Transfer</a>
        <a href="transactionHistory" class="btn">📋 History</a>
    </div>
</div>
</body></html>