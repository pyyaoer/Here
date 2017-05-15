package zctang.here;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.msg_listview);

        MsgAdapter msgAdapter = new MsgAdapter(this, R.layout.msg_layout);
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

            msgText.setText(msg.getMsg());
            //msgText.setText(position);
            timeText.setText(msg.getTime());
            upvoteText.setText(msg.getUpvote());

            return view;
        }
    };

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
    };
}
