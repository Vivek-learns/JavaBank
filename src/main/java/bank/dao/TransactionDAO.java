package bank.dao;


import bank.db.DBconnection;
import bank.model.Transaction;
import java.sql.*;
import java.util.*;

public class TransactionDAO {

    public boolean log(int userId, String type, double amount,
                       String relatedAccount, String description) throws SQLException {
        String sql = "INSERT INTO transactions (user_id, type, amount, related_account, description) VALUES (?,?,?,?,?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setString(4, relatedAccount);
            ps.setString(5, description);
            return ps.executeUpdate() > 0;
        }
    }

    // Overload for shared connection (fund transfer transaction)
    public boolean log(Connection conn, int userId, String type, double amount,
                       String relatedAccount, String description) throws SQLException {
        String sql = "INSERT INTO transactions (user_id, type, amount, related_account, description) VALUES (?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setString(2, type);
        ps.setDouble(3, amount);
        ps.setString(4, relatedAccount);
        ps.setString(5, description);
        return ps.executeUpdate() > 0;
    }

    public List<Transaction> getByUserId(int userId) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setUserId(rs.getInt("user_id"));
                t.setType(rs.getString("type"));
                t.setAmount(rs.getDouble("amount"));
                t.setRelatedAccount(rs.getString("related_account"));
                t.setDescription(rs.getString("description"));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(t);
            }
        }
        return list;
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT t.*, u.full_name as user_name FROM transactions t JOIN users u ON t.user_id = u.id ORDER BY t.created_at DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setUserId(rs.getInt("user_id"));
                t.setType(rs.getString("type"));
                t.setAmount(rs.getDouble("amount"));
                t.setRelatedAccount(rs.getString("related_account"));
                t.setDescription(rs.getString("description"));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setUserName(rs.getString("user_name"));
                list.add(t);
            }
        }
        return list;
    }
}
