package com.sdbi.delightfulmusic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText editUser;
    private EditText editPass;
    private Button login;
    private CheckBox rememberPass;
    private CheckBox displayPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editUser = findViewById(R.id.editText);
        editPass = findViewById(R.id.editText2);
        rememberPass = findViewById(R.id.rememberPassword);
        displayPass = findViewById(R.id.displayPassword);
        login = findViewById(R.id.button);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            editUser.setText(pref.getString("account", ""));
            editPass.setText(pref.getString("password", ""));
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckLog()) {
                    String account = editUser.getText().toString();
                    String password = editPass.getText().toString();
                    if (rememberPass.isChecked()) {
                        editor = pref.edit();
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                        Toast.makeText(Login.this, "登录信息已保存", Toast.LENGTH_SHORT).show();
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //登录成功后杀死登录活动
                    Login.this.finish();
                    Intent intent = new Intent(Login.this, Welcome.class);
                    startActivity(intent);


                } else {
                    Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //显示密码
        displayPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //如果选中，显示密码
                    editPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    editPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });
    }

    private boolean CheckLog() {
        String id = editUser.getText().toString();
        String pwd = editPass.getText().toString();
        if (id.equals("admin") && pwd.equals("123456")) {
            return true;
        } else {
            return false;
        }
    }
}
