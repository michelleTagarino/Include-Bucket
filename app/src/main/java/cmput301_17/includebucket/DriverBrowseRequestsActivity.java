package cmput301_17.includebucket;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by michelletagarino on 16-10-20.
 *
 * Activity for dealing with browsing requests as a driver.
 *
 */
public class DriverBrowseRequestsActivity extends MainMenuActivity {

    private String key;
    private EditText keyword;
    private ListView browseRequestList;
    private ArrayList<Request> requestList;
    private ArrayAdapter<Request> requestAdapter;
    private Collection<Request> requests;

    /**
     * Controls the list of requests and handles button clicks.
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_requests);

        keyword  = (EditText) findViewById(R.id.keyword);
        browseRequestList = (ListView) findViewById(R.id.browseRequestList);

        requestList = new ArrayList<>();
        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        browseRequestList.setAdapter(requestAdapter);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                requestList.clear();
                key = keyword.getText().toString();
                requests = RequestListController.getKeywordList(key);
                requestList.addAll(requests);
                browseRequestList.setAdapter(requestAdapter);
                requestAdapter.notifyDataSetChanged();
            }
        });

        RequestListController.getKeywordList(key).addListener(new Listener() {
            @Override
            public void update() {
                requestList.clear();
                Collection<Request> requests = RequestListController.getKeywordList(key).getRequests();
                requestList.addAll(requests);
                requestAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
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