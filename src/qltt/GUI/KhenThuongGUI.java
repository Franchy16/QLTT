/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qltt.GUI;

import com.microsoft.sqlserver.jdbc.StringUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import qltt.Entity.*;
import qltt.Service.DBConnection;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author admin
 */
public class KhenThuongGUI extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JTextField txtTenSV, txtLop, txtMaKT, txtKetquadanhgia;
    private JTextField txtDiemtb, txtNamhoc;
    private JComboBox<String> cbMaSV, cbKetquadanhgia;
    private Connection con;
    private JButton addBtn, editBtn, deleteBtn, clearBtn;
    private int selectedID;

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

    public KhenThuongGUI() throws SQLException {
        setTitle("Danh sách khen thưởng");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1, 10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel infoSVPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        JPanel subPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        JPanel infoDiemPanel = new JPanel(new GridLayout(6, 1, 5, 5));

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 5, 5));

        infoSVPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));

        infoSVPanel.add(new JLabel("Mã SV:"));
        cbMaSV = new JComboBox<>();
        String placeHolder = "---Chọn mã sinh viên---";
        cbMaSV.addItem(placeHolder);
        for (SinhVien sinhVien : loadListSinhVien()) {
            cbMaSV.addItem(sinhVien.getMasv());
        }

        infoSVPanel.add(cbMaSV);

        infoSVPanel.add(new JLabel("Tên SV:"));
        txtTenSV = new JTextField();
        txtTenSV.setFocusable(false);
        txtTenSV.setEditable(false);
        infoSVPanel.add(txtTenSV);

        infoSVPanel.add(new JLabel("Lớp:"));
        txtLop = new JTextField();
        txtLop.setFocusable(false);
        txtLop.setEditable(false);
        infoSVPanel.add(txtLop);

        cbMaSV.addActionListener(e -> {
            if (placeHolder.equals(cbMaSV.getSelectedItem().toString())) {
                txtTenSV.setText(StringUtils.EMPTY);
                txtLop.setText(StringUtils.EMPTY);
                txtNamhoc.setEditable(false);
                txtNamhoc.setFocusable(false);
                return;
            }
            try {
                SinhVien sv = loadSinhVienByMaSV(cbMaSV.getSelectedItem().toString());
                txtTenSV.setText(sv.getTensv());
                txtLop.setText(sv.getLop());
                txtNamhoc.setEditable(true);
                txtNamhoc.setFocusable(true);
            } catch (SQLException ex) {
                Logger.getLogger(KhenThuongGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        infoDiemPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khen thưởng"));

        infoDiemPanel.add(new JLabel("Mã khen thưởng"));
        txtMaKT = new JTextField();
        infoDiemPanel.add(txtMaKT);

        infoDiemPanel.add(new JLabel("Năm học:"));
        txtNamhoc = new JTextField();
        txtNamhoc.setEditable(false);
        txtNamhoc.setFocusable(false);
        infoDiemPanel.add(txtNamhoc);

        txtNamhoc.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                txtKetquadanhgia.setText(getAvgRankByYear(Integer.parseInt(txtNamhoc.getText()), cbMaSV.getSelectedItem().toString()));
                txtDiemtb.setText(getAvgPointByYear(Integer.parseInt(txtNamhoc.getText()), cbMaSV.getSelectedItem().toString()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                txtKetquadanhgia.setText(getAvgRankByYear(Integer.parseInt(txtNamhoc.getText()), cbMaSV.getSelectedItem().toString()));
                txtDiemtb.setText(getAvgPointByYear(Integer.parseInt(txtNamhoc.getText()), cbMaSV.getSelectedItem().toString()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                txtKetquadanhgia.setText(getAvgRankByYear(Integer.parseInt(txtNamhoc.getText()), cbMaSV.getSelectedItem().toString()));
                txtDiemtb.setText(getAvgPointByYear(Integer.parseInt(txtNamhoc.getText()), cbMaSV.getSelectedItem().toString()));
            }
        });

        infoDiemPanel.add(new JLabel("Điểm (GPA):"));
        txtDiemtb = new JTextField();
        txtDiemtb.setFocusable(false);
        txtDiemtb.setEditable(false);
        infoDiemPanel.add(txtDiemtb);

        infoDiemPanel.add(new JLabel("Đánh giá:"));
        txtKetquadanhgia = new JTextField();
        txtKetquadanhgia.setFocusable(false);
        txtKetquadanhgia.setEditable(false);
        infoDiemPanel.add(txtKetquadanhgia);

        infoDiemPanel.add(new JLabel("Danh hiệu:"));
        String[] attitudes = {"Không có danh hiệu", "Giỏi", "Xuất sắc"};
        cbKetquadanhgia = new JComboBox<>(attitudes);
        cbKetquadanhgia.setEnabled(false);
        cbKetquadanhgia.setFocusable(false);
        infoDiemPanel.add(cbKetquadanhgia);

        addBtn = new JButton("Thêm");
        editBtn = new JButton("Sửa");
        deleteBtn = new JButton("Xóa");
        clearBtn = new JButton("Clear");

        addBtn.setBackground(Color.GREEN);
        editBtn.setBackground(Color.YELLOW);
        deleteBtn.setBackground(Color.RED);

        addBtn.setForeground(Color.WHITE);
        editBtn.setForeground(Color.BLACK);
        deleteBtn.setForeground(Color.WHITE);

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);

        addBtn.addActionListener(e -> {
            try {
                Add();
            } catch (SQLException ex) {
                Logger.getLogger(DanhGiaGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(KhenThuongGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        editBtn.addActionListener(e -> {
            try {
                Update(selectedID);
            } catch (SQLException ex) {
                Logger.getLogger(DanhGiaGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                Delete(selectedID);
            } catch (SQLException ex) {
                Logger.getLogger(DanhGiaGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        clearBtn.addActionListener(e -> clearFields());

        model = new DefaultTableModel(new String[]{"ID", "Mã KT", "Mã SV", "Tên SV", "Lớp", "Điểm", "Danh hiệu", "Năm học"}, 0);
        table = new JTable(model);
        table.getSelectionModel().addListSelectionListener(e -> getSelectedRowData());
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setSelectionBackground(Color.LIGHT_GRAY);
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setViewportView(table);

        panel.add(infoSVPanel);
        subPanel.add(infoDiemPanel);
        panel.add(subPanel);
        mainPanel.add(panel, BorderLayout.NORTH);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        add(mainPanel);
        add(scrollPane);

        LoadData();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                goBack();
            }
        });
    }

    private float FormatDiem(String dg) {
        if (dg == null || "".equals(dg)) {
            return 0;
        }
        switch (dg) {
            case "A" -> {
                return 4.0f;
            }
            case "B" -> {
                return 3.0f;
            }
            case "C" -> {
                return 2.0f;
            }
            case "D" -> {
                return 1.0f;
            }
            default -> {
                return 0;
            }
        }
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

    private String FormatDiemSo(int diem) {
        switch (diem) {
            case 4 -> {
                return "Tốt";
            }
            case 3 -> {
                return "Khá";
            }
            case 2 -> {
                return "Trung bình";
            }
            case 1 -> {
                return "Yếu";
            }
            default -> {
                return "";
            }
        }
    }

    private String getAvgPointByYear(int year, String masv) {
        ArrayList<String> listPoints = new ArrayList<>();
        float avg = 0.0f;
        try {
            con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement("select diemtb from Diem where namhoc = ? and masv = ?");
            ps.setInt(1, year);
            ps.setString(2, masv);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                listPoints.add(rs.getString("diemtb"));
            }

            if (!listPoints.isEmpty()) {
                for (String point : listPoints) {
                    avg += FormatDiem(point);
                }
            }

            cbKetquadanhgia.setSelectedIndex(calculateDanhHieu(avg, txtKetquadanhgia.getText()));

            return String.valueOf(avg / listPoints.size() * 10 / 4);
        } catch (SQLException e) {
            System.err.print(e);
            return "0";
        }
    }

    private String getAvgRankByYear(int year, String masv) {
        ArrayList<String> listPoints = new ArrayList<>();
        float avg = 0.0f;
        try {
            con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement("select ketquadanhgia from DanhGia where namhoc = ? and masv = ?");
            ps.setInt(1, year);
            ps.setString(2, masv);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                listPoints.add(rs.getString("ketquadanhgia"));
            }

            if (!listPoints.isEmpty()) {
                for (String point : listPoints) {
                    avg += FormatDanhGia(point);
                }
            }
            return FormatDiemSo(Math.round(avg / listPoints.size()));
        } catch (SQLException e) {
            System.err.print(e);
            return "0";
        }
    }

    private int calculateDanhHieu(float diem, String danhgia) {
        if (danhgia.equals("Tốt")) {
            if (diem >= 8.5f) {
                return 2;
            } else if (diem >= 7.0f) {
                return 1;
            }
            return 0;
        } else {
            return 0;
        }
    }

    private void goBack() {
        dispose();
        new HomeGUI().setVisible(true);
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

    private void LoadData() throws SQLException {
        model.setRowCount(0);
        try {
            con = DBConnection.createConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, makt, masv, diem, danhhieu, namhoc from KhenThuong");
            while (rs.next()) {
                KhenThuong dg = new KhenThuong();
                dg.setId(rs.getInt("id"));
                dg.setMakt(rs.getString("makt"));
                dg.setMasv(rs.getString("masv"));
                dg.setDiem(rs.getFloat("diem"));
                dg.setNamhoc(rs.getInt("namhoc"));
                dg.setDanhhieu(rs.getString("danhhieu"));

                SinhVien sv = loadSinhVienByMaSV(dg.getMasv());

                model.addRow(new Object[]{
                    dg.getId(),
                    dg.getMakt(),
                    dg.getMasv(),
                    sv.getTensv(),
                    sv.getLop(),
                    dg.getDiem(),
                    dg.getDanhhieu(),
                    dg.getNamhoc()
                });
            }
        } catch (SQLException e) {
            System.err.print(e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    private void Add() throws SQLException, Exception {
        if (cbKetquadanhgia.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Danh hiệu không đạt điều kiện!");
            return;
        }
        validateMaKT(txtMaKT.getText());
        try {
            con = DBConnection.createConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select next_value from KhenThuong_SEQ");
            while (rs.next()) {
                int nextId = rs.getInt("next_value");
                PreparedStatement ps = con.prepareStatement("update KhenThuong_SEQ set last_value = ?");
                ps.setInt(1, nextId);
                ps.executeUpdate();
                if (ps != null) {
                    ps.close();
                }

                ps = con.prepareStatement("insert into KhenThuong values (?, ?, ?, ?, ?, ?, null,null)");
                ps.setInt(1, nextId);
                ps.setString(2, txtMaKT.getText());
                ps.setString(3, cbMaSV.getSelectedItem().toString());
                ps.setFloat(4, Float.parseFloat(txtDiemtb.getText()));
                ps.setString(5, cbKetquadanhgia.getSelectedItem().toString());
                ps.setInt(6, Integer.parseInt(txtNamhoc.getText()));

                int row = ps.executeUpdate();

                if (row > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công khen thưởng");
                    LoadData();
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

    private void validateMaKT(String makt) throws SQLException, Exception {
        try {
            con = DBConnection.createConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id, makt, masv, diem, danhhieu, namhoc from KhenThuong");
            while (rs.next()) {
                if (makt.equals(rs.getString("makt"))) {
                    JOptionPane.showMessageDialog(this, "Mã khen thưởng đã tồn tại!");
                    throw new Exception("Mã khen thưởng đã tồn tại!");
                }

            }
        } catch (SQLException e) {
            System.err.print(e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    private void Update(int id) throws SQLException {
        if (id == -1) {
            return;
        }
        if (cbKetquadanhgia.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Danh hiệu không đạt điều kiện!");
            return;
        }
        try {
            con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement(
                    "update KhenThuong set masv = ?, diem = ?, danhhieu = ?, namhoc = ? where id = ?");
            ps.setString(1, cbMaSV.getSelectedItem().toString());
            ps.setFloat(2, Float.parseFloat(txtDiemtb.getText()));
            ps.setString(3, cbKetquadanhgia.getSelectedItem().toString());
            ps.setInt(4, Integer.parseInt(txtNamhoc.getText()));
            ps.setInt(5, id);
            int row = ps.executeUpdate();
            if (row > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                clearFields();
                LoadData();
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

    private void Delete(int id) throws SQLException {
        if (id == -1) {
            return;
        }

        try {
            con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement("delete from KhenThuong where id = ?");
            ps.setInt(1, id);
            int row = ps.executeUpdate();
            if (row > 0) {
                JOptionPane.showMessageDialog(this, "Xóa thành công đánh giá");
                clearFields();
                LoadData();
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

    private void clearFields() {
        txtMaKT.setText(StringUtils.EMPTY);
        txtDiemtb.setText(StringUtils.EMPTY);
        txtKetquadanhgia.setText(StringUtils.EMPTY);
        txtNamhoc.setText(StringUtils.EMPTY);
        cbKetquadanhgia.setSelectedIndex(0);
        cbMaSV.setSelectedIndex(0);
        selectedID = -1;
        txtMaKT.setEditable(true);
        txtMaKT.setFocusable(true);
    }

    private void getSelectedRowData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            txtMaKT.setText(model.getValueAt(selectedRow, 1).toString());
            cbMaSV.setSelectedItem(model.getValueAt(selectedRow, 2).toString());
            txtNamhoc.setText(model.getValueAt(selectedRow, 7).toString());
            txtDiemtb.setText(model.getValueAt(selectedRow, 5).toString());
            cbKetquadanhgia.setSelectedItem(model.getValueAt(selectedRow, 6).toString());
            selectedID = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
            txtMaKT.setEditable(false);
            txtMaKT.setFocusable(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new KhenThuongGUI().setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(KhenThuongGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
