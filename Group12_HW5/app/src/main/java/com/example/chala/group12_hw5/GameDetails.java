package com.example.chala.group12_hw5;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GameDetails extends AppCompatActivity implements DetailsAsyncTask.Data{
    String g,z;
    Button p,s,f;
    ImageView iv;
    TextView tv,ov,gn,pb;
    ProgressDialog pd;
    ArrayList<Details> dt=new ArrayList<Details>();
    final static String key2="Name";
    final static String key3="Title";
    final static String key4="Id";
    final static String key5="similar";
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        if(getIntent().getExtras()!=null){
            g=(String) getIntent().getExtras().getSerializable(MainActivity.key);
            z=(String) getIntent().getExtras().getSerializable(MainActivity.key1);
        }
        Log.d("demo","http://thegamesdb.net/api/GetGame.php?id="+g);
        String u="http://thegamesdb.net/api/GetGame.php?id="+g;
        tv=(TextView) findViewById(R.id.title_db);
        ov=(TextView) findViewById(R.id.overText);
        gn=(TextView) findViewById(R.id.genreText);
        pb=(TextView) findViewById(R.id.pubText);
        wv=(WebView) findViewById(R.id.webView);
        p=(Button) findViewById(R.id.play);
        s=(Button) findViewById(R.id.similar);
        f=(Button) findViewById(R.id.finish);
        iv=(ImageView) findViewById(R.id.imageView);

        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.show();
        new DetailsAsyncTask(GameDetails.this).execute(u);


        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(GameDetails.this,SimGames.class);
                i.putExtra(key2,z);
                i.putExtra(key3,dt.get(0).getTitle());
                i.putExtra(key4,g);
                i.putExtra(key5,dt.get(0).getSim());
                startActivity(i);
            }
        });

        p.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                Log.d("demo","ut url is"+dt.get(0).getYou());
                if(dt.get(0).getYou()==null){
                    Toast.makeText(GameDetails.this,"No Youtube Url for this game",Toast.LENGTH_LONG).show();
                }
                else {
                    wv.setVisibility(View.VISIBLE);
                    wv.loadUrl(dt.get(0).getYou());
                }
                wv.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void over(ArrayList<Details> details) {
        dt=details;
        pd.cancel();
        Log.d("demo","values in "+dt);
        tv.setText(dt.get(0).getTitle());
        ov.setText(dt.get(0).getOverview());
        gn.setText(dt.get(0).getGenre());
        pb.setText(dt.get(0).getPub());
        Log.d("demo","values sim are"+dt.get(0).getSim());
        if(dt.get(0).getImage()==null){
            Toast.makeText(this,"no image for this",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"loading image ",Toast.LENGTH_LONG).show();
            Picasso.with(this)
                    .load(dt.get(0).getImage())
                    .into(iv);
        }
    }

}
