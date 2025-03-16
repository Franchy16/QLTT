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
import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.text.MaskFormatter;
import qltt.Entity.SinhVien;
import qltt.Service.DBConnection;

/**
 *
 * @author admin
 */
public class SinhVienGUI extends JFrame {

    private DefaultTableModel model;
    private JTable tableSV;
    private JTextField txtId, txtName, txtClass, txtAddress, txtSearch;
    private JFormattedTextField txtDate, txtPhone;
    private JRadioButton rbMale, rbFemale;
    private ButtonGroup genderGroup;
//    private JDateChooser dateChooser;
    private JButton btnAdd, btnEdit, btnDelete, btnSearch, btnClear;
    private Connection con = null;
    private ArrayList<SinhVien> sinhvienList = new ArrayList<>();
    private JScrollPane scrollPane;
    private ArrayList<String> listMaSV = new ArrayList<>();

    public SinhVienGUI() throws SQLException {
        setTitle("Quản lý Sinh Viên");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        JPanel buttonPanel = new JPanel(new FlowLayout());

        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));

        inputPanel.add(new JLabel("Mã SV:"));
        txtId = new JTextField();
        inputPanel.add(txtId);

        inputPanel.add(new JLabel("Tên SV:"));
        txtName = new JTextField();
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("Lớp:"));
        txtClass = new JTextField();
        inputPanel.add(txtClass);

        inputPanel.add(new JLabel("Giới tính:"));
        rbMale = new JRadioButton("Nam");
        rbFemale = new JRadioButton("Nữ");
        genderGroup = new ButtonGroup();
        genderGroup.add(rbMale);
        genderGroup.add(rbFemale);
        JPanel genderPanel = new JPanel();
        genderPanel.add(rbMale);
        genderPanel.add(rbFemale);
        inputPanel.add(genderPanel);

        inputPanel.add(new JLabel("Địa chỉ:"));
        txtAddress = new JTextField();
        inputPanel.add(txtAddress);

        inputPanel.add(new JLabel("Số điện thoại:"));
        MaskFormatter maskPhoneFormatter = null;
        try {
            maskPhoneFormatter = new MaskFormatter("##########");
            maskPhoneFormatter.setValidCharacters("0123456789");  // Chỉ cho phép nhập số

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        txtPhone = new JFormattedTextField(maskPhoneFormatter);
        txtPhone.setFocusLostBehavior(JFormattedTextField.PERSIST);
        txtPhone.setHorizontalAlignment(JTextField.CENTER);
        inputPanel.add(txtPhone);

        inputPanel.add(new JLabel("Năm sinh:"));
        MaskFormatter maskDateFormatter = null;
        try {
            // Định dạng ngày tháng: ##/##/####
            maskDateFormatter = new MaskFormatter("##/##/####");
            maskDateFormatter.setPlaceholderCharacter(' ');  // Đặt ký tự thay thế là khoảng trắng
            maskDateFormatter.setValidCharacters("0123456789");  // Chỉ cho phép nhập số
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        txtDate = new JFormattedTextField(maskDateFormatter);
        txtDate.setColumns(10);
        txtDate.setFocusLostBehavior(JFormattedTextField.PERSIST);
        txtDate.setHorizontalAlignment(JTextField.CENTER);
        inputPanel.add(txtDate);
        panel.add(inputPanel, BorderLayout.NORTH);

        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnSearch = new JButton("Tìm kiếm");
        btnClear = new JButton("Clear");

        btnAdd.setBackground(Color.GREEN);
        btnEdit.setBackground(Color.YELLOW);
        btnDelete.setBackground(Color.RED);
        btnSearch.setBackground(Color.CYAN);
        btnClear.setBackground(Color.GRAY);

        btnAdd.setForeground(Color.WHITE);
        btnEdit.setForeground(Color.BLACK);
        btnDelete.setForeground(Color.WHITE);
        btnSearch.setForeground(Color.BLACK);
        btnClear.setForeground(Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnClear);

        panel.add(buttonPanel, BorderLayout.CENTER);

        tableSV = new JTable();
        scrollPane = new JScrollPane(tableSV);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        clearFields();
        btnAdd.addActionListener(e -> {
            try {
                addStudent();
            } catch (SQLException ex) {
                Logger.getLogger(SinhVienGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnEdit.addActionListener(e -> {
            try {
                editStudent();
            } catch (SQLException ex) {
                Logger.getLogger(SinhVienGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnDelete.addActionListener(e -> {
            try {
                deleteStudent();
            } catch (SQLException ex) {
                Logger.getLogger(SinhVienGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnSearch.addActionListener(e -> searchStudent());
        btnClear.addActionListener(e -> {
            try {
                clearFields();
            } catch (SQLException ex) {
                Logger.getLogger(SinhVienGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                goBack();
            }
        });
    }

    private ArrayList<SinhVien> loadStudent() throws SQLException {
        ArrayList<SinhVien> listSinhVien = new ArrayList<>();
        ArrayList<String> maSV = new ArrayList<>();
        try {
            con = DBConnection.createConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select masv, tensv, lop, gioitinh, diachi, sdt, namsinh from SinhVien");
            if (Objects.isNull(rs)) {
                return new ArrayList<>();
            }
            while (rs.next()) {
                SinhVien sv = new SinhVien();
                sv.setMasv(rs.getString("masv"));
                sv.setTensv(rs.getString("tensv"));
                sv.setLop(rs.getString("lop"));
                sv.setDiachi(rs.getString("diachi"));
                sv.setGioitinh(rs.getInt("gioitinh"));
                sv.setNamsinh(rs.getString("namsinh"));
                sv.setSdt(rs.getString("sdt"));

                listSinhVien.add(sv);
                maSV.add(rs.getString("masv"));
            }

        } catch (SQLException e) {
            System.err.print(e);
        } finally {
            if (con != null) {
                con.close();
            }
        }
        listMaSV = maSV;
        return listSinhVien;
    }

    private void addStudent() throws SQLException {
        if (!validateFields()) {
            return;
        }
        if (listMaSV.contains(txtId.getText())) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên bị trùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            con = DBConnection.createConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select next_value from SinhVien_SEQ");
            System.out.println(rs);
            while (rs.next()) {
                int nextId = rs.getInt("next_value");
                PreparedStatement ps = con.prepareStatement("update SinhVien_SEQ set last_value = ?");
                ps.setInt(1, nextId);
                ps.executeUpdate();
                if (ps != null) {
                    ps.close();
                }

                ps = con.prepareStatement("insert into SinhVien values (?, ?, ?, ?, ?, ?, ?,?)");
                ps.setInt(1, nextId);
                ps.setString(2, txtId.getText());
                ps.setString(3, txtName.getText());
                ps.setString(4, txtClass.getText());
                int gender;
                if (rbMale.isSelected()) {
                    gender = 1;
                } else {
                    gender = 2;
                }

                ps.setInt(5, gender);
                ps.setString(6, txtAddress.getText());
                ps.setString(7, txtPhone.getText());
                ps.setString(8, txtDate.getText());
                int row = ps.executeUpdate();

                if (row > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công.");
                    displayStudent();
                    clearFields();
                    ps.close();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm sinh viên thất bại.");
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

        clearFields();
    }

    private void goBack() {
        dispose();
        new HomeGUI().setVisible(true);
    }

    private void editStudent() throws SQLException {
        int selectedRow = tableSV.getSelectedRow();
        if (selectedRow != -1) {
            if (!validateFields()) {
                return;
            }
            try {
                con = DBConnection.createConnection();
                PreparedStatement ps = con.prepareStatement(
                        "update SinhVien set tensv = ?, lop = ?, gioitinh = ?, diachi = ?, sdt = ?, namsinh = ? where masv = ?");
                ps.setString(1, txtName.getText());
                ps.setString(2, txtClass.getText());
                ps.setInt(3, rbMale.isSelected() ? 1 : 2);
                ps.setString(4, txtAddress.getText());
                ps.setString(5, txtPhone.getText());
                ps.setString(6, txtDate.getText());
                ps.setString(7, txtId.getText());
                int row = ps.executeUpdate();
                if (row > 0) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công sinh viên.");
                    clearFields();
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
    }

    private void deleteStudent() throws SQLException {
        int selectedRow = tableSV.getSelectedRow();
        if (selectedRow != -1) {
            con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement("delete from SinhVien where masv = ?");
            ps.setString(1, txtId.getText());
            int row = ps.executeUpdate();
            if (row > 0) {
                JOptionPane.showMessageDialog(this, "Xóa thành công sinh viên");
                clearFields();
                ps.close();
            }

            model.removeRow(selectedRow);
        }
    }

    private void searchStudent() {
        String masv = txtId.getText();
        String tensv = txtName.getText();
        String lopsv = txtClass.getText();

        int gioitinh = -1;
        boolean searchByGender = false;
        if (rbMale.isSelected()) {
            gioitinh = 1;
            searchByGender = true;
        } else if (rbFemale.isSelected()) {
            gioitinh = 0;
            searchByGender = true;
        }
        String diachi = txtAddress.getText();
        String sdt = txtPhone.getText();
        String namSinh = txtDate.getText();

        if (masv.isEmpty() && tensv.isEmpty() && lopsv.isEmpty() && searchByGender == false && diachi.isEmpty() && sdt.isEmpty() && namSinh.isEmpty()) {
            return;
        }

        model.setRowCount(0);

        // Duyệt qua tất cả các sinh viên trong danh sách
        for (SinhVien sv : sinhvienList) {
            boolean matchFound = false; // Reset matchFound cho mỗi sinh viên

            // Kiểm tra từng điều kiện tìm kiếm
            if (!masv.isEmpty() && sv.getMasv().equals(masv)) {
                matchFound = true;
            } else {
                matchFound = false;
            }
            if (!tensv.isEmpty() && sv.getTensv().contains(tensv)) {
                matchFound = true;
            } else {
                matchFound = false;
            }
            if (!lopsv.isEmpty() && sv.getLop().equals(lopsv)) {
                matchFound = true;
            } else {
                matchFound = false;
            }
            if (searchByGender && sv.getGioitinh() == gioitinh) {
                matchFound = true;
            } else {
                matchFound = false;
            }
            if (!diachi.isEmpty() && sv.getDiachi().contains(diachi)) {
                matchFound = true;
            } else {
                matchFound = false;
            }
            if (!sdt.isEmpty() && sv.getSdt().contains(sdt)) {
                matchFound = true;
            } else {
                matchFound = false;
            }
            if (!namSinh.isEmpty() && sv.getNamsinh().contains(namSinh)) {
                matchFound = true;
            } else {
                matchFound = false;
            }

            // Nếu tìm thấy khớp, thêm sinh viên vào bảng
            if (matchFound) {
                model.addRow(new Object[]{
                    sv.getMasv(),
                    sv.getTensv(),
                    sv.getLop(),
                    sv.getGioitinh() == 1 ? "Nam" : "Nữ", // Hiển thị "Nam" hoặc "Nữ"
                    sv.getDiachi(),
                    sv.getSdt(),
                    sv.getNamsinh()
                });
            }
        }
    }

    private void clearFields() throws SQLException {
        txtId.setText("");
        txtName.setText("");
        txtClass.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtDate.setText("");
        genderGroup.clearSelection();
        displayStudent();
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new DanhGiaGUI().setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(SinhVienGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void displayStudent() throws SQLException {
        sinhvienList = loadStudent();
        model = new DefaultTableModel(new String[]{"Mã SV", "Tên SV", "Lớp", "Giới tính", "Địa chỉ", "SĐT", "Năm sinh"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tất cả các ô đều không thể chỉnh sửa
            }
        };

        if (!sinhvienList.isEmpty()) {

            for (SinhVien sv : sinhvienList) {
                // Thêm từng sinh viên vào model
                model.addRow(new Object[]{
                    sv.getMasv(),
                    sv.getTensv(),
                    sv.getLop(),
                    sv.getGioitinh() == 1 ? "Nam" : "Nữ", // Hiển thị "Nam" hoặc "Nữ" dựa trên giá trị của gioitinh
                    sv.getDiachi(),
                    sv.getSdt(),
                    sv.getNamsinh()
                });
            }
        }
        tableSV = new JTable(model);
        tableSV.setFillsViewportHeight(true);
        tableSV.setRowHeight(25);
        tableSV.setSelectionBackground(Color.LIGHT_GRAY);
        tableSV.getTableHeader().setBackground(Color.DARK_GRAY);
        tableSV.getTableHeader().setForeground(Color.WHITE);
        tableSV.getSelectionModel().addListSelectionListener(e -> getSelectedRowData());

        scrollPane.setViewportView(tableSV);
        txtId.setEnabled(true);

    }

    private void getSelectedRowData() {
        int selectedRow = tableSV.getSelectedRow();
        if (selectedRow != -1) {
            txtId.setText(model.getValueAt(selectedRow, 0).toString());
            txtName.setText(model.getValueAt(selectedRow, 1).toString());
            txtClass.setText(model.getValueAt(selectedRow, 2).toString());
            if (model.getValueAt(selectedRow, 3).toString().equals("Nam")) {
                rbMale.setSelected(true);
            } else {
                rbFemale.setSelected(true);
            }
            txtAddress.setText(model.getValueAt(selectedRow, 4).toString());
            txtPhone.setText(model.getValueAt(selectedRow, 5).toString());
            txtDate.setText(model.getValueAt(selectedRow, 6).toString());
        }

        txtId.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
    }

    private boolean validateFields() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã sinh viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false; // Nếu mã sinh viên trống thì dừng việc thêm
        }
        if (txtName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sinh viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtClass.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lớp của sinh viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "SDT sinh viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtAddress.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtDate.getText().trim().length() != 10) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ ngày tháng năm sinh của sinh viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidDate(txtDate.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập chuẩn ngày sinh của sinh viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtPhone.getText().trim().length() != 10 || !txtPhone.getText().matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ số điện thoại của sinh viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isValidDate(String date) {
        // Kiểm tra nếu độ dài không đúng (10 ký tự)
        if (date.length() != 10) {
            return false;
        }

        // Kiểm tra nếu ký tự tại các vị trí không phải số
        for (int i = 0; i < date.length(); i++) {
            if (i == 2 || i == 5) {
                // Chỉ chấp nhận dấu '/' tại vị trí 3 và 6 (index 2 và 5)
                if (date.charAt(i) != '/') {
                    return false;
                }
            } else {
                // Kiểm tra nếu ký tự không phải là số
                if (!Character.isDigit(date.charAt(i))) {
                    return false;
                }
            }
        }

        try {
            String[] parts = date.split("/");  // Tách ngày, tháng, năm
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            // Kiểm tra các tháng có số ngày đúng
            if (month < 1 || month > 12) {
                return false;
            }

            // Kiểm tra ngày hợp lệ cho các tháng
            if (month == 2) {
                if (isLeapYear(year)) {
                    if (day < 1 || day > 29) {
                        return false;
                    }
                } else {
                    if (day < 1 || day > 28) {
                        return false;
                    }
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day < 1 || day > 30) {
                    return false;
                }
            } else {
                if (day < 1 || day > 31) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false; // Nếu có lỗi, ngày không hợp lệ
        }
        return true;
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
