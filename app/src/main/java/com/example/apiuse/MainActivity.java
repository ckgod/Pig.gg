package com.example.apiuse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    static final String APIKEY = "//";
    static final String ORI_URL_SUMMONER = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/";
    static final String ORI_URL_LEAGUE = "https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/";
    static final String ORI_URL_ICON = "http://ddragon.leagueoflegends.com/cdn/10.3.1/img/profileicon/";
    static final String ORI_URL_MATCH = "https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/";
    static final String ORI_URL_GAME = "https://kr.api.riotgames.com/lol/match/v4/matches/";

    static final String APIKEYADD = "?api_key=";
    static final String APIKEYADD2 = "?endIndex=10&beginIndex=0&api_key=";
    EditText et_search;


    String id,accountId,level,iconId,name;
    String wins,losses,tier,rank,queueType,leaguePoint;
    String gameWin, kill, death, assist;
    String item0,item1,item2,item3,item4,item5,item6;

    String summonerName;
    Button btn_search,btn_invaildate;

    String postIconId;
    String postName;
    String postId;
    String postAccountId;
    String postGameId;
    TextView tv_summonerName,tv_summonerLevel,tv_summonerWinrate;

    ImageView iv_soloRankImg, iv_freeRankImg;
    TextView tv_soloRankTierName, tv_soloRankLP, tv_soloRankWinRate;
    TextView tv_freeRankTierName, tv_freeRankLP, tv_freeRankWinRate;

    ImageView iv_icon;

    String testurl = "http://ddragon.leagueoflegends.com/cdn/10.3.1/img/champion/Aatrox.png";

    private RecordAdapter adapter;
    ListView list_record;
    ArrayList<Record> rcds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_search = findViewById(R.id.et_search);
        btn_search = findViewById(R.id.btn_search);
        list_record = findViewById(R.id.Lv_past);
        btn_invaildate = findViewById(R.id.btn_invaildate);

        // 소환사 정보
        iv_icon = findViewById(R.id.iv_icon);
        tv_summonerName = findViewById(R.id.tv_summonerName);
        tv_summonerLevel = findViewById(R.id.tv_summonerLevel);
        tv_summonerWinrate = findViewById(R.id.tv_summonerWInRate);

        iv_soloRankImg = findViewById(R.id.iv_soloRankImg);
        tv_soloRankTierName = findViewById(R.id.tv_soloRankTierName);
        tv_soloRankLP = findViewById(R.id.tv_soloRankLP);
        tv_soloRankWinRate = findViewById(R.id.tv_soloRankWinRate);

        iv_freeRankImg = findViewById(R.id.iv_freeRankImg);
        tv_freeRankTierName = findViewById(R.id.tv_freeRankTierName);
        tv_freeRankLP = findViewById(R.id.tv_freeRankLP);
        tv_freeRankWinRate = findViewById(R.id.tv_freeRankWinRate);



        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rcds.clear();

                    summonerName = URLEncoder.encode(et_search.getText().toString(),"UTF-8");
                    postName = sumURL(summonerName);
                    getSummoner(postName);

                    splash();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_invaildate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"not yet",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String sumURL(String name) {
        String tmp = ORI_URL_SUMMONER + name + APIKEYADD + APIKEY;
        return tmp;
    }


    void getSummoner(String url){
        try {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .addHeader("api-key",APIKEY)
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("error + Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String Jdata = response.body().string();

                  //  System.out.println("Response(SUMMONER) Body is " + Jdata);

                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(Jdata);

                                // 처음 이름 검색했을때 나온 json정보 받기
                                name = jsonObject.getString("name");
                                level = jsonObject.getString("summonerLevel");
                                iconId = jsonObject.getString("profileIconId");
                                accountId = jsonObject.getString("accountId");
                                id = jsonObject.getString("id");

                                // 소환사 정보 설정부분
                                System.out.println("iconId is : " + iconId);
                                postIconId = ORI_URL_ICON + iconId + ".png";
                                Glide.with(getApplicationContext()).load(postIconId).into(iv_icon);
                                tv_summonerName.setText(name);
                                tv_summonerLevel.setText("Lv. " + level);

                                // League-v4 URL 생성
                                postId = ORI_URL_LEAGUE + id + APIKEYADD + APIKEY;
                                getLEAGUE(postId);
                                postAccountId = ORI_URL_MATCH + accountId + APIKEYADD2 + APIKEY;
                                getMATCH(postAccountId);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    void getLEAGUE(String url){
        try {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .addHeader("api-key",APIKEY)
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("error + Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String JMdata = response.body().string();

                  //  System.out.println("Response(LEAGUE) Body is " + JMdata);

                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(JMdata);

                                setSummoner(jsonArray);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                System.out.println("실패!");
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    void getMATCH(String url){
        try {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .addHeader("api-key",APIKEY)
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("error + Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String JMdata = response.body().string();

                  //  System.out.println("Response(MATCH) Body is " + JMdata);

                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(JMdata);
                                JSONArray jsonArray = jsonObject.getJSONArray("matches");


                                for(int i = 0; i < jsonArray.length(); i++){
                                    String championKey = jsonArray.getJSONObject(i).getString("champion");
                                    String tmpSign = "cpimg" + championKey;
                                    int lid = getResources().getIdentifier(tmpSign,"drawable",getPackageName());


                                    String gameId = jsonArray.getJSONObject(i).getString("gameId");
                                    postGameId = ORI_URL_GAME + gameId + APIKEYADD + APIKEY;
                                    getGame(postGameId, championKey);

                                    rcds.add(new Record(lid,championKey,gameWin,kill,death,assist));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                System.out.println("실패!");
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    void getGame(String url, final String championKey){
        try {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .addHeader("api-key",APIKEY)
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("error + Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String JMdata = response.body().string();

                   // System.out.println("Response(GAME) Body is " + JMdata);

                    try {
                        JSONObject jsonObject = new JSONObject(JMdata);
                        JSONArray participants = jsonObject.getJSONArray("participants");

                        for(int i = 0; i < participants.length(); i++){
                            if(participants.getJSONObject(i).getString("championId").equals(championKey)){
                                JSONObject stats = participants.getJSONObject(i).getJSONObject("stats");
                                gameWin = stats.getString("win");
                                kill = stats.getString("kills");
                                death = stats.getString("deaths");
                                assist = stats.getString("assists");
                                System.out.println("gameWin : " + gameWin + ", kill : " + kill + ", death : " + death + ", assist : " + assist);

                                for(int k = 0; k <rcds.size(); k++){
                                    if(rcds.get(k).getChampionKey() == championKey){
                                        rcds.get(k).setKills(kill);
                                        rcds.get(k).setDeaths(death);
                                        rcds.get(k).setAssists(assist);
                                        rcds.get(k).setWins(gameWin);
                                    }
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("실패!");
                    }
                }
            });
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }


    private void splash() { // 동기식으로 해결할 수 있음
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("다끝나는 시점");
                adapter = new RecordAdapter(
                        getApplicationContext(),
                        R.layout.listview_item,
                        rcds
                );

                list_record.setAdapter(adapter);
            }
        }, 2700);
    }

    public void setSummoner(JSONArray jsonArray){
        try {
            System.out.println("size : " + jsonArray.length());

            if(jsonArray.length() == 0){
                String tmpWInRate = "배치중";
                String tierName = "UNRANK";
                String rankLP = "0 LP";
                String winRate = "0승 0패 (0.00%)";
                tv_summonerWinrate.setText(tmpWInRate);
                iv_soloRankImg.setImageResource(R.drawable.tier_unrank);
                iv_freeRankImg.setImageResource(R.drawable.tier_unrank);
                tv_soloRankTierName.setText(tierName);
                tv_freeRankTierName.setText(tierName);
                tv_freeRankLP.setText(rankLP);
                tv_soloRankLP.setText(rankLP);
                tv_soloRankWinRate.setText(winRate);
                tv_freeRankWinRate.setText(winRate);

            }
            else if(jsonArray.length() == 1){
                wins = jsonArray.getJSONObject(0).getString("wins");
                losses = jsonArray.getJSONObject(0).getString("losses");
                tier = jsonArray.getJSONObject(0).getString("tier");
                rank = jsonArray.getJSONObject(0).getString("rank");
                queueType = jsonArray.getJSONObject(0).getString("queueType");
                leaguePoint = jsonArray.getJSONObject(0).getString("leaguePoints");

                System.out.println(queueType);

                if (queueType.equals("RANKED_SOLO_5x5")) {

                    String tmpWInRate = "솔로랭크 : " + wins + "승 " + losses + "패";
                    String tierName = tier + " " + rank;
                    String LP = leaguePoint + " LP";
                    String WinRate = wins + "승 " + losses + "패";

                    System.out.println("tmpWinRate is : " + tmpWInRate);
                    tv_summonerWinrate.setText(tmpWInRate);
                    tv_soloRankTierName.setText(tierName);
                    tv_soloRankLP.setText(LP);
                    tv_soloRankWinRate.setText(WinRate);
                    if(tier.equals("BRONZE")){
                        iv_soloRankImg.setImageResource(R.drawable.tier_bronze);
                    }
                    else if(tier.equals("SILVER")){
                        iv_soloRankImg.setImageResource(R.drawable.tier_silver);
                    }
                    else if(tier.equals("GOLD")){
                        iv_soloRankImg.setImageResource(R.drawable.tier_gold);
                    }
                    else if(tier.equals("PLATINUM")){
                        iv_soloRankImg.setImageResource(R.drawable.tier_platinum);
                    }
                    else if(tier.equals("DIAMOND")){
                        iv_soloRankImg.setImageResource(R.drawable.tier_diamond);
                    }
                    else if(tier.equals("MASTER")){
                        iv_soloRankImg.setImageResource(R.drawable.tier_master);
                    }
                    else if(tier.equals("GRANDMASTER")){
                        iv_soloRankImg.setImageResource(R.drawable.tier_grandmaster);
                    }
                    else if(tier.equals("IRON")){
                        iv_soloRankImg.setImageResource(R.drawable.tier_iron);
                    }
                    else{
                        iv_soloRankImg.setImageResource(R.drawable.tier_challenger);
                    }

                    iv_freeRankImg.setImageResource(R.drawable.tier_unrank);
                    tv_freeRankTierName.setText("UNRANK");
                    tv_freeRankLP.setText("0 LP");
                    tv_freeRankWinRate.setText("0승 0패(0.00%)");

                } else {
                    String tmpWInRate = "자유랭크 : " + wins + "승 " + losses + "패";
                    String tierName = tier + " " + rank;
                    String LP = leaguePoint + " LP";
                    String WinRate = wins + "승 " + losses + "패";

                    System.out.println("tmpWinRate is : " + tmpWInRate);
                    tv_summonerWinrate.setText(tmpWInRate);
                    tv_freeRankTierName.setText(tierName);
                    tv_freeRankLP.setText(LP);
                    tv_freeRankWinRate.setText(WinRate);
                    if(tier.equals("BRONZE")){
                        iv_freeRankImg.setImageResource(R.drawable.tier_bronze);
                    }
                    else if(tier.equals("SILVER")){
                        iv_freeRankImg.setImageResource(R.drawable.tier_silver);
                    }
                    else if(tier.equals("GOLD")){
                        iv_freeRankImg.setImageResource(R.drawable.tier_gold);
                    }
                    else if(tier.equals("PLATINUM")){
                        iv_freeRankImg.setImageResource(R.drawable.tier_platinum);
                    }
                    else if(tier.equals("DIAMOND")){
                        iv_freeRankImg.setImageResource(R.drawable.tier_diamond);
                    }
                    else if(tier.equals("MASTER")){
                        iv_freeRankImg.setImageResource(R.drawable.tier_master);
                    }
                    else if(tier.equals("GRANDMASTER")){
                        iv_freeRankImg.setImageResource(R.drawable.tier_grandmaster);
                    }
                    else if(tier.equals("IRON")){
                        iv_freeRankImg.setImageResource(R.drawable.tier_iron);
                    }
                    else{
                        iv_freeRankImg.setImageResource(R.drawable.tier_challenger);
                    }

                    iv_soloRankImg.setImageResource(R.drawable.tier_unrank);
                    tv_soloRankTierName.setText("UNRANK");
                    tv_soloRankLP.setText("0 LP");
                    tv_soloRankWinRate.setText("0승 0패(0.00%)");
                }
            }
            else if(jsonArray.length() == 2){
                for(int i = 0; i <jsonArray.length(); i++){
                    wins = jsonArray.getJSONObject(i).getString("wins");
                    losses = jsonArray.getJSONObject(i).getString("losses");
                    tier = jsonArray.getJSONObject(i).getString("tier");
                    rank = jsonArray.getJSONObject(i).getString("rank");
                    queueType = jsonArray.getJSONObject(i).getString("queueType");
                    leaguePoint = jsonArray.getJSONObject(i).getString("leaguePoints");

                    if (queueType.equals("RANKED_SOLO_5x5")) {

                        String tmpWInRate = "솔로랭크 : " + wins + "승 " + losses + "패";
                        String tierName = tier + " " + rank;
                        String LP = leaguePoint + " LP";
                        String WinRate = wins + "승 " + losses + "패";

                        System.out.println("tmpWinRate is : " + tmpWInRate);
                        tv_summonerWinrate.setText(tmpWInRate);
                        tv_soloRankTierName.setText(tierName);
                        tv_soloRankLP.setText(LP);
                        tv_soloRankWinRate.setText(WinRate);
                        if(tier.equals("BRONZE")){
                            iv_soloRankImg.setImageResource(R.drawable.tier_bronze);
                        }
                        else if(tier.equals("SILVER")){
                            iv_soloRankImg.setImageResource(R.drawable.tier_silver);
                        }
                        else if(tier.equals("GOLD")){
                            iv_soloRankImg.setImageResource(R.drawable.tier_gold);
                        }
                        else if(tier.equals("PLATINUM")){
                            iv_soloRankImg.setImageResource(R.drawable.tier_platinum);
                        }
                        else if(tier.equals("DIAMOND")){
                            iv_soloRankImg.setImageResource(R.drawable.tier_diamond);
                        }
                        else if(tier.equals("MASTER")){
                            iv_soloRankImg.setImageResource(R.drawable.tier_master);
                        }
                        else if(tier.equals("GRANDMASTER")){
                            iv_soloRankImg.setImageResource(R.drawable.tier_grandmaster);
                        }
                        else if(tier.equals("IRON")){
                            iv_soloRankImg.setImageResource(R.drawable.tier_iron);
                        }
                        else{
                            iv_soloRankImg.setImageResource(R.drawable.tier_challenger);
                        }
                    } else {
                        String tmpWInRate = "자유랭크 : " + wins + "승 " + losses + "패";
                        String tierName = tier + " " + rank;
                        String LP = leaguePoint + " LP";
                        String WinRate = wins + "승 " + losses + "패";

                        System.out.println("tmpWinRate is : " + tmpWInRate);
                        tv_freeRankTierName.setText(tierName);
                        tv_freeRankLP.setText(LP);
                        tv_freeRankWinRate.setText(WinRate);
                        if(tier.equals("BRONZE")){
                            iv_freeRankImg.setImageResource(R.drawable.tier_bronze);
                        }
                        else if(tier.equals("SILVER")){
                            iv_freeRankImg.setImageResource(R.drawable.tier_silver);
                        }
                        else if(tier.equals("GOLD")){
                            iv_freeRankImg.setImageResource(R.drawable.tier_gold);
                        }
                        else if(tier.equals("PLATINUM")){
                            iv_freeRankImg.setImageResource(R.drawable.tier_platinum);
                        }
                        else if(tier.equals("DIAMOND")){
                            iv_freeRankImg.setImageResource(R.drawable.tier_diamond);
                        }
                        else if(tier.equals("MASTER")){
                            iv_freeRankImg.setImageResource(R.drawable.tier_master);
                        }
                        else if(tier.equals("GRANDMASTER")){
                            iv_freeRankImg.setImageResource(R.drawable.tier_grandmaster);
                        }
                        else if(tier.equals("IRON")){
                            iv_freeRankImg.setImageResource(R.drawable.tier_iron);
                        }
                        else{
                            iv_freeRankImg.setImageResource(R.drawable.tier_challenger);
                        }
                    }
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
