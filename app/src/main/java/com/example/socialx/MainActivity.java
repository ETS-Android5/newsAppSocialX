package com.example.socialx;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.socialx.Adapter.newsAdapter;
import com.example.socialx.Model.newsModel;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth userAuth;
    private ImageView userLogoutBtn;
    private RecyclerView newsRecycler;
    private EditText newsSearch;
    private ArrayList<newsModel> recyclerNewsModel,searchModel;
    private newsAdapter newsadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userAuth=FirebaseAuth.getInstance();
        userLogoutBtn=findViewById(R.id.options_logout);
        newsRecycler=findViewById(R.id.news_id);
        newsSearch=findViewById(R.id.search_news);
        recyclerNewsModel=new ArrayList<>();
        searchModel=new ArrayList<>();
        newsadapter=new newsAdapter(this,recyclerNewsModel);
        LinearLayoutManager ln =new LinearLayoutManager(this);
        newsRecycler.setLayoutManager(ln);
        newsRecycler.setAdapter(newsadapter);
        newsSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchModel.clear();
                if(s.toString().isEmpty())
                {
                    newsadapter= new newsAdapter(MainActivity.this,recyclerNewsModel);
                    newsRecycler.setAdapter(newsadapter);
                    newsadapter.notifyDataSetChanged();
                }
                else
                {
                    filter(s.toString());
                }
            }
        });
        userLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              logout();
            }
        });
        getNewsInfo();
    }

    private void filter(String text)
    {
        for(newsModel ps:recyclerNewsModel)
        {
            if(ps.getName().equals(text))
            {
                searchModel.add(ps);
            }
        }
        newsadapter= new newsAdapter(MainActivity.this,searchModel);
        newsRecycler.setAdapter(newsadapter);
        newsadapter.notifyDataSetChanged();
    }

    private void getNewsInfo(){
        String uri="https://newsapi.org/v2/everything?q=apple&from=2022-02-22&to=2022-02-22&sortBy=popularity&apiKey=7c95f4b63d7e4fddb9ad48b09b64a4f0";
        RequestQueue request= Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                recyclerNewsModel.clear();
                try {

                    JSONArray articleArray= response.getJSONArray("articles");
                    for(int i=0;i<articleArray.length();i++)
                    {
                        JSONObject jsonArrayObj=articleArray.getJSONObject(i);
                        String title=jsonArrayObj.getString("title");
                        String desc=jsonArrayObj.getString("description");
                        String newsBy=jsonArrayObj.getJSONObject("source").getString("name");
                        String urlToImage=jsonArrayObj.getString("urlToImage");
                        String publishedAt=jsonArrayObj.getString("publishedAt");

                        recyclerNewsModel.add(new newsModel(title,desc,newsBy,urlToImage,publishedAt));
                    }
                    newsadapter.notifyDataSetChanged();

                } catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"error: "+e,Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"error: "+error,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("User-Agent", "Mozilla/5.0");
                return headers;
            }
        };
        request.add(jsonObjectRequest);
    }
    private void logout()
    {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
        alertDialog2.setTitle("Do you want to Logout?");
        alertDialog2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                userAuth.signOut();
                Toast.makeText(MainActivity.this,"Successfully Logged out",Toast.LENGTH_SHORT).show();
                sendUserToLoginActivity();

            }
        });
        alertDialog2.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }
    private void sendUserToLoginActivity() {
        Intent intent = new Intent(MainActivity.this,signUpActivitynew.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}