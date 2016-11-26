package cmput301_17.includebucket;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by michelletagarino on 16-11-23.
 */
public class DriverProfileActivity extends MainMenuActivity {

    private UserAccount user = new UserAccount();
    private UserController userController = new UserController();
    private DriverRequestsController driverController = new DriverRequestsController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_data);

        user = UserController.getUserAccount();
        driverController.setContext(DriverProfileActivity.this);
        userController.setContext(DriverProfileActivity.this);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                finish();
            }
        });
    }
}
