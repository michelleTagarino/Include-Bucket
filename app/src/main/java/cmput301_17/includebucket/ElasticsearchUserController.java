package cmput301_17.includebucket;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

/**
 * ElasticsearchUserController
 *
 * This class controls how UserAccounts are added and retrieved in ElasticSearch.
 */
public class ElasticsearchUserController {

    private static JestDroidClient client;

    /**
     * This method creates a UserAccount instance.
     */
    public static class CreateUser extends AsyncTask<UserAccount, Void, Void> {
        @Override
        protected Void doInBackground(UserAccount... userAccounts) {
            verifySettings();

            for (UserAccount user : userAccounts) {
                Index index = new Index.Builder(user)
                        .index("cmput301f16t17")
                        .type("user")
                        .build();
                try {
                    DocumentResult result = client.execute(index);

                    if (result.isSucceeded())
                    {
                        user.setUserId(result.getId());
                    }
                    else
                    {
                        Log.i("Error", "Elastic search was not able to add the user.");
                    }
                } catch(Exception e){
                    Log.i("Error", "We failed to add a user to elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * This method retrieves a UserAccount.
     */
    public static class RetrieveUser extends AsyncTask<String, Void, UserAccount> {
        @Override
        protected UserAccount doInBackground(String... user) {
            verifySettings();

            UserAccount foundUser = new UserAccount();

            if (user[0].length() != 0) {
                String search_string =
                        "{\"from\":0,\"size\":10000, " +
                                "\"query\": { \"match\": { \"uniqueUserName\": \"" + user[0] + "\" }}}";

                Search search = new Search.Builder(search_string)
                        .addIndex("cmput301f16t17")
                        .addType("user")
                        .build();
                try {
                    SearchResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        foundUser = result.getSourceAsObject(UserAccount.class);
                        foundUser.setLoginStatus(Boolean.TRUE);
                    } else {
                        Log.i("Error", "The search query failed to find any users that matched.");
                    }
                    return foundUser;
                } catch (Exception e) {
                    Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                }
            }
            return null;
        }
    }

    /**
     * Get user from Elasticsearch based on a userId.
     */
    public static class RetrieveUserById extends AsyncTask<String, Void, UserAccount> {
        @Override
        protected UserAccount doInBackground(String... userIds) {
            verifySettings();

            UserAccount user = new UserAccount();

            Get response = new Get.Builder("cmput301f16t17",userIds[0]).type("user").build();

            try {

                DocumentResult result = client.execute(response);

                if(result.isSucceeded())
                {
                    user = result.getSourceAsObject(UserAccount.class);
                }
                else
                {
                    Log.i("Error", "Could not find the user with that ID in Elasticsearch!");
                }
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return user;
        }
    }

    /**
     * This method deletes a user specified by an ID.
     */
    public static class DeleteUser extends AsyncTask<UserAccount, Void, Void> {
        @Override
        protected Void doInBackground(UserAccount... users) {
            verifySettings();

            Delete deleteUser = new Delete
                    .Builder(users[0].getUserId())
                    .index("cmput301f16t17")
                    .type("user")
                    .build();

            try {
                client.execute(deleteUser);
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the Elasticsearch!");
            }

            return null;
        }
    }

    /**
     * Taken from https://github.com/SRomansky/lonelyTwitter/blob/lab7end/app/src/main/java/ca/ualberta/cs/lonelytwitter/ElasticsearchTweetController.java
     * Accessed November 2, 2016
     * Author: sromansky
     */
    private static void verifySettings() {
        if (client == null)
        {
            DroidClientConfig.Builder builder;
            builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
