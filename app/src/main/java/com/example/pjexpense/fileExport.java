package com.example.pjexpense;

public class fileExport {
    public int _id;
    public int dateDay;
    public int dateMonth;
    public int dateyear;
    public String listInEx;
    public int income;
    public int expense;
    public int totalInEx;

    public fileExport (int _id,int dateDay,int dateMonth,int dateyear,String listInEx,int income,int expense,int totalInEx){
        this._id = _id;
        this.dateDay = dateDay;
        this.dateMonth = dateMonth;
        this.dateyear = dateyear;
        this.listInEx = listInEx;
        this.income = income;
        this.expense = expense;
        this.totalInEx = totalInEx;
    }


}
