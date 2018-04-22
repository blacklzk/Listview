package com.example.administrator.login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends BaseActivity{
    private db_account dbHelper;
    private EditText account_registerEdit;
    private EditText password_registerEdit;

    public void reg(){
        //建表
        dbHelper.getWritableDatabase();

        //插入数据
        String account = account_registerEdit.getText().toString();
        //account_registerEdit.setText(account);
        String password = password_registerEdit.getText().toString();
        //password_registerEdit.setText(password);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account",account);
        values.put("password",password);
        db.insert("phone",null,values);
        Toast.makeText(Register.this,"Success",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Register.this,Login.class);
        startActivity(intent);
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        dbHelper = new db_account(this,"register.db",null,1);
        account_registerEdit = (EditText) findViewById(R.id.account_register);
        password_registerEdit =(EditText) findViewById(R.id.password_register);


        Button cancel = (Button) findViewById(R.id.No);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
                finish();
            }

        });

        Button register = (Button)findViewById(R.id.Yes);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            reg();
            }
        });

    }
}
