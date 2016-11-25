package cmput301_17.includebucket;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * User data controller?
 *
 */
public class EditUserDataActivity extends MainMenuActivity {


    protected EditText userName, userEmail, userPhone, vehicleMake, vehicleModel, vehicleYear;

    UserAccount user = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_data);

        user = (UserAccount) getIntent().getSerializableExtra("User");

        userEmail = (EditText) findViewById(R.id.editEmail);
        userPhone = (EditText) findViewById(R.id.editPhone);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                editUser();
                finish();
            }
        });
    }
    public void editUser() {

        String login = user.getUniqueUserName();

        String email, phone, make, model, year;

        if (userEmail.getText().toString().equals("")){
             email = user.getEmail();
        } else{
             email = userEmail.getText().toString();
        }
        if (userPhone.getText().toString().equals("")){
            phone = user.getPhoneNumber();
        } else{
            phone = userPhone.getText().toString();
        }
        if (vehicleMake.getText().toString().equals("")){
            make = user.getVehicleMake();
        } else{
            make = vehicleMake.getText().toString();
        }
        if (vehicleModel.getText().toString().equals("")){
            model = user.getVehicleModel();
        } else{
            model = vehicleModel.getText().toString();
        }
        if (vehicleYear.getText().toString().equals("")){
            year = user.getVehicleYear();
        } else{
            year = vehicleYear.getText().toString();
        }

        UserAccount user = new UserAccount(login, email, phone, make, model, year);

        ElasticsearchUserController.CreateUser editUser;
        editUser = new ElasticsearchUserController.CreateUser();
        editUser.execute(user);
    }
}