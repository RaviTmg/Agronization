package com.sochfoundation.agronization;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OrgSubmit2Activity extends AppCompatActivity {
    public static final String PAR_KEY = "DATA3";
    Button mnext;
    EditText mdesc;
    Organization org;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_submit2);
        mnext = (Button) findViewById(R.id.button_next);
        mdesc = (EditText) findViewById(R.id.desc);
        org = getIntent().getParcelableExtra(OrgSubmit1Activity.PAR_KEY);
        mnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Organization org1 = new Organization(org.getName(), org.getType(), org.getPhone(),
                        org.getEmail(), org.getWebsite(), mdesc.getText().toString() );
                Intent intent = new Intent(OrgSubmit2Activity.this, MapsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(PAR_KEY, org1);
                mBundle.putInt("TAB_INDEX", 3);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }
}
