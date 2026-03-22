package bank.dao;


import bank.db.DBconnection;
import bank.model.User;
import java.sql.*;

public class UserDAO {

    // Generate a simple unique account number
    private String generateAccountNumber() {
        return "JB" + System.currentTimeMillis();
    }

    public boolean register(User user) throws SQLException {
        String sql = "INSERT INTO users (full_name, email, password, account_number, balance) VALUES (?,?,?,?,?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword()); // hash in production!
            ps.setString(4, generateAccountNumber());
            ps.setDouble(5, 0.00);
            return ps.executeUpdate() > 0;
        }
    }

    public User login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        }
        return null;
    }

    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        }
        return null;
    }

    public User getUserByAccountNumber(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM users WHERE account_number = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        }
        return null;
    }

    public boolean updateBalance(int userId, double newBalance) throws SQLException {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // Used for atomic fund transfer — takes external connection
    public boolean updateBalance(Connection conn, int userId, double newBalance) throws SQLException {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, newBalance);
        ps.setInt(2, userId);
        return ps.executeUpdate() > 0;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setAccountNumber(rs.getString("account_number"));
        u.setBalance(rs.getDouble("balance"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }
}