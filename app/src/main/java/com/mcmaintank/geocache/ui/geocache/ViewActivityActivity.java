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
import com.mcmaintank.geocache.data.model.Activity;
import com.mcmaintank.geocache.data.model.Geocache;
import com.mcmaintank.geocache.data.model.UserInfoShp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class ViewActivityActivity extends AppCompatActivity {

    private String responseString;
    private OkHttpClient okHttpClient;
    private String username;
    private List list = new ArrayList();
    private List<Activity> activitiesList = new ArrayList<Activity>();
    private Activity activity;
    private static UserInfoShp userInfoShp;
    private JSONArray jsonArray;
    private ViewActivityActivity.MyAdapter adapter = new ViewActivityActivity.MyAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        this.username = userInfoShp.getUserName(ViewActivityActivity.this);
        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("jsonString");
        try {
            jsonArray = new JSONArray(jsonString);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Activity activity = new Activity();
                activity.setActivityId(parseInt(jsonObject.getString("activityId")));
                activity.setActivityType(jsonObject.getString("activityType"));
                activity.setGeocacheId(parseInt(jsonObject.getString("geocacheId")));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                activity.setActivityDateOfUpload(sdf.parse(jsonObject.getString("activityDateOfUpload")));
                activitiesList.add(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ListView listView = (ListView) findViewById(R.id.history_list);
        adapter = new MyAdapter(activitiesList,ViewActivityActivity.this);
        listView.setAdapter(adapter);


//        System.out.println(activitiesList.get(0).getActivityId().toString());

    }


    public class MyAdapter extends BaseAdapter {

        private List<Activity> activityItemsList;
        private LayoutInflater inflater;

        public MyAdapter() {
        }

        @SuppressLint("NewApi")
        public MyAdapter(List<Activity> activityItemsList, Context context) {
            this.activityItemsList = activityItemsList;
            this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return activityItemsList == null ? 0 : activityItemsList.size();
        }

        @Override
        public Activity getItem(int position) {
            return activityItemsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.activity_child_layout, null);
            Activity activity = getItem(position);
            TextView tv_name = (TextView) view.findViewById(R.id.activity_id);
            TextView tv_geocacheId = (TextView) view.findViewById(R.id.activity_geocache_id);
            TextView tv_date = (TextView) view.findViewById(R.id.activity_date);
            TextView tv_type = (TextView) view.findViewById(R.id.activity_type);
            View item = (View) view.findViewById(R.id.history_item);
            tv_name.setText("Activity ID: "+activity.getActivityId().toString());
            tv_geocacheId.setText("Geocache ID: "+activity.getGeocacheId().toString());
            tv_type.setText(activity.getActivityType());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tv_date.setText(sdf.format(activity.getActivityDateOfUpload()).toString());


            return view;
        }

    }

}
