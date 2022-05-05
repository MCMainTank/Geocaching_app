package com.mcmaintank.geocache.ui.geocache;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mcmaintank.geocache.R;
import com.mcmaintank.geocache.data.model.Geocache;
import com.mcmaintank.geocache.data.model.UserInfoShp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class ShowGeocacheActivity extends AppCompatActivity {

    private String responseString;
    private OkHttpClient okHttpClient;
    private String username;
    private List list = new ArrayList();
    private List<Geocache> geocacheList = new ArrayList<Geocache>();
    private Geocache geocache;
    private static UserInfoShp userInfoShp;
    private JSONArray jsonArray;
    private ShowGeocacheActivity.MyAdapter adapter = new ShowGeocacheActivity.MyAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        this.username = userInfoShp.getUserName(ShowGeocacheActivity.this);
        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("jsonString");

        try {
            jsonArray = new JSONArray(jsonString);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Geocache geocache = new Geocache();
                geocache.setGeocacheId(parseInt(jsonObject.getString("geocacheId")));
                geocache.setDescription(jsonObject.getString("geocacheLocationDescription"));
                geocache.setLatitudes(jsonObject.getDouble("geocacheLatitudes"));
                geocache.setLongitudes(jsonObject.getDouble("geocacheLongitudes"));
                geocache.setDeleted(jsonObject.getBoolean("deleted"));
                geocache.setPid(parseInt(jsonObject.getString("pid")));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                geocache.setGeocacheDateOfUpload(sdf.parse(jsonObject.getString("geocacheDateOfUpload")));
                geocacheList.add(geocache);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ListView listView = (ListView) findViewById(R.id.history_list);
        adapter = new ShowGeocacheActivity.MyAdapter(geocacheList,ShowGeocacheActivity.this);
        listView.setAdapter(adapter);

//        Bundle bundle = intent.getExtras();
//        list = bundle.getParcelableArrayList("list");
//        geocacheList= (ArrayList<Geocache>) list.get(0);
//        System.out.println(geocacheList.get(0).getGeocacheId().toString());
//        GetGeocacheList getGeocacheList = new GetGeocacheList();
//        new Thread(getGeocacheList).start();
//        try {
//            sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
////        new Thread(){
////            @Override
////            public void run(){
////
////            }
////        }.start();
//
//        System.out.println(this.geocacheList.get(0).getGeocacheId().toString());
    }

    //    class GetGeocacheList implements Runnable{
//
//
//        @Override
//        public void run() {
//
//        }
//    }
    public class MyAdapter extends BaseAdapter {

        private List<Geocache> historyItemsList;
        private LayoutInflater inflater;

        public MyAdapter() {
        }

        @SuppressLint("NewApi")
        public MyAdapter(List<Geocache> historyItemsList, Context context) {
            this.historyItemsList = historyItemsList;
            this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return historyItemsList == null ? 0 : historyItemsList.size();
        }

        @Override
        public Geocache getItem(int position) {
            return historyItemsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.history_child_layout, null);
            Geocache geocache = getItem(position);
            TextView tv_name = (TextView) view.findViewById(R.id.geocache_id);
            TextView tv_date = (TextView) view.findViewById(R.id.history_date);
            View item = (View) view.findViewById(R.id.history_item);
            tv_name.setText(geocache.getGeocacheId().toString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tv_date.setText(sdf.format(geocache.getGeocacheDateOfUpload()).toString());

            item.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(ShowGeocacheActivity.this,ViewGeocacheActivity.class);
                    Bundle bundle = new Bundle();
                    if(geocache!=null){
                        Log.i(TAG,"Fine");
                    }
                    bundle.putInt("geocacheId",geocache.getGeocacheId());
                    bundle.putString("geocacheLocationDescription",geocache.getDescription());
                    bundle.putDouble("geocacheLatitudes",geocache.getLatitudes());
                    bundle.putDouble("geocacheLongitudes",geocache.getLongitudes());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return view;
        }

    }
}
