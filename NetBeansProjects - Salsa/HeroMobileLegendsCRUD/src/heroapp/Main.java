package heroapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}

class MainFrame extends JFrame {
    private JTextField txtId, txtNama;
    private JComboBox<String> cmbKategori, cmbGender;
    private JTable tblHero;
    private DefaultTableModel tableModel;

    MainFrame() {
        setTitle("CRUD Hero Mobile Legends");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 470);
        setLocationRelativeTo(null); // center window
        setLayout(new BorderLayout(10, 10));

        // ---------- PANEL INPUT ----------
        JPanel panelInput = new JPanel(new GridLayout(3, 4, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder("Input Data"));

        panelInput.add(new JLabel("ID Hero:"));
        txtId = new JTextField();
        panelInput.add(txtId);

        panelInput.add(new JLabel("Nama Hero:"));
        txtNama = new JTextField();
        panelInput.add(txtNama);

        panelInput.add(new JLabel("Kategori:"));
        cmbKategori = new JComboBox<>(new String[]{
            "MAGE", "ASSASIN", "FIGHTER", "TANK", "MARKSMAN", "SUPPORT"
        });
        panelInput.add(cmbKategori);

        panelInput.add(new JLabel("Gender:"));
        cmbGender = new JComboBox<>(new String[]{"MALE", "FEMALE"});
        panelInput.add(cmbGender);

        JButton btnTambah = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnHapus = new JButton("Hapus");
        panelInput.add(btnTambah);
        panelInput.add(btnUpdate);
        panelInput.add(btnHapus);

        add(panelInput, BorderLayout.NORTH);

        // ---------- TABEL ----------
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama", "Kategori", "Gender"}, 0);
        tblHero = new JTable(tableModel);
        tblHero.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(tblHero);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Hero"));
        add(scrollPane, BorderLayout.CENTER);

        // ---------- AKSI TOMBOL ----------
        btnTambah.addActionListener(e -> tambahData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e -> hapusData());

        // Isi form saat klik baris tabel
        tblHero.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblHero.getSelectedRow();
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtNama.setText(tableModel.getValueAt(row, 1).toString());
                cmbKategori.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                cmbGender.setSelectedItem(tableModel.getValueAt(row, 3).toString());
            }
        });

        loadTable();
        setVisible(true);
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/ml_hero_db", "root", "");
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tm_hero")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id_hero"),
                    rs.getString("nama_hero"),
                    rs.getString("kategori"),
                    rs.getString("gender")
                });
            }
        } catch (SQLException e) {
            showError("Load data gagal:\n" + e.getMessage());
        }
    }

    private void tambahData() {
        try (Connection conn = connect()) {
            String sql = "INSERT INTO tm_hero(id_hero, nama_hero, kategori, gender) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.setString(2, txtNama.getText());
            stmt.setString(3, cmbKategori.getSelectedItem().toString());
            stmt.setString(4, cmbGender.getSelectedItem().toString());
            stmt.executeUpdate();
            loadTable();
            clearForm();
        } catch (NumberFormatException e) {
            showError("ID harus berupa angka!");
        } catch (SQLException e) {
            showError("Tambah data gagal:\n" + e.getMessage());
        }
    }

    private void updateData() {
        int row = tblHero.getSelectedRow();
        if (row == -1) return;
        int id = (int) tableModel.getValueAt(row, 0);

        try (Connection conn = connect()) {
            String sql = "UPDATE tm_hero SET nama_hero=?, kategori=?, gender=? WHERE id_hero=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNama.getText());
            stmt.setString(2, cmbKategori.getSelectedItem().toString());
            stmt.setString(3, cmbGender.getSelectedItem().toString());
            stmt.setInt(4, id);
            stmt.executeUpdate();
            loadTable();
            clearForm();
        } catch (SQLException e) {
            showError("Update data gagal:\n" + e.getMessage());
        }
    }

    private void hapusData() {
        int row = tblHero.getSelectedRow();
        if (row == -1) return;
        int id = (int) tableModel.getValueAt(row, 0);

        try (Connection conn = connect()) {
            String sql = "DELETE FROM tm_hero WHERE id_hero=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            loadTable();
            clearForm();
        } catch (SQLException e) {
            showError("Hapus data gagal:\n" + e.getMessage());
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtNama.setText("");
        cmbKategori.setSelectedIndex(0);
        cmbGender.setSelectedIndex(0);
        tblHero.clearSelection();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
