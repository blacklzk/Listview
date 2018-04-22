package com.example.administrator.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.name;
import static android.R.attr.phoneNumber;
import static com.example.administrator.login.R.attr.title;

public class MainActivity extends BaseActivity {
    private ListView listView;
    private Map<String, String> contact;

    List<String> contactsList = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //登出
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.administrator.login.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });


        //ListView列出联系人
        ListView contact = (ListView) findViewById(R.id.contact_view);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
        contact.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission
                (this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions
                    (this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            readContacts();
        }
    }

    //查询联系人数据
    private void readContacts() {
        listView = (ListView) findViewById(R.id.contact_view);
        SimpleAdapter simpleAdapter;
        List<Map<String, String>> listmaps = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query
                    (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString((cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.NUMBER)));

                    Map<String, String> map = new HashMap<>();
                    map.put("name", displayName);
                    map.put("number", number);
                    listmaps.add(map);

                }
                simpleAdapter = new SimpleAdapter(MainActivity.this, listmaps,
                        android.R.layout.simple_list_item_2, new String[]{"name", "number"},
                        new int[]{android.R.id.text1, android.R.id.text2});
                listView.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
                make_call(listmaps);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //点击ListView打电话
    public void make_call(final List<Map<String, String>> mapList) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (ContextCompat.checkSelfPermission
                        (MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions
                            (MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    Map<String, String> mMap = mapList.get(position);
                    contact = mapList.get(position);
                    //String number = contact.get("number");
                    call();
                }
            }
        });
    }

    //拨号
    private void call() {
        try {

            Intent intent = new Intent(Intent.ACTION_CALL);
            String n = contact.get("number");
            intent.setData(Uri.parse("tel:" + n));
            startActivity(intent);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
//添加联系人
                Intent add = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                add.putExtra(android.provider.ContactsContract.Intents.Insert.NAME, name);
                add.putExtra(android.provider.ContactsContract.Intents.Insert.JOB_TITLE,title);
                add.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE,phoneNumber);
                startActivity(add);
                break;

//修改联系人
            case R.id.update_item:
                Intent update = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                update.setType("vnd.android.cursor.item/person");
                update.setType("vnd.android.cursor.item/contact");
                update.setType("vnd.android.cursor.item/raw_contact");
                update.putExtra(android.provider.ContactsContract.Intents.Insert.NAME, name);
                update.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, phoneNumber);
                update.putExtra(android.provider.ContactsContract.Intents.Insert.JOB_TITLE, title);
                startActivity(update);
                break;
            default:
        }
        return true;
    }

//判断是否用户使用权限
    public void onRequestPermissionsResult(int requestCode, String[]  permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContacts();
                }
                else {
                    Toast.makeText(this, "You Refused This Permission!", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if(grantResults.length > 0 && grantResults[0] ==PackageManager.
                        PERMISSION_GRANTED){
                    call();
                }
                else {
                    Toast.makeText(this, "You Refused This Permission!",Toast.LENGTH_LONG).show();
                }
                break;

            default:
        }
    }
}