package com.patos.carryme.datePicker;


import android.support.v4.app.FragmentActivity;



public class DatePickerr {
    public static boolean isStart;
    private FragmentActivity factivity;
    public DatePickerr(FragmentActivity factivity){
        this.factivity=factivity;
    }
    public void get(String text ){
        android.support.v4.app.DialogFragment endDatePicker= new DatePickerFragment();
        endDatePicker.show(factivity.getSupportFragmentManager(),text);
    }
}
