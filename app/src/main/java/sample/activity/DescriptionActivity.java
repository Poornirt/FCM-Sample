package sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import helper.HttpUrlOperation;
import helper.JsonHelper;
import jdo.Books;
import jdo.HttpJdo;

import static constants.Constants.ACTION_PERFORMED;
import static constants.Constants.BOOK_OBJECT;
import static constants.Constants.CLIENT_KEY;
import static constants.Constants.DELETE;
import static constants.Constants.SAVE;
import static constants.Constants.SERVER_KEY;
import static constants.Constants.UPDATE;

public class DescriptionActivity extends AppCompatActivity {

    private EditText id, title, description, pagecount, excerpt, publishdate;
    private Books bookObject;
    private HttpJdo httpJdo;
    private Button actionButton;
    private static TextView responseText;
    private String TAG = "DescriptionActivity";
    private String mActionPerformed,mRequestMethod;
    private JsonHelper mJsonHelper;
    private boolean isSave;
    public static boolean isActive=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_activity);
        id = findViewById(R.id.id);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        pagecount = findViewById(R.id.pagecount);
        excerpt = findViewById(R.id.excerpt);
        publishdate = findViewById(R.id.publishDate);
        actionButton = findViewById(R.id.action);
        responseText = findViewById(R.id.responseBody);
        mJsonHelper = new JsonHelper();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        httpJdo = new HttpJdo();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            bookObject = (Books) bundle.getSerializable(BOOK_OBJECT);

        if (bookObject != null) {
            isSave = false;
            id.setText(bookObject.getmId());
            title.setText(bookObject.getmTitle());
            description.setText(bookObject.getmDescription());
            pagecount.setText(bookObject.getmPageCount());
            excerpt.setText(bookObject.getmExcerpt());
            publishdate.setText(bookObject.getmPublishDate());
        } else {
            isSave = true;
            enableEditText();
        }

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionPerformed.equals(UPDATE)) {
                    updateBooks();
                } else {
                    saveBooks();
                }
            }
        });
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


    /**
     * post book details to http
     */

    private void saveBooks() {
        mRequestMethod="POST";
        setDataToTheView();
        httpJdo.setmUrl("https://fakerestapi.azurewebsites.net/api/Books");
        httpJdo.setmRequestmethod("POST");
        HashMap<String, String> contentTypeHashMap = new HashMap<>();
        contentTypeHashMap.put("Content-Type", "application/json");
        httpJdo.setmHeader(contentTypeHashMap);
        httpJdo.setmPayload(mJsonHelper.jsonParser(bookObject, httpJdo.getmRequestmethod()));
        new AsynchttpUrlConnection().execute(httpJdo);
    }

    /**
     * update book details to http
     */

    private void updateBooks() {
        mRequestMethod="PUT";
        setDataToTheView();
        httpJdo.setmUrl("https://fakerestapi.azurewebsites.net/api/Books/" + bookObject.getmId());
        httpJdo.setmRequestmethod("PUT");
        HashMap<String, String> contentTypeHashMap = new HashMap<>();
        contentTypeHashMap.put("Content-Type", "application/json");
        httpJdo.setmHeader(contentTypeHashMap);
        httpJdo.setmPayload(mJsonHelper.jsonParser(bookObject, httpJdo.getmRequestmethod()));
        new AsynchttpUrlConnection().execute(httpJdo);
    }

    /**
     * delete book details
     */

    private void deleteBooks() {
        mRequestMethod="DELETE";
        setDataToTheView();
        httpJdo.setmUrl("https://fakerestapi.azurewebsites.net/api/Books/" + bookObject.getmId());
        httpJdo.setmRequestmethod("DELETE");
        HashMap<String, String> contentTypeHashMap = new HashMap<>();
        contentTypeHashMap.put("Content-Type", "application/json");
        httpJdo.setmHeader(contentTypeHashMap);
        httpJdo.setmPayload(mJsonHelper.jsonParser(bookObject, httpJdo.getmRequestmethod()));
        new AsynchttpUrlConnection().execute(httpJdo);
    }

    /**
     * set data to the view
     */

    private void setDataToTheView() {
        bookObject = new Books();
        bookObject.setmId(id.getText().toString());
        bookObject.setmTitle(title.getText().toString());
        bookObject.setmDescription(description.getText().toString());
        bookObject.setmExcerpt(excerpt.getText().toString());
        bookObject.setmPublishDate(publishdate.getText().toString());
        bookObject.setmPageCount(pagecount.getText().toString());
    }

    /**
     * set response data to textview
     */
    public void responseBody(String pResponseBody) {
        responseText.setText(pResponseBody);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem save = menu.findItem(R.id.save);
        MenuItem update = menu.findItem(R.id.update);
        if (isSave) {
            save.setVisible(true);
        } else {
            update.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                mActionPerformed = UPDATE;
                enableEditText();
                actionButton.setVisibility(View.VISIBLE);
                actionButton.setText("UPDATE");
                break;
            case R.id.delete:
                mActionPerformed = DELETE;
                deleteBooks();
                break;
            case R.id.save:
                mActionPerformed = SAVE;
                saveBooks();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void enableEditText(){
        id.setEnabled(true);
        title.setEnabled(true);
        description.setEnabled(true);
        pagecount.setEnabled(true);
        excerpt.setEnabled(true);
        publishdate.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_PERFORMED, mActionPerformed);
        bundle.putSerializable(BOOK_OBJECT, bookObject);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
        finish();
    }

    private class AsynchttpUrlConnection extends AsyncTask<HttpJdo, String, HttpJdo> {


        @Override
        protected HttpJdo doInBackground(HttpJdo... httpJdos) {
            return new HttpUrlOperation().httpOperation(httpJdos[0]);
        }

        @Override
        protected void onPostExecute(HttpJdo httpJdo) {
            super.onPostExecute(httpJdo);
            responseBody(httpJdo.getmResponsebody());
            if (httpJdo.getmRequestcode() == 200) {
                new AsyncRemoteOperation().execute(httpJdo);
            }
        }
    }

    private class AsyncRemoteOperation extends AsyncTask<HttpJdo, String, HttpJdo> {
        private HttpJdo httpJdoRemote = new HttpJdo();

        @Override
        protected HttpJdo doInBackground(HttpJdo... httpJdos) {
            httpJdoRemote.setmUrl("https://fcm.googleapis.com/fcm/send");
            httpJdoRemote.setmRequestmethod("POST");
            HashMap<String, String> contentTypeHashMap = new HashMap<>();
            contentTypeHashMap.put("Content-Type", "application/json");
            contentTypeHashMap.put("Authorization", SERVER_KEY);
            httpJdoRemote.setmHeader(contentTypeHashMap);
            httpJdoRemote.setmPayload("{" + "\"data\"" + ":" + mJsonHelper.jsonParser(bookObject,mRequestMethod) + "," + "\"to\"" + ":"
                    + "\"" + CLIENT_KEY + "\"" + "}");
            Log.d(TAG, "httpJdoRemote: " + httpJdoRemote.getmRequestmethod());
            return new HttpUrlOperation().httpOperation(httpJdoRemote);
        }

        @Override
        protected void onPostExecute(HttpJdo httpJdo) {
            super.onPostExecute(httpJdo);
        }
    }

}
