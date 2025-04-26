package com.ngdat.mymusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ngdat.mymusic.Adapter.VideoYoutubeAdapter;
import com.ngdat.mymusic.Model.YoutubeMusic;
import com.ngdat.mymusic.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class KaraokeActivity extends AppCompatActivity {
    protected static String API_KEY = "AIzaSyD6W_TJyg_2vyzw3we2YOSXIod2zBBhrx4";
    private final String ID_PLAYLIST = "PLQDIMgoD-XFSwmiWM8Zc1wH_GoMW_JF70";
    private final String URL_GET_JSON = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + ID_PLAYLIST + "&key=" + API_KEY + "&maxResults=50";
    private ListView listViewVideo;
    private ArrayList<YoutubeMusic> mListVideo;
    private VideoYoutubeAdapter mAdapterVideo;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karaoke);
        init();
        GetJsonYoutbue(URL_GET_JSON);
    }

    private void init() {
        mToolbar = findViewById(R.id.toobarDanhSachPhat);
        listViewVideo = findViewById(R.id.lv_viedeoKaraoke);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Danh Sách Video Karaoke");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListVideo = new ArrayList<YoutubeMusic>();
        mAdapterVideo = new VideoYoutubeAdapter(KaraokeActivity.this, mListVideo);
        listViewVideo.setAdapter(mAdapterVideo);
        listViewVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KaraokeActivity.this, PlayVideoKaraokeActivity.class);
                intent.putExtra("KaraokeVideo", mListVideo.get(position).getmIdVideo());
                startActivity(intent);
            }
        });
    }

    public void GetJsonYoutbue(String url) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    String title = "";
                    String link = "";
                    String chanal = "";
                    String idVideo = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
                        JSONObject jsonObjectSnippet = jsonObjectItem.getJSONObject("snippet");
                        title = jsonObjectSnippet.getString("title");
                        JSONObject jsonObjectThumbnail = jsonObjectSnippet.getJSONObject("thumbnails");
                        JSONObject jsonObjectMedium = jsonObjectThumbnail.getJSONObject("medium");
                        link = jsonObjectMedium.getString("url");
                        chanal = jsonObjectSnippet.getString("channelTitle");
                        JSONObject jsonResourceID = jsonObjectSnippet.getJSONObject("resourceId");
                        idVideo = jsonResourceID.getString("videoId");
                        mListVideo.add(new YoutubeMusic(title, idVideo, link, chanal));
                    }
                    mAdapterVideo.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
