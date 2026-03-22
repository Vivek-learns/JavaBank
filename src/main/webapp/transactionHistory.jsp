<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, bank.model.Transaction" %>
<!DOCTYPE html>
<html>
<head><title>Transaction History</title><link rel="stylesheet" href="css/style.css"/></head>
<body>
<div class="dashboard">
    <div class="navbar"><h2>Transaction History</h2><a href="dashboard">← Dashboard</a></div>
    <% if(request.getAttribute("error") != null){ %><p class="error">${error}</p><% } %>
    <%
        List<Transaction> txns = (List<Transaction>) request.getAttribute("transactions");
        if(txns == null || txns.isEmpty()){
    %><p style="text-align:center;margin-top:30px;">No transactions yet.</p><%
        } else { %>
    <table>
        <tr><th>#</th><th>Type</th><th>Amount (₹)</th><th>Description</th><th>Date & Time</th></tr>
        <% for(Transaction t : txns){ %>
        <tr class="tx-<%= t.getType().toLowerCase() %>">
            <td><%= t.getId() %></td>
            <td><%= t.getType() %></td>
            <td><%= String.format("%.2f", t.getAmount()) %></td>
            <td><%= t.getDescription() %></td>
            <td><%= t.getCreatedAt() %></td>
        </tr>
        <% } %>
    </table>
    <% } %>
</div>
</body></html>