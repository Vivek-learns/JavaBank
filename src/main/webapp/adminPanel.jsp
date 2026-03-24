<%@ page import="java.util.List" %>
<%@ page import="bank.model.Transaction" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Mode - JavaBank</title>
    <link rel="stylesheet" href="css/style.css"/>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css"/>
    <style>
        body { margin: 0; padding: 0; background: linear-gradient(135deg, #f0f8ff 0%, #b3d4ff 100%); min-height: 100vh; font-family: 'Inter', sans-serif; }
        .container { max-width: 1000px; margin: 40px auto; padding: 0 20px; }
        
        table.dataTable thead th { 
            background-color: #1a4a76 !important; 
            color: #ffffff !important; 
            border-bottom: none !important;
            padding: 12px 15px;
            font-size: 15px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        table.dataTable tbody tr:hover { background-color: #e9f2fb !important; }
        
        .filter-section {
            background: white; padding: 15px 20px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.05);
            display: flex; align-items: center; gap: 15px;
        }
        .filter-section select {
            padding: 8px 12px; border: 1px solid #ced4da; border-radius: 5px; font-size: 14px; cursor: pointer; outline: none; background: #f8f9fa;
        }
        .filter-section select:focus { border-color: #1a4a76; }
        
        .header-section {
            display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px;
        }
        .header-section h1 {
            color: #1a4a76; margin: 0; font-size: 28px;
        }
        .header-links a {
            text-decoration: none; font-weight: bold; font-size: 16px; margin-left: 20px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="header-section">
        <h1>Admin Mode</h1>
        <div class="header-links">
            <a href="dashboard" style="color: #1a4a76;">Dashboard</a>
            <a href="logout" style="color: #dc3545;">Logout</a>
        </div>
    </div>

    <div class="filter-section">
        <label for="typeSelect" style="font-weight: bold; color: #333; font-size: 15px;">Filter by Transaction:</label>
        <select id="typeSelect">
            <option value="">All Transactions</option>
            <option value="DEPOSIT">Deposits Only</option>
            <option value="WITHDRAW">Withdrawals Only</option>
            <option value="TRANSFER_OUT">Transfers Sent</option>
            <option value="TRANSFER_IN">Transfers Received</option>
        </select>
    </div>

    <div style="background: white; padding: 25px; border-radius: 10px; box-shadow: 0 8px 20px rgba(0,0,0,0.08);">
        <table id="adminTable" class="display" style="width:100%">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>User</th>
                    <th>Type</th>
                    <th>Description</th>
                    <th>Amount</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Transaction> txs = (List<Transaction>) request.getAttribute("transactions");
                    if (txs != null && !txs.isEmpty()) {
                        for (Transaction t : txs) {
                %>
                <tr>
                    <td><%= t.getId() %></td>
                    <td style="font-weight: 600; color: #1a4a76;"><%= t.getUserName() != null ? t.getUserName() : "Unknown" %></td>
                    <td>
                        <span style="font-weight: bold; background: <%= t.getType().contains("IN") || t.getType().equals("DEPOSIT") ? "#d4edda" : "#f8d7da" %>; 
                                     color: <%= t.getType().contains("IN") || t.getType().equals("DEPOSIT") ? "#155724" : "#721c24" %>; 
                                     padding: 4px 8px; border-radius: 4px; font-size: 12px;">
                            <%= t.getType() %>
                        </span>
                    </td>
                    <td style="color: #555;"><%= t.getDescription() != null ? t.getDescription() : "-" %></td>
                    <td style="font-weight: 600;">₹<%= String.format("%.2f", t.getAmount()) %></td>
                    <td style="color: #666; font-size: 14px;"><%= t.getCreatedAt() %></td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function() {
        var table = $('#adminTable').DataTable({
            "order": [[ 5, "desc" ]], // Sort by date descending
            "pageLength": 25,
            "language": {
                "search": "Global Search:"
            }
        });

        // Add event listener to the custom select dropdown to filter the Type column (index 2)
        $('#typeSelect').on('change', function () {
            var val = $.fn.dataTable.util.escapeRegex($(this).val());
            // Use word boundary instead of strict start/end to ignore HTML whitespace padding
            table.column(2).search(val ? '\\b' + val + '\\b' : '', true, false).draw();
        });
    });
</script>
</body>
</html>
