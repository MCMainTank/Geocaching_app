package com.mcmaintank.geocache.ui.admin;

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

public class ManageGeocacheActivity extends AppCompatActivity {

    private String responseString;
    private OkHttpClient okHttpClient;
    private String username;
    private List list = new ArrayList();
    private List<Geocache> geocacheList = new ArrayList<Geocache>();
    private Geocache geocache;
    private static UserInfoShp userInfoShp;
    private JSONArray jsonArray;
    private MyAdapter adapter = new MyAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_geocache_management);
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
                geocache.setReported(parseInt(jsonObject.getString("reported")));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                geocache.setGeocacheDateOfUpload(sdf.parse(jsonObject.getString("geocacheDateOfUpload")));
                geocacheList.add(geocache);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ListView listView = (ListView) findViewById(R.id.geocache_list);
        adapter = new MyAdapter(geocacheList,ManageGeocacheActivity.this);
        listView.setAdapter(adapter);
    }





    public class MyAdapter extends BaseAdapter {

        private List<Geocache> geocacheList;
        private LayoutInflater inflater;

        public MyAdapter() {
        }

        @SuppressLint("NewApi")
        public MyAdapter(List<Geocache> historyItemsList, Context context) {
            this.geocacheList = historyItemsList;
            this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return geocacheList == null ? 0 : geocacheList.size();
        }

        @Override
        public Geocache getItem(int position) {
            return geocacheList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.report_child_layout, null);
            Geocache geocache = getItem(position);
            TextView tv_id = (TextView) view.findViewById(R.id.id);
            TextView tv_date = (TextView) view.findViewById(R.id.textView1);
            TextView tv_reported = (TextView) view.findViewById(R.id.textView2);
            View item = (View) view.findViewById(R.id.history_item);
            Integer geocacheId = geocache.getGeocacheId();
            Integer geocacheReported = geocache.getReported();
            tv_id.setText("Geocache ID: "+geocacheId);
            tv_reported.setText("Report count: "+geocacheReported);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tv_date.setText("Date of Upload: "+sdf.format(geocache.getGeocacheDateOfUpload()));


            item.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(ManageGeocacheActivity.this, AdminViewGeocacheActivity.class);
                    Bundle bundle = new Bundle();
                    if(geocache!=null){
                        Log.i(TAG,"Fine");
                    }
                    bundle.putInt("geocacheId",geocache.getGeocacheId());
                    bundle.putString("geocacheLocationDescription",geocache.getDescription());
                    bundle.putDouble("geocacheLatitudes",geocache.getLatitudes());
                    bundle.putDouble("geocacheLongitudes",geocache.getLongitudes());
                    bundle.putInt("reported",geocache.getReported());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return view;
        }

    }
}
