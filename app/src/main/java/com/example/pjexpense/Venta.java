package com.example.pjexpense;

public class Venta {
    int _id;
    int date;
    int month;
    int year;
    String type;
    String tittle;
    int amount;

    public Venta(int _id,int date,int month,int year, String type,String tittle, int amount) {
        this._id = _id;
        this.date = date;
        this.month = month;
        this.year = year;
        this.type = type;
        this.type = tittle;
        this.amount = amount;
    }

    public int getIdVenta() {
        return _id;
    }

    public void setIdVenta(int _id) {
        this._id = _id;
    }
    public int getDate(){
        return date;
    }
    public void setDate(int date){
        this.date = date;
    }

    public int getMonth(){
        return month;
    }
    public void setMonth(int month){
        this.month = month;
    }

    public int getYear(){
        return year;
    }
    public void setYear(int year){
        this.year = year;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTittle() {
        return tittle;
    }
    public void setTittle(String tittle) {
        this.type = tittle;
    }
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

