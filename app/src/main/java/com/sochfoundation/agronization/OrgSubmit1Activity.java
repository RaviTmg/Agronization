package com.sochfoundation.agronization;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OrgSubmit1Activity extends AppCompatActivity {
    public static final String PAR_KEY = "DATA2";
    Button mnext;
    EditText mphone, memail, mwebsite;
    Organization org;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_submit1);
        mnext = (Button) findViewById(R.id.button_next);
        mphone = (EditText) findViewById(R.id.phone);
        memail = (EditText) findViewById(R.id.email);
        mwebsite = (EditText) findViewById(R.id.website);
        org = getIntent().getParcelableExtra(OrgSubmitActivity.PAR_KEY);
        mnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Organization org1 = new Organization(org.getName(), org.getType(), mphone.getText().toString(),
                       memail.getText().toString(), mwebsite.getText().toString() );
                Intent intent = new Intent(OrgSubmit1Activity.this, OrgSubmit2Activity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(PAR_KEY, org1);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }
}
