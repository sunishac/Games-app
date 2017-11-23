package com.example.chala.group12_hw5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements GamesListAsyncTask.IData{
    Button go,search;
    ProgressBar spinner;
    ArrayList<Games> gamesList;
    EditText st;
    RadioGroup rg;
    LinearLayout ll;
    final static String key="Id";
    final static String key1="Name";
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        go=(Button) findViewById(R.id.go);
        search=(Button) findViewById(R.id.search);
        st=(EditText) findViewById(R.id.searchText);
        rg=(RadioGroup) findViewById(R.id.rg);
        ll=(LinearLayout) findViewById(R.id.ll);
        go.setEnabled(false);
        spinner= (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(st.getText().length()!=0) {
                    spinner.setVisibility(View.VISIBLE);
                    rg.setVisibility(View.INVISIBLE);
                    go.setEnabled(false);
                    rg.removeAllViews();
                    String u="http://thegamesdb.net/api/GetGamesList.php?name="+st.getText().toString();
                    Log.d("demo",""+u);
                    new GamesListAsyncTask(MainActivity.this).execute(u);
                }
                else{
                    Toast.makeText(MainActivity.this,"No keyword entered",Toast.LENGTH_LONG).show();
                }
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int t=0;
                t=rg.getCheckedRadioButtonId();
                id=gamesList.get(t).getId();
                //Log.d("demo","id is "+gamesList.get(t).getId());
                go.setEnabled(true);
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,GameDetails.class);
                i.putExtra(key,id);
                i.putExtra(key1,st.getText().toString());
                startActivity(i);
            }
        });

    }

    @Override
    public void List(ArrayList<Games> games) {
        gamesList=games;
        spinner.setVisibility(View.GONE);
        Log.d("demo",""+gamesList);
        if(gamesList.size()==0){
            Toast.makeText(MainActivity.this,"No matches for the keyword",Toast.LENGTH_LONG).show();
        }
        else{
            rg.setVisibility(View.VISIBLE);
            for(int i=0;i<gamesList.size();i++){
                RadioButton r=new RadioButton(this);
                r.setId(i);
                String dob = gamesList.get(i).getDate();
                int yea = 0;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Date d = sdf.parse(dob);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);

                     yea = (cal.get(Calendar.YEAR));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                r.setText(gamesList.get(i).getTitle()+". Released in "+ yea+ ". Platform: "+gamesList.get(i).getPlatform()+".");
                rg.addView(r);
                Log.d("demo",""+gamesList.get(i).getTitle());
            }
        }
    }
}
