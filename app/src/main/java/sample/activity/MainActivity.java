package sample.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.RecyclerAdapter;
import helper.HttpUrlOperation;
import jdo.Books;
import jdo.HttpJdo;
import listener.RecyclerListener;

import static constants.Constants.ACTION_PERFORMED;
import static constants.Constants.BOOK_OBJECT;
import static constants.Constants.DELETE;
import static constants.Constants.SAVE;
import static constants.Constants.UPDATE;
import static constants.Constants.UPDATE_UI;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String TAG = "MainActivity";
    private ArrayList<Books> mBooksArrayList;
    private HttpJdo httpJdo;
    private HashMap<String, String> mHttpHeaderMap;
    private int mRecyclerViewPosition;
    public static boolean isActive=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        httpJdo = new HttpJdo();
        mHttpHeaderMap = new HashMap<>();
        mHttpHeaderMap.put("Content-Type", "application/json");
        httpJdo.setmHeader(mHttpHeaderMap);
        httpJdo.setmUrl("https://fakerestapi.azurewebsites.net/api/Books");
        httpJdo.setmRequestmethod("GET");
        new AsynchttpUrlConnection().execute(httpJdo);
        mRecyclerView.addOnItemTouchListener(new RecyclerListener(this, new RecyclerListener.onClickListener() {
            @Override
            public void onClick(View view, int position) {
                mRecyclerViewPosition = position;
                Intent bookIntent = new Intent(MainActivity.this, DescriptionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(BOOK_OBJECT, mBooksArrayList.get(position));
                bookIntent.putExtras(bundle);
                startActivityForResult(bookIntent, 101);
            }
        }));
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookIntent = new Intent(MainActivity.this, DescriptionActivity.class);
                startActivityForResult(bookIntent, 101);
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter(UPDATE_UI));

    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive=false;
    }

    public class AsynchttpUrlConnection extends AsyncTask<HttpJdo, String, HttpJdo> {

        @Override
        protected HttpJdo doInBackground(HttpJdo... httpJdos) {
            return new HttpUrlOperation().httpOperation(httpJdos[0]);
        }

        @Override
        protected void onPostExecute(HttpJdo httpJdo) {
            super.onPostExecute(httpJdo);
            getDataFromJsonToArrayList(httpJdo.getmResponsebody());
            mAdapter = new RecyclerAdapter(MainActivity.this, mBooksArrayList);
            mRecyclerView.setAdapter(mAdapter);
        }

        /**
         * get json from api and set it in textview
         */

        private void getDataFromJsonToArrayList(String json) {
            try {
                    JSONArray jsonArray = new JSONArray(json);
                    Books booksJdo;
                    mBooksArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectDetails = jsonArray.getJSONObject(i);
                        booksJdo = new Books();
                        String lId = jsonObjectDetails.getString("ID");
                        String lTitle = jsonObjectDetails.getString("Title");
                        String lDescription = jsonObjectDetails.getString("Description");
                        String lPageCount = jsonObjectDetails.getString("PageCount");
                        String lExcerpt = jsonObjectDetails.getString("Excerpt");
                        String lPublishDate = jsonObjectDetails.getString("PublishDate");
                        booksJdo.setmId(lId);
                        booksJdo.setmTitle(lTitle);
                        booksJdo.setmDescription(lDescription);
                        booksJdo.setmPageCount(lPageCount);
                        booksJdo.setmExcerpt(lExcerpt);
                        booksJdo.setmPublishDate(lPublishDate);
                        mBooksArrayList.add(booksJdo);
                    }
                Log.d(TAG, "onPostExecute: " + mBooksArrayList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Books books = (Books) data.getSerializableExtra(BOOK_OBJECT);
                Log.d(TAG, "onActivityResult: " + books);
                if (data.getStringExtra(ACTION_PERFORMED) != null) {
                    if (data.getStringExtra(ACTION_PERFORMED).equals(UPDATE)) {
                        mBooksArrayList.set(mRecyclerViewPosition, books);
                        mAdapter.notifyItemChanged(mRecyclerViewPosition);
                    } else if (data.getStringExtra(ACTION_PERFORMED).equals(DELETE)) {
                        //int position = mBooksArrayList.indexOf(m);
                        mBooksArrayList.remove(mRecyclerViewPosition);
                        mAdapter.notifyItemRemoved(mRecyclerViewPosition);
                        mAdapter.notifyDataSetChanged();
                    } else if (data.getStringExtra(ACTION_PERFORMED).equals(SAVE)) {
                        mBooksArrayList.add(books);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

        }
    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Books books= (Books) intent.getSerializableExtra(BOOK_OBJECT);
            String id=books.getmId();
            if (intent.getStringExtra(ACTION_PERFORMED) != null) {
                if(intent.getExtras().getString(ACTION_PERFORMED).equals("POST")){
                    mBooksArrayList.add(books);
                    mAdapter.notifyDataSetChanged();
                }
                else if(intent.getExtras().getString(ACTION_PERFORMED).equals("PUT")){
                    Log.d(TAG, "onReceive: "+id);
                    for(int i=0;i<mBooksArrayList.size();i++){
                        if(id.equals(mBooksArrayList.get(i).getmId())){
                            mBooksArrayList.set(i, books);
                            mAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
                else if(intent.getExtras().getString(ACTION_PERFORMED).equals("DELETE")){
                    for(int i=0;i<mBooksArrayList.size();i++) {
                        if (id.equals(mBooksArrayList.get(i).getmId())) {
                            mBooksArrayList.remove(i);
                            mAdapter.notifyItemRemoved(i);
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }
        }
    };
}
