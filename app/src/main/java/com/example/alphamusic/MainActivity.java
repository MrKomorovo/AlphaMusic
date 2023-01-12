package com.example.alphamusic;

import static android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    boolean isAuthorization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //test
        Intent intent = new Intent(MainActivity.this, AfterEnterActivity.class);
        startActivity(intent);
        //test
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView(); //скрыть панель навигации
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
               | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        TextView btReg = findViewById(R.id.bt_registration);//Кнопка перехода на регистрацию
        EditText etLogin = findViewById(R.id.et_Login);//ВХОД
        EditText etRLogin = findViewById(R.id.et_RLogin);//РЕГИСТРАЦИЯ
        EditText etPassword = findViewById(R.id.et_Password);//ВХОД
        EditText etRPassword = findViewById(R.id.et_RPassword);//РЕГИСТРАЦИЯ
        EditText etRReplacePassword = findViewById(R.id.et_RReplacePassword);//РЕГИСТРАЦИЯ
        EditText etNickName = findViewById(R.id.et_RNick);//РЕГИСТРАЦИЯ
        TextView btLog = findViewById(R.id.bt_registration2);//Кпнопка перехода на ВХОД
        TextView btEnterReg = findViewById(R.id.bt_registr);//ЗАРЕГИСТРИРОВАТЬСЯ
        TextView btEnter = findViewById(R.id.bt_enter);//ВОЙТИ
        TextView tvInvalidPassword = findViewById(R.id.tv_invalidPassword);//ТЕКСТ "ПАРОЛИ НЕ СОВПАДАЮТ"
        TextView tvNoneLogin = findViewById(R.id.tv_noneLogin);//ТЕКСТ "НЕТ ЛОГИНА"
        TextView tvNoneAll = findViewById(R.id.tv_noneAll);//ТЕКСТ "НЕ ВСЕ ДАННЫЕ"
        TextView tvNonePassword = findViewById(R.id.tv_nonePassword);//ТЕКСТ "НЕТ ПАРОЛЯ"
        TextView tvInvalidPasswordOrLogin = findViewById(R.id.tv_invalidPasswordOrLogin);//ТЕКСТ "Неправильный логин или пароль"
        TextView tvNot4Symbols = findViewById(R.id.tv_notFourSymbols);//Данные меньше 4 символов


        //переход на РЕГИСТРАЦИЮ
        btReg.setOnClickListener(view -> {
            btLog.setVisibility(View.VISIBLE);
            etLogin.setVisibility(View.GONE);
            etRLogin.setVisibility(View.VISIBLE);
            etRPassword.setVisibility(View.VISIBLE);
            etRReplacePassword.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.GONE);
            btEnterReg.setVisibility(View.VISIBLE);
            btEnter.setVisibility(View.GONE);
            btReg.setVisibility(View.GONE);
            etNickName.setVisibility(View.VISIBLE);
            etLogin.setText("");
            etPassword.setText("");
        });

        //переход на ВХОД
        btLog.setOnClickListener(view -> {
            btLog.setVisibility(View.GONE);
            etLogin.setVisibility(View.VISIBLE);
            etRLogin.setVisibility(View.GONE);
            etRPassword.setVisibility(View.GONE);
            etRReplacePassword.setVisibility(View.GONE);
            etPassword.setVisibility(View.VISIBLE);
            btReg.setVisibility(View.VISIBLE);
            btEnter.setVisibility(View.VISIBLE);
            btEnterReg.setVisibility(View.GONE);
            etNickName.setVisibility(View.GONE);
            etRLogin.setText("");
            etRPassword.setText("");
            etRReplacePassword.setText("");
            etNickName.setText("");
        });


        //Регистрация
        btEnterReg.setOnClickListener(view -> {
            if(etRLogin.getText().length() > 4 && etRPassword.getText().length() > 4 &&
                    etRReplacePassword.getText().length() > 4 && etNickName.getText().length() > 4){
                if(etRPassword.getText().toString().equals(etRReplacePassword.getText().toString())){
                    String id = mDatabase.getKey();
                    Toast.makeText(MainActivity.this, "Регистрация успешна\nТеперь войдите в свой аккаунт", Toast.LENGTH_LONG).show();
                    User user = new User(etNickName.getText().toString(), etRLogin.getText().toString(), etRPassword.getText().toString());
                    mDatabase.child("Users").push().setValue(user);
                    btLog.setVisibility(View.GONE);
                    etLogin.setVisibility(View.VISIBLE);
                    etRLogin.setVisibility(View.GONE);
                    etRPassword.setVisibility(View.GONE);
                    etRReplacePassword.setVisibility(View.GONE);
                    etPassword.setVisibility(View.VISIBLE);
                    btReg.setVisibility(View.VISIBLE);
                    btEnter.setVisibility(View.VISIBLE);
                    btEnterReg.setVisibility(View.GONE);
                    etNickName.setVisibility(View.GONE);
                    etRLogin.setText("");
                    etRPassword.setText("");
                    etRReplacePassword.setText("");
                    etNickName.setText("");
                    tvNoneAll.setVisibility(View.GONE);
                    tvInvalidPassword.setVisibility(View.GONE);
                }
                else{
                    //пароли не совпадают
                    tvNoneAll.setVisibility(View.GONE);
                    tvInvalidPassword.setVisibility(View.VISIBLE);
                    etRLogin.clearFocus();
                    etRPassword.clearFocus();
                    etRReplacePassword.clearFocus();
                    etNickName.clearFocus();
                }
            }
            else{
                //Надписи про не все данные при регистрации
                if(etRLogin.getText().length() == 0 || etRPassword.getText().length() == 0 || etRReplacePassword.getText().length() == 0 ||
                    etNickName.getText().length() == 0) {
                    tvInvalidPassword.setVisibility(View.GONE);
                    tvNoneAll.setVisibility(View.VISIBLE);
                    etRLogin.clearFocus();
                    etRPassword.clearFocus();
                    etRReplacePassword.clearFocus();
                    etNickName.clearFocus();
                }
                else {
                    //Данные меньше 4 символов
                    if (etRLogin.getText().length() < 4 || etRPassword.getText().length() < 4 || etRReplacePassword.getText().length() < 4 ||
                            etNickName.getText().length() < 4) {
                        tvNot4Symbols.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        //Убрать надпись про неккоректные данные
        etRPassword.setOnFocusChangeListener((view, b) -> {
            tvInvalidPassword.setVisibility(View.GONE);
            tvNoneAll.setVisibility(View.GONE);
            tvNot4Symbols.setVisibility(View.GONE);
        });
        etRReplacePassword.setOnFocusChangeListener((view, b) -> {
            tvInvalidPassword.setVisibility(View.GONE);
            tvNoneAll.setVisibility(View.GONE);
            tvNot4Symbols.setVisibility(View.GONE);
        });
        etNickName.setOnFocusChangeListener((view, b) -> {
            tvNoneAll.setVisibility(View.GONE);
        });
        etRLogin.setOnFocusChangeListener((view, b) -> {
            tvNoneAll.setVisibility(View.GONE);
            tvNot4Symbols.setVisibility(View.GONE);
        });
        etLogin.setOnFocusChangeListener((view, b) -> {
            tvNoneLogin.setVisibility(View.GONE);
            tvInvalidPasswordOrLogin.setVisibility(View.GONE);
        });
        etPassword.setOnFocusChangeListener((view, b) -> {
            tvNonePassword.setVisibility(View.GONE);
            tvInvalidPasswordOrLogin.setVisibility(View.GONE);
        });

        //Вход
        btEnter.setOnClickListener(view -> {
            if(etLogin.getText().length() > 0 && etPassword.getText().length() > 0){

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            User user = ds.getValue(User.class);
                            if (etLogin.getText().toString().equals(user.Login) && etPassword.getText().toString().equals(user.Password)) {
                                isAuthorization = true;
                                break;
                            }
                            else
                                isAuthorization = false;
                        }
                        if(isAuthorization) {
                            Intent intent = new Intent(MainActivity.this, AfterEnterActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Авторизация не прошла", Toast.LENGTH_LONG).show();
                            tvInvalidPasswordOrLogin.setVisibility(View.VISIBLE);
                            etLogin.clearFocus();
                            etPassword.clearFocus();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
            else{//надпси о некорректных данных
                if(etLogin.getText().length() <= 0)
                    tvNoneLogin.setVisibility(View.VISIBLE);
                if(etPassword.getText().length() <= 0)
                    tvNonePassword.setVisibility(View.VISIBLE);

            }
        });
    }
}