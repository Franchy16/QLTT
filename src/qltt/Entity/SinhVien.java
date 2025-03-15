/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qltt.Entity;

/**
 *
 * @author admin
 */
public class SinhVien {

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the masv
     */
    public String getMasv() {
        return masv;
    }

    /**
     * @param masv the masv to set
     */
    public void setMasv(String masv) {
        this.masv = masv;
    }

    /**
     * @return the tensv
     */
    public String getTensv() {
        return tensv;
    }

    /**
     * @param tensv the tensv to set
     */
    public void setTensv(String tensv) {
        this.tensv = tensv;
    }

    /**
     * @return the lop
     */
    public String getLop() {
        return lop;
    }

    /**
     * @param lop the lop to set
     */
    public void setLop(String lop) {
        this.lop = lop;
    }

    /**
     * @return the gioitinh
     */
    public int getGioitinh() {
        return gioitinh;
    }

    /**
     * @param gioitinh the gioitinh to set
     */
    public void setGioitinh(int gioitinh) {
        this.gioitinh = gioitinh;
    }

    /**
     * @return the diachi
     */
    public String getDiachi() {
        return diachi;
    }

    /**
     * @param diachi the diachi to set
     */
    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    /**
     * @return the sdt
     */
    public String getSdt() {
        return sdt;
    }

    /**
     * @param sdt the sdt to set
     */
    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    /**
     * @return the namsinh
     */
    public String getNamsinh() {
        return namsinh;
    }

    /**
     * @param namsinh the namsinh to set
     */
    public void setNamsinh(String namsinh) {
        this.namsinh = namsinh;
    }
    private int id;
    private String masv;
    private String tensv;
    private String lop;
    private int gioitinh;
    private String diachi;
    private String sdt;
    private String namsinh;
    
    public SinhVien(){};
    public SinhVien(int id, String masv, String tensv, String lop, int gioitinh, String diachi, String sdt, String namsinh){
        this.id = id;
        this.masv = masv;
        this.tensv = tensv;
        this.lop = lop;
        this.gioitinh = gioitinh;
        this.diachi = diachi;
        this.sdt = sdt;
        this.namsinh = namsinh;
    }
}
