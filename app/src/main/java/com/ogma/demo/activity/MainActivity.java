package com.ogma.demo.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ogma.demo.R;
import com.ogma.demo.application.App;
import com.ogma.demo.application.AppSettings;
import com.ogma.demo.enums.URL;
import com.ogma.demo.network.HttpClient;
import com.ogma.demo.network.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DemoAdapter demoAdapter;
    private ArrayList<String> dataSet = new ArrayList<>();
    private App app;
    private NetworkConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (App) getApplication();
        app.setAppSettings(new AppSettings(this));
        connection = new NetworkConnection(this);

        demoAdapter = new DemoAdapter();

        if (prepareExecuteAsync())
            new DemoTask().execute();
    }

    private boolean prepareExecuteAsync() {
        if (connection.isNetworkConnected()) {
            return true;
        } else if (connection.isNetworkConnectingOrConnected()) {
            Toast.makeText(MainActivity.this, "Connection temporarily unavailable", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "You're offline", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private class DemoAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return dataSet.size();
        }

        @Override
        public Object getItem(int i) {
            return dataSet.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.row_demo, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.ivIcon = (ImageView) itemView.findViewById(R.id.iv_demo);
                viewHolder.tvTitle = (TextView) itemView.findViewById(R.id.tv_demo);
                itemView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) itemView.getTag();
            }

            viewHolder.tvTitle.setText(dataSet.get(position));

            return itemView;
        }

        class ViewHolder {
            ImageView ivIcon;
            TextView tvTitle;
        }
    }


    private class DemoTask extends AsyncTask<String, Void, Boolean> {
        private String error_msg = "Server error!";
        private ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        private JSONObject response;

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                JSONObject mJsonObject = new JSONObject();
                mJsonObject.put("user_id", app.getAppSettings().__uId);

                Log.e("Send Obj:", mJsonObject.toString());

                response = HttpClient.SendHttpPost(URL.LOGIN.getURL(), mJsonObject);
                boolean status = response != null && response.getInt("is_error") == 0;
                if (status) {
                    JSONArray jArr = response.getJSONArray("User");
                    //add data to data set
                    for (int i = 0; i < jArr.length(); i++) {
                        dataSet.add(jArr.getJSONObject(i).getString("user_name"));
                    }
                }
                return status;
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                mDialog.dismiss();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean status) {
            mDialog.dismiss();
            if (status) {
                demoAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


