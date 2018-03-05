package com.parse.anywall;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends AppCompatActivity {

    String name,phone,add,des;
    Button b1;

    TextView tv1,tv2,tv3,tv4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();

        name = i.getStringExtra("name");
        phone = i.getStringExtra("phone");
        add = i.getStringExtra("add");
        des = i.getStringExtra("des");



        tv1 = (TextView) findViewById(R.id.name);
        tv2 = (TextView) findViewById(R.id.phone);
        tv3 = (TextView) findViewById(R.id.add);
        tv4 = (TextView) findViewById(R.id.des);

        b1 = (Button) findViewById(R.id.button5);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phone));
                startActivity(callIntent);
            }
        });
        tv1.setText(name);
        tv2.setText(phone);
        tv3.setText(add);
        tv4.setText(des);

    }


}
