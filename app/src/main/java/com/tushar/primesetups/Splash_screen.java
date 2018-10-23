package com.tushar.primesetups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.tushar.primesetups.Database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Splash_screen extends AppCompatActivity {

    @BindView(R.id.tv)TextView tv;
    @BindView(R.id.next) Button next;
    @BindView(R.id.prev) Button prev;
    int cs = 0;
    int jsonarraylength = 0;
    public static int TRACK = 0;



    DatabaseHelper databaseHelper;
    String all_setup_url="http://tusharkalsara.com/primesetup.tusharkalsara.com/primesetup/fetch_all_setups.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ButterKnife.bind(this);
        loadjson();


next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Splash_screen.this,MainActivity.class);
        startActivity(intent);

    }
});
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        databaseHelper = new DatabaseHelper(this);






    }

    public void loadjson(){
        final JSONObject js = new JSONObject();
        try {
            js.put("Themecount",6380);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,all_setup_url,js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            databaseHelper.droptable();
                           // Toast.makeText(Splash_screen.this, "afaf", Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray jsonArray = jsonObject.getJSONArray("setups");
                            jsonarraylength = jsonArray.length();
                            tv.setText(jsonArray.getJSONObject(0).toString());
                          //  Toast.makeText(Splash_screen.this, " "+ jsonArray.getJSONObject(0).toString(), Toast.LENGTH_SHORT).show();
                            while (TRACK < jsonarraylength) {
                                databaseHelper.addJson(jsonArray.getJSONObject(TRACK));
                                TRACK++;
                            }
                            if(TRACK>=jsonarraylength){
                                Toast.makeText(Splash_screen.this, "doneee", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }
}
