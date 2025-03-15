/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qltt.GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author admin
 */
public class HomeGUI extends JFrame {
    private JButton btnStudent, btnScore, btnEvaluation, btnReward;

    public HomeGUI() {
        setTitle("Home - Quản lý Hệ Thống");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));
        
        btnStudent = new JButton("Sinh Viên");
        btnScore = new JButton("Điểm");
        btnEvaluation = new JButton("Đánh Giá");
        btnReward = new JButton("Khen Thưởng");
        
        btnStudent.addActionListener(e -> {
            try {
                openStudentManagement();
            } catch (SQLException ex) {
                Logger.getLogger(HomeGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnScore.addActionListener(e -> openScoreManagement());
        btnEvaluation.addActionListener(e -> {
            try {
                openEvaluationManagement();
            } catch (SQLException ex) {
                Logger.getLogger(HomeGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnReward.addActionListener(e -> openRewardManagement());
        
        add(btnStudent);
        add(btnScore);
        add(btnEvaluation);
        add(btnReward);
    }

    private void openStudentManagement() throws SQLException {
        new SinhVienGUI().setVisible(true);
        dispose();
    }

    private void openScoreManagement() {
        JOptionPane.showMessageDialog(this, "Mở giao diện quản lý điểm (chưa triển khai)");
    }

    private void openEvaluationManagement() throws SQLException {
        new DanhGiaGUI().setVisible(true);
        dispose();
    }

    private void openRewardManagement() {
        JOptionPane.showMessageDialog(this, "Mở giao diện khen thưởng (chưa triển khai)");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomeGUI().setVisible(true));
    }
}
