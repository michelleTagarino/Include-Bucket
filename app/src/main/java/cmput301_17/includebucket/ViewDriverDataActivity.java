package cmput301_17.includebucket;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * ViewDriverDataActivity
 *
 * This is a view to see the driver's data. It shows contact information: phone number and email address.
 */
public class ViewDriverDataActivity extends MainMenuActivity {

    private TextView loginTextView;
    private Button acceptButton, emailButton, phoneButton;
    private UserAccount driver = new UserAccount();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_driver_data);


        UserFileManager.initManager(this.getApplicationContext());

        final UserAccount driver = (UserAccount) getIntent().getSerializableExtra("User");

        final Request request = (Request) getIntent().getSerializableExtra("Request");

        loginTextView = (TextView) findViewById(R.id.loginTextView);
        emailButton = (Button) findViewById(R.id.emailTextView);
        phoneButton = (Button) findViewById(R.id.phoneTextView);

        acceptButton = (Button) findViewById(R.id.confirmButton);


        String login = driver.getUniqueUserName();
        String email = driver.getEmail();
        String phone = driver.getPhoneNumber();

        loginTextView.setText(login);
        emailButton.setText(email);
        phoneButton.setText(phone);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                request.setRequestStatus(Request.RequestStatus.Closed);

                request.chooseDriver(driver);

                request.setRiderAccepted(true); // Eventually we won't need these booleans, but they are kept for testing purposes
                request.chooseDriver(driver);
                DriverRequestsController.deleteRequest(request);
                ElasticsearchRequestController.CreateRequest createRequest;
                createRequest = new ElasticsearchRequestController.CreateRequest();
                createRequest.execute(request);
                // Do not delete request yet
                // US 05.03.01 As a driver, I want to see if my acceptance was accepted.
                // This means that when the driver goes to his Current requests (DriverCurrentRequestActivity)
                // the Driver has to see that the request is now closed and was confirmed by the rider
                // i.e. "Confirmed by... [rider's login]"
                // This is not the same as a notification

                Toast.makeText(ViewDriverDataActivity.this, "Request Confirmed", Toast.LENGTH_SHORT).show();

                finish();
            }


        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {driver.getEmail()});
                startActivity(intent);

            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + driver.getPhoneNumber()));
                startActivity(intent);

            }
        });

    }
}