package zctang.here;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    // Fill in the URL of server
    private static String mURL = "";
    MsgAdapter msgAdapter;
    String bestProvider;
    private String TAG = MainActivity.class.getSimpleName();
    private ListView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Location currentLocation;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.v(TAG, "IN ON LOCATION CHANGE, lat=" + latitude + ", lon=" + longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(TAG, "Permission check failed: Network | GPS");
            return;
        } else {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            bestProvider = locationManager.getBestProvider(criteria, false);
            locationManager.requestLocationUpdates(bestProvider, 1000, 1, locationListener);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.include);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListView = (ListView) findViewById(R.id.msg_listview);

        msgAdapter = new MsgAdapter(this, R.layout.msg_layout);
        msgAdapter.add(new Msg("First", "1:00", "upvote"));
        msgAdapter.add(new Msg("Second", "2:00", "upvote"));

        mListView.setAdapter(msgAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
            locationManager = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(TAG, "Permission check failed: Network | GPS");
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 10000, 1, locationListener);
        currentLocation = locationManager.getLastKnownLocation(bestProvider);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        new GetMsgs().execute();
    }

    class MsgAdapter extends ArrayAdapter<Msg> {
        private int mResourceId;

        public MsgAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            this.mResourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Msg msg = getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(mResourceId, parent, false);
            TextView msgText = (TextView) view.findViewById(R.id.msg_content);
            TextView timeText = (TextView) view.findViewById(R.id.time_content);
            TextView upvoteText = (TextView) view.findViewById(R.id.upvote_content);

            if (msg != null) {
                msgText.setText(msg.getMsg());
                timeText.setText(msg.getTime());
                upvoteText.setText(msg.getUpvote());
            }

            return view;
        }
    }

    class Msg{
        private String msg;
        private String time;
        private String upvote;

        public Msg(String content, String time, String upvote) {
            this.msg = content;
            this.time = time;
            this.upvote = upvote;
        }

        public String getMsg() {
            return msg;
        }

        public String getTime() {
            return time;
        }

        public String getUpvote() {
            return upvote;
        }
    }

    private class GetMsgs extends AsyncTask<Void, Void, Void> {

        private JSONArray jsonArray = null;

        @Override
        protected void onPreExecute() {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler httpHandler = new HttpHandler();
            String jsonStr = httpHandler.makeServiceCall(mURL);

            if (jsonStr != null) {
                try {
                    jsonArray = new JSONArray(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (jsonArray != null) {
                msgAdapter.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        msgAdapter.add(new Msg(jsonObject.getString("content"),
                                jsonObject.getString("time"),
                                jsonObject.getString("upvote")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            // TODO: show Location information in the last Msg
            if (currentLocation != null) {
                msgAdapter.add(new Msg(currentLocation.toString(), "0:00", "0"));
            }
            msgAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
