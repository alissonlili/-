package com.example.project_time;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText edtDay,edtHour,edtMin;
    private long currentsystemtime;
    private long settime;
    private Calendar calendar;
    //    取得日、時、分三種時間輸入
    private String day;
    private String hour;
    private String min;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkNotificationPermission();
        //        Appbar建立標題
        setTitle("時間設定");
        findViews();

    }


    private void findViews() {
        //        取得個元件ID
        edtDay=(TextInputEditText)findViewById(R.id.edtDay);
        edtHour=(TextInputEditText)findViewById(R.id.edtHour);
        edtMin=(TextInputEditText)findViewById(R.id.edtMin);
    }

    //            取得目前時間
    private void currentTime() {
        //        calendar實例化，取得預設時間、預設時區
        calendar = Calendar.getInstance();

        //        設定系統目前時間、目前時區(GMT+8)
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        //        獲得系統目前時間
        currentsystemtime=System.currentTimeMillis();
    }

    //        使用者輸入情況判斷
    private boolean isEmptyText(){
        day = edtDay.getText().toString();
        hour = edtHour.getText().toString();
        min = edtMin.getText().toString();
        if(day.isEmpty()|| hour.isEmpty()|| min.isEmpty()){
            return true;
        }
        else {
            return false;
        }
    }

    //        設定定時
    private void setTime(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(day));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(min));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        //        獲得定時時間
        settime = calendar.getTimeInMillis();

        //        若定時時間(日、時、分)比目前小自動設定為下個月的時間(日、時、分)
        if (currentsystemtime > settime) {
            //            增加一個月
            calendar.add(Calendar.MONTH, 1);

            //        重新獲得定時時間
            settime = calendar.getTimeInMillis();
        }
    }

    //   設定alarm
    private void setAlarm() {
        Intent intent = new Intent(MainActivity.this, alarmReceiver.class);
        //        PendingIntent.getBroadcast調用廣播
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        //        獲得AlarmManager物件
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //        設定單次提醒
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    //    顯示已完成設定的時間
    private void showtime() {

        String text =(calendar.get(Calendar.MONTH)+1)+"月"
                +calendar.get(Calendar.DAY_OF_MONTH)+"日\n"
                +calendar.get(Calendar.HOUR_OF_DAY)+":"
                + calendar.get(Calendar.MINUTE);

        Toast.makeText(this,"設定成功\n"
                        + "設定時間為\n"+text,Toast.LENGTH_LONG)
                .show();
    }
    public void btnNotify(View view) {
        //        使用者輸入情況判斷
        if(isEmptyText()){
            Toast.makeText(MainActivity.this,"輸入不正確", Toast.LENGTH_SHORT).show();
        }
        else{
            //    取得目前時間
            currentTime();
            //     設定定時
            setTime(calendar);
            //   設定alarm
            setAlarm();
            //    顯示已完成設定的時間
            showtime();
            //     結束此頁面
            finish();
        }
    }
    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            // 检查通知渠道是否已被禁用
            if (notificationManager != null && !notificationManager.areNotificationsEnabled()) {
                // 跳转到应用程序的通知设置页面
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivityForResult(intent, NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    // 处理权限请求结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            // 检查用户是否授予通知权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                if (notificationManager != null && notificationManager.areNotificationsEnabled()) {
                    // 用户已授予通知权限，可以进行相应操作
                } else {
                    // 用户未授予通知权限，可以根据需要进行处理
                }
            }
        }
    }
}
