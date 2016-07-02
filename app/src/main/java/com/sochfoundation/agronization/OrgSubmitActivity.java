package com.sochfoundation.agronization;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OrgSubmitActivity extends AppCompatActivity {
    public static final String PAR_KEY = "DATA1";
    Button mnext;
    EditText mname, mtype;
    Organization org;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_submit);
        mnext = (Button) findViewById(R.id.button_next);
        mname = (EditText) findViewById(R.id.name);
        mtype = (EditText) findViewById(R.id.type);
        mnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                org = new Organization(mname.getText().toString(), mtype.getText().toString());
                Intent intent = new Intent(OrgSubmitActivity.this, OrgSubmit1Activity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(PAR_KEY, org);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }
}
