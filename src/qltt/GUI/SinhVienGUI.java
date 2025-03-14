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

/**
 *
 * @author admin
 */
public class SinhVienGUI extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JTextField txtId, txtName, txtClass, txtAddress, txtPhone, txtSearch;
    private JRadioButton rbMale, rbFemale;
    private ButtonGroup genderGroup;
    private JDateChooser dateChooser;
    private JButton btnAdd, btnEdit, btnDelete, btnSearch;

    public SinhVienGUI() {
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
        txtPhone = new JTextField();
        inputPanel.add(txtPhone);

        inputPanel.add(new JLabel("Năm sinh:"));
        dateChooser = new JDateChooser();
        inputPanel.add(dateChooser);

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

        model = new DefaultTableModel(new String[]{"Mã SV", "Tên SV", "Lớp", "Giới tính", "Địa chỉ", "SĐT", "Năm sinh"}, 0);
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setSelectionBackground(Color.LIGHT_GRAY);
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnSearch.addActionListener(e -> searchStudent());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                goBack();
            }
        });
    }

    private void addStudent() {
        clearFields();
    }

    private void goBack() {
        dispose();
        new HomeGUI().setVisible(true);
    }

    private void editStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.setValueAt(txtId.getText(), selectedRow, 0);
            model.setValueAt(txtName.getText(), selectedRow, 1);
            clearFields();
        }
    }

    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        }
    }

    private void searchStudent() {
        String keyword = txtSearch.getText().toLowerCase();
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i, 1).toString().toLowerCase().contains(keyword)) {
                table.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SinhVienGUI().setVisible(true));
    }
}
