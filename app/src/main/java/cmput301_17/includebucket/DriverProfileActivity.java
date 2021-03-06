package cmput301_17.includebucket;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * DriverProfileActivity
 *
 * An activity for viewing a driver's profile.
 */
public class DriverProfileActivity extends MainMenuActivity {

    private UserAccount user = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_data);

        UserFileManager.initManager(this.getApplicationContext());
        DriverRequestsFileManager.initManager(this.getApplicationContext());

        user = UserController.getUserAccount();

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
