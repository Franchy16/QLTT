/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qltt.GUI;

import qltt.Entity.Diem;
import com.microsoft.sqlserver.jdbc.StringUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import qltt.Entity.MonHoc;
import qltt.Entity.SinhVien;
import qltt.Service.DBConnection;
import qltt.Entity.Diem;

/**
 *
 * @author admin
 */
public class DiemGUI extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JTextField txtTenSV, txtLop, txtTenMH, txtSoTinChi;
    private JTextField txtDiemtb, txtNamhoc, txtDiemcc, txtDieml1, txtDieml2, txtDieml3, txtDieml4, txtDiemck, txtKihoc;
    private JComboBox<String> cbMaSV, cbKetquadanhgia, cbMaMH;
    private Connection con;
    private JButton addBtn, editBtn, deleteBtn, clearBtn;
    private int selectedID;
    private ArrayList<Diem> currentGrades = new ArrayList<>();

    public DiemGUI() throws SQLException {
        setTitle("Quản lý điểm");
        setSize(1280, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1, 10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        JPanel infoSVPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        JPanel subPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        JPanel infoDiemPanel = new JPanel(new GridLayout(9, 2, 5, 5));
        JPanel infoMHPanel = new JPanel(new GridLayout(6, 1, 5, 5));

        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 5, 5));

        infoSVPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));

        infoMHPanel.setBorder(BorderFactory.createTitledBorder("Thông tin môn học"));

        infoSVPanel.add(new JLabel("Mã SV:"));
        cbMaSV = new JComboBox<>();
        String placeHolder = "---Chọn mã sinh viên---";
        cbMaSV.addItem(placeHolder);
        for (SinhVien sinhVien : loadListSinhVien()) {
            cbMaSV.addItem(sinhVien.getMasv());
        }

        infoSVPanel.add(cbMaSV);

        infoMHPanel.add(new JLabel("Mã MH:"));
        cbMaMH = new JComboBox<>();
        String placeHolderSelectMH = "---Chọn mã môn học---";
        cbMaMH.addItem(placeHolderSelectMH);
        for (MonHoc monhoc : loadListMonHoc()) {
            cbMaMH.addItem(monhoc.getMamh());
        }

        infoMHPanel.add(cbMaMH);

        infoSVPanel.add(new JLabel("Tên SV:"));
        txtTenSV = new JTextField();
        txtTenSV.setFocusable(false);
        txtTenSV.setEditable(false);
        infoSVPanel.add(txtTenSV);

        infoMHPanel.add(new JLabel("Tên Môn học:"));
        txtTenMH = new JTextField();
        txtTenMH.setFocusable(false);
        txtTenMH.setEditable(false);
        infoMHPanel.add(txtTenMH);

        infoSVPanel.add(new JLabel("Lớp:"));
        txtLop = new JTextField();
        txtLop.setFocusable(false);
        txtLop.setEditable(false);
        infoSVPanel.add(txtLop);

        infoMHPanel.add(new JLabel("Số tín chỉ:"));
        txtSoTinChi = new JTextField();
        txtSoTinChi.setFocusable(false);
        txtSoTinChi.setEditable(false);
        infoMHPanel.add(txtSoTinChi);

        cbMaSV.addActionListener(e -> {
            if (placeHolder.equals(cbMaSV.getSelectedItem().toString())) {
                txtTenSV.setText(StringUtils.EMPTY);
                txtLop.setText(StringUtils.EMPTY);
                return;
            }
            try {
                SinhVien sv = loadSinhVienByMaSV(cbMaSV.getSelectedItem().toString());
                txtTenSV.setText(sv.getTensv());
                txtLop.setText(sv.getLop());
            } catch (SQLException ex) {
                Logger.getLogger(KhenThuongGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        cbMaMH.addActionListener(e -> {
            clearGradeFields();
            if (placeHolderSelectMH.equals(cbMaMH.getSelectedItem().toString())) {
                txtTenMH.setText(StringUtils.EMPTY);
                txtSoTinChi.setText(StringUtils.EMPTY);
                return;
            }
            try {
                MonHoc mh = loadMonHocByMaMH(cbMaMH.getSelectedItem().toString());
                txtTenMH.setText(mh.getTenmh());
                int stc = mh.getSotinchi();
                txtSoTinChi.setText(String.valueOf(stc));
                if (stc >= 1) {
                    txtDieml1.setEnabled(true);
                    if (stc >= 2) {
                        txtDieml2.setEnabled(true);
                        if (stc >= 3) {
                            txtDieml3.setEnabled(true);
                            if (stc == 4) {
                                txtDieml4.setEnabled(true);
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(KhenThuongGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        infoDiemPanel.setBorder(BorderFactory.createTitledBorder("Thông tin điểm"));

        infoDiemPanel.add(new JLabel("Điểm CC:"));
        txtDiemcc = new JTextField();
        txtDiemcc.addKeyListener(new NumberKeyListener(txtDiemcc));
        infoDiemPanel.add(txtDiemcc);

        infoDiemPanel.add(new JLabel("Điểm 1:"));
        txtDieml1 = new JTextField();
        txtDieml1.addKeyListener(new NumberKeyListener(txtDieml1));
        infoDiemPanel.add(txtDieml1);

        infoDiemPanel.add(new JLabel("Điểm 2:"));
        txtDieml2 = new JTextField();
        txtDieml2.addKeyListener(new NumberKeyListener(txtDieml2));
        infoDiemPanel.add(txtDieml2);

        infoDiemPanel.add(new JLabel("Điểm 3:"));
        txtDieml3 = new JTextField();
        txtDieml3.addKeyListener(
                new NumberKeyListener(txtDieml3));
        infoDiemPanel.add(txtDieml3);

        infoDiemPanel.add(new JLabel("Điểm 4:"));
        txtDieml4 = new JTextField();
        txtDieml4.addKeyListener(new NumberKeyListener(txtDieml4));
        infoDiemPanel.add(txtDieml4);

        infoDiemPanel.add(new JLabel("Điểm CK:"));
        txtDiemck = new JTextField();
        txtDiemck.addKeyListener(new NumberKeyListener(txtDiemck));
        infoDiemPanel.add(txtDiemck);

        infoDiemPanel.add(new JLabel("Điểm TB:"));
        txtDiemtb = new JTextField();
        infoDiemPanel.add(txtDiemtb);

        infoDiemPanel.add(new JLabel("Năm học:"));
        txtNamhoc = new JTextField();
        infoDiemPanel.add(txtNamhoc);

        infoDiemPanel.add(new JLabel("Ki hoc"));
        txtKihoc = new JTextField();
        infoDiemPanel.add(txtKihoc);

        addBtn = new JButton("Thêm");
        editBtn = new JButton("Sửa");
        deleteBtn = new JButton("Xóa");
        clearBtn = new JButton("Clear");

        addBtn.setBackground(Color.GREEN);
        editBtn.setBackground(Color.YELLOW);
        deleteBtn.setBackground(Color.RED);
        clearBtn.setBackground(Color.GRAY);

        addBtn.setForeground(Color.WHITE);
        editBtn.setForeground(Color.BLACK);
        deleteBtn.setForeground(Color.WHITE);
        clearBtn.setForeground(Color.BLACK);

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);
        clearBtn.addActionListener(e -> {
            try {
                clearFields();
            } catch (SQLException ex) {
                Logger.getLogger(DiemGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        addBtn.addActionListener(e -> {
            try {
                Add();
            } catch (SQLException ex) {
                Logger.getLogger(DanhGiaGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        editBtn.addActionListener(e -> {
            try {
                Edit();
            } catch (SQLException ex) {
                Logger.getLogger(DanhGiaGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                Delete();
            } catch (SQLException ex) {
                Logger.getLogger(DanhGiaGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        model = new DefaultTableModel(new String[]{"ID",
            "Mã SV", "Tên SV", "Lớp", "Môn học", "Điểm CC", "Điểm lần 1",
            "Điểm lần 2", "Điểm lần 3", "Điểm lần 4", "Điểm CK", "Điểm TB", "Kì học", "Năm học"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Khong cho edit cac dong
            }
        };
        table = new JTable(model);
//        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        table.setCellSelectionEnabled(false);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setSelectionBackground(Color.LIGHT_GRAY);
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getSelectionModel().addListSelectionListener(e -> getSelectedRowData());

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setViewportView(table);

        panel.add(infoSVPanel);
        panel.add(infoMHPanel);
        panel.add(infoDiemPanel);
        mainPanel.add(panel, BorderLayout.NORTH);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        add(mainPanel);
        add(scrollPane);

        clearFields();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                goBack();
            }
        });
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

    private MonHoc loadMonHocByMaMH(String mamh) throws SQLException {
        try {
            con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement("select mamh, tenmh, sotinchi from MonHoc where mamh = ?");
            ps.setString(1, mamh);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MonHoc mh = new MonHoc();
                mh.setMamh(rs.getString("mamh"));
                mh.setTenmh(rs.getString("tenmh"));
                mh.setSotinchi(rs.getInt("sotinchi"));
                return mh;
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

    public void caculateGrade() {

        int sotinchi = Integer.parseInt(txtSoTinChi.getText());
        float diemcc;
        float dieml1;
        float dieml2;
        float dieml3;
        float dieml4;
        float diemck;
        float diemtb;
        float tongqt = 0;
        String textDiem1 = txtDieml1.getText();
        String textDiem2 = txtDieml1.getText();
        String textDiem3 = txtDieml1.getText();
        String textDiem4 = txtDieml1.getText();

        if (txtDiemcc.getText().isEmpty()) {
            return;
        }
        diemcc = Float.parseFloat(txtDiemcc.getText());
        tongqt += diemcc;
        if (txtDiemck.getText().isEmpty()) {
            return;
        }
        diemck = Float.parseFloat(txtDiemck.getText());

        if (sotinchi >= 1) {
            if (textDiem1.isEmpty()) {
                return;
            }
            dieml1 = Float.parseFloat(txtDieml1.getText());
            tongqt += dieml1;
            if (sotinchi >= 2) {
                if (textDiem2.isEmpty()) {
                    return;
                }
                dieml2 = Float.parseFloat(txtDieml2.getText());
                tongqt += dieml2;

                if (sotinchi >= 3) {
                    if (textDiem3.isEmpty()) {
                        return;
                    }
                    dieml3 = Float.parseFloat(txtDieml3.getText());
                    tongqt += dieml3;
                    if (sotinchi == 4) {
                        if (textDiem4.isEmpty()) {
                            return;
                        }
                        dieml4 = Float.parseFloat(txtDieml4.getText());
                        tongqt += dieml4;
                    }
                }
            }
        }
        String finalValue;
        diemtb = (tongqt / (sotinchi + 1) + diemck * 3) / 4;
        if (diemtb <= 10) {
            finalValue = "A";
            txtDiemtb.setText(finalValue);

        }
        if (diemtb < 8.5) {
            finalValue = "B";
            txtDiemtb.setText(finalValue);

        }
        if (diemtb < 7) {
            finalValue = "C";
            txtDiemtb.setText(finalValue);

        }
        if (diemtb < 5.5) {
            finalValue = "D";
            txtDiemtb.setText(finalValue);

        }
        if (diemtb < 4) {
            finalValue = "F";
            txtDiemtb.setText(finalValue);

        }

    }

    private MonHoc loadMonHocByTenMH(String tenMH) throws SQLException {
        try {
            con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement("select mamh, tenmh, sotinchi from MonHoc where tenmh = ?");
            ps.setString(1, tenMH);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MonHoc mh = new MonHoc();
                mh.setMamh(rs.getString("mamh"));
                mh.setTenmh(rs.getString("tenmh"));
                mh.setSotinchi(rs.getInt("sotinchi"));
                return mh;
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

    private ArrayList<MonHoc> loadListMonHoc() throws SQLException {
        ArrayList<MonHoc> result = new ArrayList<>();
        try {
            con = DBConnection.createConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select mamh, tenmh, sotinchi from MonHoc");
            while (rs.next()) {
                MonHoc mh = new MonHoc();
                mh.setMamh(rs.getString("mamh"));
                mh.setTenmh(rs.getString("tenmh"));
                mh.setSotinchi(rs.getInt("sotinchi"));
                result.add(mh);
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

    private void Add() throws SQLException {
        if (txtTenSV.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn mã sinh viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtTenMH.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn mã môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtDiemcc.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm chuyên cần của môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtDieml1.isEnabled() && txtDieml1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm lần 1 của môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtDieml1.isEnabled() && txtDieml1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm lần 1 của môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtDieml2.isEnabled() && txtDieml2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm lần 2 của môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtDieml3.isEnabled() && txtDieml3.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm lần 3 của môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtDieml4.isEnabled() && txtDieml4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm lần 4 của môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtDiemck.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm cuối kỳ của môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            if(!CheckMaSVMaMHKiHocNamHoc()) {
                            JOptionPane.showMessageDialog(this, "Sinh viên đã có điểm môn đã chọn ở kỳ học của năm này rồi, vui lòng nhập lại thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);

                return ;
            }
            con = DBConnection.createConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select next_value from Diem_SEQ");
            while (rs.next()) {
                int nextId = rs.getInt("next_value");
                PreparedStatement ps = con.prepareStatement("update Diem_SEQ set last_value = ?");
                ps.setInt(1, nextId);
                ps.executeUpdate();
                if (ps != null) {
                    ps.close();
                }
//
                ps = con.prepareStatement("INSERT INTO Diem (id, masv, mamh, diemcc, dieml1, dieml2, dieml3, dieml4, diemck, diemtb, kihoc, namhoc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                ps.setInt(1, nextId);
                ps.setString(2, cbMaSV.getSelectedItem().toString());
                ps.setString(3, cbMaMH.getSelectedItem().toString());
                ps.setString(4, txtDiemcc.getText());
                ps.setString(5, txtDieml1.getText().isEmpty() ? null : txtDieml1.getText());
                ps.setString(6, txtDieml2.getText().isEmpty() ? null : txtDieml2.getText());
                ps.setString(7, txtDieml3.getText().isEmpty() ? null : txtDieml3.getText());
                ps.setString(8, txtDieml4.getText().isEmpty() ? null : txtDieml4.getText());
                ps.setString(9, txtDiemck.getText());
                ps.setString(10, txtDiemtb.getText());
                ps.setString(11, txtKihoc.getText());
                ps.setString(12, txtNamhoc.getText());

                int row = ps.executeUpdate();

                if (row > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công điểm.");
                    clearFields();
                    ps.close();
                }
                break;
            }
//
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

    private void Edit() throws SQLException {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            long id = (Long) table.getValueAt(selectedRow, 0);
            try {
                con = DBConnection.createConnection();
                PreparedStatement pstmt = con.prepareStatement(
                        "UPDATE Diem SET diemcc = ?, dieml1 = ?, dieml2 = ?, dieml3 = ?, dieml4 = ?, diemck = ?, diemtb = ?, kihoc = ?, namhoc = ? WHERE id = ?");
                pstmt.setFloat(1, Float.parseFloat(txtDiemcc.getText()));
                pstmt.setString(2, !txtDieml1.getText().isEmpty() ? Float.valueOf(txtDieml1.getText()).toString() : null);
                pstmt.setString(3, !txtDieml2.getText().isEmpty() ? Float.valueOf(txtDieml2.getText()).toString() : null);
                pstmt.setString(4, !txtDieml3.getText().isEmpty() ? Float.valueOf(txtDieml3.getText()).toString() : null);
                pstmt.setString(5, !txtDieml4.getText().isEmpty() ? Float.valueOf(txtDieml4.getText()).toString() : null);
                pstmt.setFloat(6, Float.parseFloat(txtDiemck.getText()));
                pstmt.setString(7, txtDiemtb.getText());
                pstmt.setInt(8, Integer.parseInt(txtKihoc.getText()));
                pstmt.setInt(9, Integer.parseInt(txtNamhoc.getText()));
                pstmt.setLong(10, id);
                int row = pstmt.executeUpdate();
                if (row > 0) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công điểm của sinh viên.");
                    clearFields();
                    pstmt.close();
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

    private boolean isValidGrade(String text) {
        try {
            int number = Integer.parseInt(text);
            return number <= 10 && number >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void Delete() throws SQLException {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            long id = (Long) table.getValueAt(selectedRow, 0);
            try {
                con = DBConnection.createConnection();
                PreparedStatement pstmt = con.prepareStatement(
                        "DELETE FROM Diem WHERE id = ?");
                pstmt.setLong(1, id);
                int row = pstmt.executeUpdate();
                if (row > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công điểm của sinh viên.");
                    clearFields();
                    pstmt.close();
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

    private void clearFields() throws SQLException {
        txtDiemtb.setText(StringUtils.EMPTY);
        txtTenSV.setText("");
        txtLop.setText("");
        cbMaMH.setSelectedIndex(0);
        cbMaSV.setSelectedIndex(0);
        clearGradeFields();

        selectedID = -1;
        txtTenMH.setText("");
        txtSoTinChi.setText("");
        deleteBtn.setEnabled(false);
        editBtn.setEnabled(false);
        LoadData();

    }

    private void getSelectedRowData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            clearGradeFields();
            try {
                MonHoc monhoc = loadMonHocByTenMH(model.getValueAt(selectedRow, 4).toString());
                cbMaMH.setSelectedItem(monhoc.getMamh());
                txtSoTinChi.setText(String.valueOf(monhoc.getSotinchi()));
            } catch (SQLException ex) {
                Logger.getLogger(DiemGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!Objects.isNull(model.getValueAt(selectedRow, 6))) {
                txtDieml1.setEnabled(true);
                txtDieml1.setText(model.getValueAt(selectedRow, 6).toString());
            }
            if (!Objects.isNull(model.getValueAt(selectedRow, 7))) {
                txtDieml2.setEnabled(true);

                txtDieml2.setText(model.getValueAt(selectedRow, 7).toString());
            }
            if (!Objects.isNull(model.getValueAt(selectedRow, 8))) {
                txtDieml3.setEnabled(true);

                txtDieml3.setText(model.getValueAt(selectedRow, 8).toString());
            }
            if (!Objects.isNull(model.getValueAt(selectedRow, 9))) {
                txtDieml4.setEnabled(true);

                txtDieml4.setText(model.getValueAt(selectedRow, 9).toString());
            }
            cbMaSV.setSelectedItem(model.getValueAt(selectedRow, 1).toString());
            txtTenSV.setText(model.getValueAt(selectedRow, 2).toString());
            txtLop.setText(model.getValueAt(selectedRow, 3).toString());
            txtTenMH.setText(model.getValueAt(selectedRow, 4).toString());
            txtDiemcc.setText(model.getValueAt(selectedRow, 5).toString());

            txtDiemck.setText(model.getValueAt(selectedRow, 10).toString());
            txtDiemtb.setText(model.getValueAt(selectedRow, 11).toString());

            txtNamhoc.setText(model.getValueAt(selectedRow, 12).toString());
            txtKihoc.setText(model.getValueAt(selectedRow, 13).toString());
            selectedID = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
            editBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
        }
    }

    private boolean CheckMaSVMaMHKiHocNamHoc() {
        String msv = cbMaSV.getSelectedItem().toString();
        String mmh = cbMaMH.getSelectedItem().toString();
        int kihoc = Integer.parseInt(txtKihoc.getText());
        int namhoc = Integer.parseInt(txtNamhoc.getText());
        for (Diem diem : currentGrades) {
            if (diem.getMasv().equals(msv) 
                    && diem.getMamh().equals(mmh) 
                    && diem.getNamhoc() == namhoc 
                    && diem.getKihoc() == kihoc) {
                return false;
            }
        }
        return true;
    }

    private void goBack() {
        dispose();
        new HomeGUI().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new DiemGUI().setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(DiemGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void LoadData() throws SQLException {
        model.setRowCount(0);
        try {
            con = DBConnection.createConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, masv, mamh, diemcc, dieml1, dieml2, dieml3, dieml4, diemck, diemtb, kihoc, namhoc"
                    + " FROM Diem;");
            while (rs.next()) {
                Diem dg = new Diem();
                dg.setId(rs.getLong("id"));
                dg.setMasv(rs.getString("masv"));
                dg.setMamh(rs.getString("mamh"));
                dg.setDiemcc(rs.getFloat("diemcc"));
                dg.setDieml1(rs.getString("dieml1") != null ? rs.getFloat("dieml1") : null);
                dg.setDieml2(rs.getString("dieml2") != null ? rs.getFloat("dieml2") : null);
                dg.setDieml3(rs.getString("dieml3") != null ? rs.getFloat("dieml3") : null);
                dg.setDieml4(rs.getString("dieml4") != null ? rs.getFloat("dieml4") : null);
                dg.setDiemck(rs.getFloat("diemck"));
                dg.setDiemtb(rs.getString("diemtb").charAt(0));
                dg.setNamhoc(rs.getInt("namhoc"));
                dg.setKihoc(rs.getInt("kihoc"));
                currentGrades.add(dg);
                SinhVien sv = loadSinhVienByMaSV(dg.getMasv());
                MonHoc mh = loadMonHocByMaMH(dg.getMamh());
                model.addRow(new Object[]{
                    dg.getId(),
                    dg.getMasv(),
                    sv.getTensv(),
                    sv.getLop(),
                    mh.getTenmh(),
                    dg.getDiemcc(),
                    dg.getDieml1(),
                    dg.getDieml2(),
                    dg.getDieml3(),
                    dg.getDieml4(),
                    dg.getDiemck(),
                    dg.getDiemtb(),
                    dg.getKihoc(),
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

    private void clearGradeFields() {
        txtDiemtb.setText("");
        txtDiemtb.setEnabled(false);
        txtNamhoc.setText("");
        txtDiemcc.setText("");
        txtDieml1.setText("");
        txtDieml1.setEnabled(false);

        txtDieml2.setText("");
        txtDieml2.setEnabled(false);

        txtDieml3.setText("");
        txtDieml3.setEnabled(false);

        txtDieml4.setText("");
        txtDieml4.setEnabled(false);

        txtDiemck.setText("");
        txtKihoc.setText("");
    }

    class NumberKeyListener extends KeyAdapter {

        private JTextField textField;

        public NumberKeyListener(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            String text = textField.getText() + e.getKeyChar();

            // Kiểm tra nếu giá trị nhập vào không hợp lệ
            if (!isValidNumber(text)) {
                e.consume(); // Hủy sự kiện nếu nhập không hợp lệ
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // Chặn việc paste khi nhấn Ctrl+V hoặc Command+V (trên macOS)
            if ((e.isControlDown() || e.isMetaDown()) && e.getKeyCode() == KeyEvent.VK_V) {
                e.consume(); // Hủy hành động paste
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            caculateGrade();
        }

        // Kiểm tra nếu chuỗi nhập vào là số và nằm trong khoảng từ 0 đến 10
        private boolean isValidNumber(String text) {
            try {
                float number = Float.parseFloat(text);
                return number >= 0 && number <= 10;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}
