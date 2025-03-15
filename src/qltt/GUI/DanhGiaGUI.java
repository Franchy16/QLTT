/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qltt.GUI;

import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import qltt.Entity.*;
import qltt.Service.DBConnection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author admin
 */
public class DanhGiaGUI extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JTextField txtPeriod, txtYear, txtSearch, txtPoint, txtResult;
    private JButton btnAdd, btnEdit, btnDelete, btnSearch;
    private JComboBox<String> cbMaSv, cbAttitude;
    private Connection con = null;
    private ArrayList<SinhVien> listSV;
    private ArrayList<DanhGia> listDG;
    private int dgSelectionId = -1;

    private ArrayList<SinhVien> loadListSinhVien() throws SQLException {
        ArrayList<SinhVien> result = new ArrayList<>();
        try {
            con = DBConnection.createConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select masv, tensv, lop from SinhVien");
            while (rs.next()) {
                SinhVien sv = new SinhVien();
                sv.setMasv(rs.getString("masv"));
                sv.setTensv(rs.getString("tensv"));
                sv.setLop(rs.getString("lop"));
                result.add(sv);
            }
        } catch (SQLException e) {
            System.err.print(e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    private SinhVien loadSinhVienByMaSV(String masv) throws SQLException {
        try {
            con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement("select masv, tensv, lop from SinhVien where masv = ?");
            ps.setString(1, masv);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SinhVien sv = new SinhVien();
                sv.setMasv(rs.getString("masv"));
                sv.setTensv(rs.getString("tensv"));
                sv.setLop(rs.getString("lop"));
                return sv;
            }
        } catch (SQLException e) {
            System.err.print(e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    private ArrayList<DanhGia> loadListDanhGia() throws SQLException {
        ArrayList<DanhGia> result = new ArrayList<>();
        model.setRowCount(0);
        try {
            con = DBConnection.createConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, masv, kihoc, namhoc, thaidohoctap, diemrenluyen, ketquadanhgia from DanhGia");
            while (rs.next()) {
                DanhGia dg = new DanhGia();
                dg.setId(rs.getInt("id"));
                dg.setMasv(rs.getString("masv"));
                dg.setKihoc(rs.getInt("kihoc"));
                dg.setNamhoc(rs.getInt("namhoc"));
                dg.setTdht(rs.getString("thaidohoctap"));
                dg.setDrl(rs.getString("diemrenluyen"));
                dg.setKetqua(rs.getString("ketquadanhgia"));

                SinhVien sv = loadSinhVienByMaSV(dg.getMasv());

                model.addRow(new Object[]{
                    dg.getId(),
                    dg.getMasv(),
                    sv.getTensv(),
                    sv.getLop(),
                    dg.getTdht(),
                    dg.getDrl(),
                    dg.getKetqua(),
                    dg.getKihoc(),
                    dg.getNamhoc()
                });

                result.add(dg);
            }
        } catch (SQLException e) {
            System.err.print(e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    private void loadListDanhGiaByMSV(String masv) throws SQLException {
        System.out.print(masv);
        if ("".equals(masv)) {
            loadListDanhGia();
        } else {
            ArrayList<DanhGia> result = new ArrayList<>();
            model.setRowCount(0);
            try {
                con = DBConnection.createConnection();
                PreparedStatement ps = con.prepareStatement("select id, masv, kihoc, namhoc, thaidohoctap, diemrenluyen, ketquadanhgia from DanhGia where masv = ?");
                ps.setString(1, masv);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    DanhGia dg = new DanhGia();
                    dg.setId(rs.getInt("id"));
                    dg.setMasv(rs.getString("masv"));
                    dg.setKihoc(rs.getInt("kihoc"));
                    dg.setNamhoc(rs.getInt("namhoc"));
                    dg.setTdht(rs.getString("thaidohoctap"));
                    dg.setDrl(rs.getString("diemrenluyen"));
                    dg.setKetqua(rs.getString("ketquadanhgia"));

                    SinhVien sv = loadSinhVienByMaSV(dg.getMasv());

                    model.addRow(new Object[]{
                        dg.getId(),
                        dg.getMasv(),
                        sv.getTensv(),
                        sv.getLop(),
                        dg.getTdht(),
                        dg.getDrl(),
                        dg.getKetqua(),
                        dg.getKihoc(),
                        dg.getNamhoc()
                    });

                    result.add(dg);
                }
            } catch (SQLException e) {
                System.err.print(e);
            } finally {
                if (con != null) {
                    con.close();
                }
            }
        }
    }

    private void AddDanhGia() throws SQLException {
        try {
            con = DBConnection.createConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select next_value from DanhGia_SEQ");
            while (rs.next()) {
                int nextId = rs.getInt("next_value");
                PreparedStatement ps = con.prepareStatement("update DanhGia_SEQ set last_value = ?");
                ps.setInt(1, nextId);
                ps.executeUpdate();
                if (ps != null) {
                    ps.close();
                }

                ps = con.prepareStatement("insert into DanhGia values (?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, nextId);
                ps.setString(2, cbMaSv.getSelectedItem().toString());
                ps.setInt(3, Integer.parseInt(txtPeriod.getText()));
                ps.setInt(4, Integer.parseInt(txtYear.getText()));
                ps.setString(5, cbAttitude.getSelectedItem().toString());
                ps.setString(6, txtPoint.getText());
                ps.setString(7, txtResult.getText());

                int row = ps.executeUpdate();

                if (row > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công đánh giá");
                    listDG = loadListDanhGia();
                    clearFields();
                    ps.close();
                }
                break;
            }

            if (rs != null) {
                rs.close();
            }

            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            System.err.print(e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    private void CapNhatDanhGia(int id) throws SQLException {
        if (dgSelectionId == -1) {
            return;
        }
        try {
            con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement(
                    "update DanhGia set masv = ?, kihoc = ?, namhoc = ?, thaidohoctap = ?, diemrenluyen = ?, ketquadanhgia = ? where id = ?");
            ps.setString(1, cbMaSv.getSelectedItem().toString());
            ps.setInt(2, Integer.parseInt(txtPeriod.getText()));
            ps.setInt(3, Integer.parseInt(txtYear.getText()));
            ps.setString(4, cbAttitude.getSelectedItem().toString());
            ps.setString(5, txtPoint.getText());
            ps.setString(6, txtResult.getText());
            ps.setInt(7, id);
            int row = ps.executeUpdate();
            if (row > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công đánh giá");
                clearFields();
                listDG = loadListDanhGia();
                ps.close();
            }
        } catch (SQLException e) {
            System.err.print(e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    private void XoaDanhGia(int id) throws SQLException {
        if (dgSelectionId == -1) {
            return;
        }
        try {
            con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement("delete from DanhGia where id = ?");
            ps.setInt(1, id);
            int row = ps.executeUpdate();
            if (row > 0) {
                JOptionPane.showMessageDialog(this, "Xóa thành công đánh giá");
                clearFields();
                listDG = loadListDanhGia();
                ps.close();
            }
        } catch (SQLException e) {
            System.err.print(e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public DanhGiaGUI() throws SQLException {
        setTitle("Đánh giá Sinh Viên");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        JPanel buttonPanel = new JPanel(new FlowLayout());

        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đánh giá"));

        inputPanel.add(new JLabel("Mã SV:"));

        listSV = loadListSinhVien();
        cbMaSv = new JComboBox<>();
        for (SinhVien sinhVien : listSV) {
            cbMaSv.addItem(sinhVien.getMasv());
        }

        inputPanel.add(cbMaSv);

        inputPanel.add(new JLabel("Kì học:"));
        txtPeriod = new JTextField();
        inputPanel.add(txtPeriod);

        inputPanel.add(new JLabel("Năm học:"));
        txtYear = new JTextField();
        inputPanel.add(txtYear);

        inputPanel.add(new JLabel("Thái độ học tập:"));
        String[] attitudes = {"Tốt", "Khá", "Trung bình", "Kém"};
        cbAttitude = new JComboBox<>(attitudes);
        cbAttitude.addActionListener(e -> {
            calculateResult(txtPoint.getText(), cbAttitude.getSelectedItem().toString());
        });
        inputPanel.add(cbAttitude);

        inputPanel.add(new JLabel("Điểm rèn luyện:"));
        txtPoint = new JTextField();
        txtPoint.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateResult(txtPoint.getText(), cbAttitude.getSelectedItem().toString());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateResult(txtPoint.getText(), cbAttitude.getSelectedItem().toString());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateResult(txtPoint.getText(), cbAttitude.getSelectedItem().toString());
            }

        });
        inputPanel.add(txtPoint);

        inputPanel.add(new JLabel("Kết quả đánh giá:"));
        txtResult = new JTextField();
        txtResult.setFocusable(false);
        txtResult.setEditable(false);
        inputPanel.add(txtResult);

        inputPanel.add(new JLabel("Tìm kiếm:"));
        txtSearch = new JTextField();
        inputPanel.add(txtSearch);

        panel.add(inputPanel, BorderLayout.NORTH);

        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnSearch = new JButton("Tìm kiếm");

        btnAdd.setBackground(Color.GREEN);
        btnEdit.setBackground(Color.YELLOW);
        btnDelete.setBackground(Color.RED);
        btnSearch.setBackground(Color.CYAN);

        btnAdd.setForeground(Color.WHITE);
        btnEdit.setForeground(Color.BLACK);
        btnDelete.setForeground(Color.WHITE);
        btnSearch.setForeground(Color.BLACK);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);
        panel.add(buttonPanel, BorderLayout.CENTER);

        model = new DefaultTableModel(new String[]{"ID", "Mã SV", "Tên SV", "Lớp", "Thái độ học tập", "Điểm rèn luyện", "Kết quả đánh giá", "Kì học", "Năm học"}, 0);
        listDG = loadListDanhGia();
        table = new JTable(model);
        table.getSelectionModel().addListSelectionListener(e -> getSelectedRowData());
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setSelectionBackground(Color.LIGHT_GRAY);
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            try {
                AddDanhGia();
            } catch (SQLException ex) {
                Logger.getLogger(DanhGiaGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnEdit.addActionListener(e -> {
            try {
                CapNhatDanhGia(dgSelectionId);
            } catch (SQLException ex) {
                Logger.getLogger(DanhGiaGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnDelete.addActionListener(e -> {
            try {
                XoaDanhGia(dgSelectionId);
            } catch (SQLException ex) {
                Logger.getLogger(DanhGiaGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnSearch.addActionListener(e -> {
            try {
                loadListDanhGiaByMSV(txtSearch.getText());
            } catch (SQLException ex) {
                Logger.getLogger(DanhGiaGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                goBack();
            }
        });
    }

    private String calculateDrl(String drl) {
        try {
            int _drl = Integer.parseInt(drl);
            if (_drl >= 85) {
                return "Tốt";
            } else if (_drl >= 70) {
                return "Khá";
            } else if (_drl >= 55) {
                return "Trung bình";
            } else {
                return "Yếu";
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void calculateResult(String drl, String attitude) {
        String _drl = calculateDrl(drl);
        int ketqua = FormatDanhGia(_drl) + FormatDanhGia(attitude);
        String textResult = "";
        switch (Math.round(ketqua / 2)) {
            case 4 -> {
                textResult = "Tốt";
            }
            case 3 -> {
                textResult = "Khá";
            }
            case 2 -> {
                textResult = "Trung bình";
            }
            case 1 -> {
                textResult = "Yếu";
            }
            default -> {
                textResult = "";
            }
        }
        txtResult.setText(FormatDanhGia(_drl) == 0 ? "" : textResult);
    }

    private int FormatDanhGia(String dg) {
        if (dg == null || "".equals(dg)) {
            return 0;
        }
        switch (dg) {
            case "Tốt" -> {
                return 4;
            }
            case "Khá" -> {
                return 3;
            }
            case "Trung bình" -> {
                return 2;
            }
            case "Yếu" -> {
                return 1;
            }
            default -> {
                return 0;
            }
        }
    }

    private void addStudent() {
        clearFields();
    }

    private void goBack() {
        dispose();
        new HomeGUI().setVisible(true);
    }

    private void getSelectedRowData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            cbMaSv.setSelectedItem(model.getValueAt(selectedRow, 1).toString());
            txtPeriod.setText(model.getValueAt(selectedRow, 7).toString());
            txtYear.setText(model.getValueAt(selectedRow, 8).toString());
            cbAttitude.setSelectedItem(model.getValueAt(selectedRow, 4).toString());
            txtPoint.setText(model.getValueAt(selectedRow, 5).toString());
            txtResult.setText(model.getValueAt(selectedRow, 6).toString());
            dgSelectionId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());

        }
    }

    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        }
    }

    private void searchStudent() {
//        String keyword = txtSearch.getText().toLowerCase();
//        for (int i = 0; i < table.getRowCount(); i++) {
//            if (table.getValueAt(i, 1).toString().toLowerCase().contains(keyword)) {
//                table.setRowSelectionInterval(i, i);
//                break;
//            }
//        }
    }

    private void clearFields() {
        txtPeriod.setText("");
        txtPoint.setText("");
        txtResult.setText("");
        txtYear.setText("");
        cbAttitude.setSelectedIndex(0);
        cbMaSv.setSelectedItem(0);
        dgSelectionId = -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new DanhGiaGUI().setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(DanhGiaGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
