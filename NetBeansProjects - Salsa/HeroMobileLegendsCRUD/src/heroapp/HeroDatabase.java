package heroapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HeroDatabase {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_mobilelegend";
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; // isi jika kamu pakai password

    // Buka koneksi
    public static Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL tidak ditemukan!", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // Ambil semua hero dari database
    public static List<Hero> getAllHeroes() {
        List<Hero> heroList = new ArrayList<>();
        String sql = "SELECT * FROM tm_hero";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Hero hero = new Hero();
                hero.setId(rs.getInt("id_hero"));
                hero.setNama(rs.getString("nama_hero"));
                hero.setKategori(rs.getString("kategori"));
                hero.setGender(rs.getString("gender"));
                heroList.add(hero);
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data: " + e.getMessage());
        }

        return heroList;
    }

    // Tambah data hero
    public static boolean insertHero(Hero hero) {
        String sql = "INSERT INTO tm_hero (id_hero, nama_hero, kategori, gender) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hero.getId());
            stmt.setString(2, hero.getNama());
            stmt.setString(3, hero.getKategori());
            stmt.setString(4, hero.getGender());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gagal menambahkan hero: " + e.getMessage());
            return false;
        }
    }

    // Update data hero
    public static boolean updateHero(Hero hero) {
        String sql = "UPDATE tm_hero SET nama_hero=?, kategori=?, gender=? WHERE id_hero=?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hero.getNama());
            stmt.setString(2, hero.getKategori());
            stmt.setString(3, hero.getGender());
            stmt.setInt(4, hero.getId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gagal update hero: " + e.getMessage());
            return false;
        }
    }

    // Hapus data hero
    public static boolean deleteHero(int id) {
        String sql = "DELETE FROM tm_hero WHERE id_hero=?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gagal menghapus hero: " + e.getMessage());
            return false;
        }
    }
}
