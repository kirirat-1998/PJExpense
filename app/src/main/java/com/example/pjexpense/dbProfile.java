package com.example.pjexpense;

public class dbProfile {
    public int idtool;
    public String proName;
    public String proSurName;
    public String proEmail;

    public dbProfile(int idtool, String proName, String proSurName, String proEmail){
        this.idtool = idtool;
        this.proName = proName;
        this.proSurName = proSurName;
        this.proEmail = proEmail;
    }

    public void setIdtool(int idtool) {
        this.idtool = idtool;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public void setProSurName(String proSurName) {
        this.proSurName = proSurName;
    }

    public void setProEmail(String proEmail) {
        this.proEmail = proEmail;
    }

    public int getIdtool() {
        return idtool;
    }

    public String getProName() {
        return proName;
    }

    public String getProSurName() {
        return proSurName;
    }

    public String getProEmail() {
        return proEmail;
    }



}
