package com.t_robop.smaon;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends Activity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private static final int ADDRESSLOADER_ID = 0;
    String str="aaa";
    String str2="bbb";


    int Level = 0;


 //   Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences datasy = getSharedPreferences("DataSave",MODE_PRIVATE);    //sharedPreference
        Level = datasy.getInt("sStart",0);




        if(Level==0){   //初回起動判定→設定画面に飛ばす。


            Intent setIntent =new Intent (this,SettingActivity.class);

            startActivity(setIntent); //settingActivity変遷

        }else {

            getLoaderManager().restartLoader(ADDRESSLOADER_ID, null, this); //スレッド処理開始



        }


    }

        @Override
        public Loader<JSONObject> onCreateLoader ( int id, Bundle args){


            String url = "http://192.168.1.31";

            return new AsyncWorker(this, url);
        }

        @Override
        public void onLoadFinished (Loader < JSONObject > loader, JSONObject data){

            if (data != null) {

                try {

                    JSONArray jsonArray = data.getJSONArray("pidatas"); //pidatasの配列参照

                    JSONObject object = jsonArray.getJSONObject(0); //一番初めのデータを参照,0はdate,1はセルシウス
                    JSONObject obj1 = jsonArray.getJSONObject(1);
                    //   String str = (String) object.get("name");
                    str = object.getString("date");   //strにdate or celsius を代入
                    str2 = obj1.getString("celsius");

                    Log.d("JSONObject", str);

                 //   str1 = str2;
                    //String date = jsonObject.getString("name");
                    Log.d("JSONObject", "JSONObject");

                } catch (JSONException e) {
                    Log.d("onLoadFinished", "JSONのパースに失敗しました。 JSONException=" + e);
                }


            } else {
                Log.d("onLoadFinished", "onLoadFinished error!");
            }

            //nullデータを受け取った時の処理
            if(str.equals("aaa") || str2.equals("bbb")){
                str="xxx";
                str2="0.0";
            }

            Intent sIntent = new Intent();      //インテント生成

            sIntent.putExtra("date",str);       //日付を送っている
            sIntent.putExtra("temper",str2);        //温度データを送っている
            sIntent.setClass(this,MainActivity.class);


            // MainActivity の起動
            startActivity(sIntent);

            StartActivity.this.finish();

        }

        @Override
        public void onLoaderReset (Loader < JSONObject > loader) {


        }

    }
