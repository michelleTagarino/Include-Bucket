package cmput301_17.includebucket;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

/**
 * DriverBrowseRequestsActivity
 *
 * Activity for dealing with browsing requests as a driver.
 *
 */
public class DriverBrowseRequestsActivity extends MainMenuActivity {

    private String key;
    private EditText keyword;
    private boolean keywordFound = Boolean.FALSE;
    private ListView browseRequestList;
    private ArrayList<Request> requestList;
    private ArrayAdapter<Request> requestAdapter;
    private Collection<Request> requests;
    private UserAccount user = new UserAccount();
    private ConnectivityManager connectivityManager;
    private boolean connected;

    /**
     * Controls the list of requests and handles button clicks.
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_requests);

        UserFileManager.initManager(this.getApplicationContext());
        DriverRequestsFileManager.initManager(this.getApplicationContext());

        keyword = (EditText) findViewById(R.id.keyword);
        browseRequestList = (ListView) findViewById(R.id.browseRequestList);

        user = UserController.getUserAccount();

        /**
         * Taken from: http://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
         * Accessed: November 26, 2016
         * Author: binnyb
         */
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            connected = true;
        }
        else connected = false;


        if (connected)
        {
            DriverRequestsController.loadOpenRequestsFromElasticsearch();
        }

        requests = DriverRequestsController.getDriverRequests().getRequests();
        requestList = new ArrayList<>();
        requestList.addAll(requests);
        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        browseRequestList.setAdapter(requestAdapter);
        requestAdapter.notifyDataSetChanged();

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                requestList.clear();

                if (keyword.getText().toString() == null)
                {
                    key = "";
                }
                else key = keyword.getText().toString();

                DriverRequestsController.loadRequestsByKeyword(key);

                requests = DriverRequestsController.getDriverRequests().getRequests();
                requestList.addAll(requests);
                Log.i("SIZE", "" + requestList.size());
                browseRequestList.setAdapter(requestAdapter);
                requestAdapter.notifyDataSetChanged();
            }
        });

        DriverRequestsController.getDriverRequests().addListener(new Listener() {
            @Override
            public void update() {
                requestList.clear();
                Collection<Request> requests = DriverRequestsController.getDriverRequests().getRequests();
                requestList.addAll(requests);
                requestAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        requestAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestAdapter.notifyDataSetChanged();

        browseRequestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(DriverBrowseRequestsActivity.this, DriverSingleRequestActivity.class);
                Request request =  requestList.get(position);
                intent.putExtra("Request", request);
                startActivity(intent);
            }
        });
    }
}