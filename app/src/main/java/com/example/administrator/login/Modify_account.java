package com.example.administrator.login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Modify_account extends BaseActivity{
    private EditText acc;
    private EditText oldpas;
    private EditText newpas;
    private db_account dbHelper;

    //判断用户密码正确性
    public boolean test() {
        String account = acc.getText().toString();
        String password = oldpas.getText().toString();
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
    public void mod() {
        boolean flag = test();
        if(flag){
            modify();

            Intent intent = new Intent(Modify_account.this,Login.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(Modify_account.this,"Account or Old Password is invalid",Toast.LENGTH_SHORT).show();
        }
    }

//修改密码
    public void modify(){
        String n = newpas.getText().toString();
        newpas.setText(n);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password",n);
        db.update("phone",values,null,null);
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_layout);

        dbHelper = new db_account(this,"register.db",null,1);
        acc =(EditText) findViewById(R.id.account_modify);
        oldpas = (EditText) findViewById(R.id.oldpas_modify);
        newpas = (EditText) findViewById(R.id.newpas_modify);

        Button no = (Button) findViewById(R.id.modify_no);
        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Modify_account.this,Login.class);
                startActivity(intent);
                finish();
            }

        });

        Button yes = (Button) findViewById(R.id.modify_yes);
        yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mod();
            }
        });

    }
}
