package com.example.administrator.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends BaseActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private CheckBox rememberPass;
    private db_account dbHelper;

//验证账号密码
        public boolean login() {
            String account = accountEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("phone", null, null, null, null, null, null);
            if (cursor.moveToFirst())
                do {
                    String a = cursor.getString(cursor.getColumnIndex("account"));
                    String b = cursor.getString(cursor.getColumnIndex("password"));

                     if(account.equals(a) && password.equals(b)) {
                         return true;
                     }
                }while (cursor.moveToNext());
            cursor.close();
            return false;
        }

//登录
        public void log(){
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();

            boolean flag=login();

        if(flag){
          //记住密码
            editor = pref.edit();
            if(rememberPass.isChecked()){
                editor.putBoolean("remember_password",true);
                editor.putString("account",account);
                editor.putString("password",password);
            }else{
                editor.clear();
            }
            editor.apply();

            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(Login.this,"account or password is invalid",Toast.LENGTH_SHORT).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        dbHelper = new db_account(this,"register.db",null,1);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        Button login = (Button) findViewById(R.id.login);
        Button register = (Button) findViewById(R.id.register);
        Button modify = (Button) findViewById(R.id.modify_);

//记住密码
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
//注册button
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(Login.this,Register.class) ;
                startActivity(intent);
                finish();
            }

        });
//登录button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            log();
            }
        });

        modify.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Modify_account.class) ;
                startActivity(intent);
                finish();
            }
        });
    }
}
