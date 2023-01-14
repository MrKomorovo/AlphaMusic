package com.example.alphamusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AfterEnterActivity extends AppCompatActivity {

    List<String> nameOfSel;
    List<String> pictureURL;
    List<Integer> numOfSel;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_enter);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView(); //скрыть панель навигации
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        nameOfSel = new ArrayList<>();
        pictureURL = new ArrayList<>();
        numOfSel = new ArrayList<>();

        LinearLayout lvMain = findViewById(R.id.lv_main_menuItem);//Кнопка Главная
        LinearLayout lvProfile = findViewById(R.id.lv_profile_menuItem);//Кнопка Профиль
        TextView tvNickName = findViewById(R.id.tv_userNick);//Имя пользователя

        Bundle loginUser = getIntent().getExtras();
        if(loginUser != null)
            tvNickName.setText(loginUser.getString("userName"));


        //Получение информации о подборках
        mDatabase.child("Selections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Selections selection = ds.getValue(Selections.class);
                    nameOfSel.add(selection.Name);
                    pictureURL.add(selection.PictureURL);
                    numOfSel.add(selection.NumOfSel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Toast.makeText(AfterEnterActivity.this, "Готово!", Toast.LENGTH_LONG).show();
    }
}