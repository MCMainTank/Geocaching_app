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
import com.mcmaintank.geocache.data.model.User;
import com.mcmaintank.geocache.data.model.UserInfoShp;
import com.mcmaintank.geocache.ui.geocache.ViewGeocacheActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageUserActivity extends AppCompatActivity {

    private List<Geocache> geocacheList = new ArrayList<Geocache>();
    private Geocache geocache;
    private static UserInfoShp userInfoShp;
    private JSONArray jsonArray;
    private MyAdapter adapter = new MyAdapter();
    private List<User> userList = new ArrayList<User>();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_management);
        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("jsonString");
        try {
            jsonArray = new JSONArray(jsonString);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                User user = new User();
                user.setUserId(Long.parseLong(jsonObject.getString("userId")));
                user.setUserName(jsonObject.getString("userName"));
                user.setReported(parseInt(jsonObject.getString("reported")));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ListView listView = (ListView) findViewById(R.id.user_list);
        adapter = new MyAdapter(userList,ManageUserActivity.this);
        listView.setAdapter(adapter);
    }


    public class MyAdapter extends BaseAdapter {

        private List<User> userList;
        private LayoutInflater inflater;

        public MyAdapter() {
        }

        @SuppressLint("NewApi")
        public MyAdapter(List<User> userList, Context context) {
            this.userList = userList;
            this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return userList == null ? 0 : userList.size();
        }

        @Override
        public User getItem(int position) {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.report_child_layout, null);
            User user = getItem(position);
            TextView tv_id = (TextView) view.findViewById(R.id.id);
            TextView tv_username = (TextView) view.findViewById(R.id.textView1);
            TextView tv_reported = (TextView) view.findViewById(R.id.textView2);
            View item = (View) view.findViewById(R.id.history_item);
            tv_id.setText("User ID: "+user.getUserId().toString());
            tv_username.setText("Name: "+user.getUserName());
            tv_reported.setText("Reported count: "+user.getReported().toString());

            item.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(ManageUserActivity.this, ViewGeocacheActivity.class);
                    Bundle bundle = new Bundle();
                    if(user!=null){
                        Log.i(TAG,"Fine");
                    }

//                    bundle.putInt("geocacheId",geocache.getGeocacheId());
//                    bundle.putString("geocacheLocationDescription",geocache.getDescription());
//                    bundle.putDouble("geocacheLatitudes",geocache.getLatitudes());
//                    bundle.putDouble("geocacheLongitudes",geocache.getLongitudes());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return view;
        }

    }
}
