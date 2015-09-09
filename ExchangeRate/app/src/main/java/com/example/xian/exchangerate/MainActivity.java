package com.example.xian.exchangerate;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
    public Intent intent = new Intent();
    public ArrayList<HashMap<String, String>> ItemList= new ArrayList<HashMap<String, String>>();
    public String country, cashBuy, cashSell, ontimeBuy, ontimeSell;
    public Elements trdes, tddes;
    SimpleAdapter adapter;
    String url = "http://rate.bot.com.tw/Pages/Static/UIP003.zh-TW.htm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchData().execute();
        ViewListCallback();
    }

    private class FetchData extends AsyncTask<Void, Void, Void> {

        HashMap<String, String> HashMap;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to website
                Document document = Jsoup.connect(url).get();
                trdes = document.getElementsByTag("tr");
                for(int i=13;i<32;i++) {
                    HashMap = new HashMap<String, String>();
                    tddes = trdes.get(i).select("td");
                    country = tddes.get(0).before("&nbsp").text();
                    cashBuy =tddes.get(1).text();
                    cashSell =tddes.get(2).text();
                    ontimeBuy =tddes.get(3).text();
                    ontimeSell =tddes.get(4).text();
                    HashMap.put("country", country);
                    HashMap.put("cashBuy",  cashBuy);
                    HashMap.put("cashSell",  cashSell);
                    HashMap.put("onTimeBuy",  ontimeBuy);
                    HashMap.put("onTimeSell",  ontimeSell);
                    ItemList.add(HashMap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result){
            getItemViewList();
        }
    }


    private void getItemViewList(){
        adapter = new SimpleAdapter(this, ItemList, R.layout.item_layout, new String[]{"country", "cashBuy", "cashSell", "onTimeBuy", "onTimeSell"}, new int[]{R.id.country, R.id.cashBuy, R.id.cashSell, R.id.onTimeBuy, R.id.onTimeSell});
        ListView list = (ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);
    }

    private void ViewListCallback(){
        ListView list = (ListView)findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.setClass(MainActivity.this, CalculateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("country", ItemList.get(position).get("country"));
                bundle.putString("cashSell", ItemList.get(position).get("cashSell"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
