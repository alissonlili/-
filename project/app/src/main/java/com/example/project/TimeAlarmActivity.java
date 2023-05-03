package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class TimeAlarmActivity extends AppCompatActivity {
    private TextInputEditText edtDay,edtHour,edtMin;
    private long currentsystemtime;
    private long settime;
    private Calendar calendar;
    //    取得日、時、分三種時間輸入
    private String day;
    private String hour;
    private String min;
}
