package com.example.chala.group12_hw5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SimGames extends AppCompatActivity implements GamesListAsyncTask.IData {
    LinearLayout ll;
    String a,b,w;
    ArrayList<Games> c=new ArrayList<Games>();
    ArrayList<String> sm=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_games);
        sm=null;
        if(getIntent().getExtras()!=null){
            a=(String) getIntent().getExtras().getSerializable(GameDetails.key2);
            b=(String) getIntent().getExtras().getSerializable(GameDetails.key3);
            w=(String) getIntent().getExtras().getSerializable(GameDetails.key4);
            sm=(ArrayList<String>) getIntent().getExtras().getSerializable(GameDetails.key5);
        }
        ll=(LinearLayout) findViewById(R.id.ll_s);

        findViewById(R.id.finishS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tv=(TextView) findViewById(R.id.title_similar);
        tv.setText("Similar games to "+b);
        String d="http://thegamesdb.net/api/GetGamesList.php?name="+a;
        Log.d("demo","sim games: "+sm);
        if(sm==null ) {
            Toast.makeText(this,"No similar games",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"Loading similar games ",Toast.LENGTH_LONG).show();
            new GamesListAsyncTask(SimGames.this).execute(d);
        }
    }

    @Override
    public void List(ArrayList<Games> games) {
        c=games;
        Log.d("demo","c is"+ c);
            for (int i = 0; i < c.size(); i++) {
                for (int j = 0; j < sm.size(); j++) {
                    if (c.get(i).getId().equals(sm.get(j))) {
                        Log.d("demo", "id in list " + c.get(i).getId() + " , sim Id is " + sm.get(j));
                        TextView t = new TextView(this);
                        String dob=c.get(i).getDate();
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
                        t.setText(c.get(i).getTitle() + ", Released in " + yea + ", Platform: " + c.get(i).getPlatform() + ".");
                        ll.addView(t);
                    }
                }
            }
    }
}
