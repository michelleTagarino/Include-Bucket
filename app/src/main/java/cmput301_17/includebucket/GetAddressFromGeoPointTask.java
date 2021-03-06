package cmput301_17.includebucket;

import android.location.Address;
import android.os.AsyncTask;
import android.widget.Toast;

import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;

/**
 * GetAddressFromGeoPointTask
 *
 * This class deals with turning a set of coordinates into an address.
 */

public class GetAddressFromGeoPointTask extends AsyncTask<GeoPoint, Void, String>
        {

        public interface AsyncResponse {
            void processFinish(String output);
        }
        public AsyncResponse delegate = null;
        public GeocoderNominatim geoCoder;

        public GetAddressFromGeoPointTask(AsyncResponse delegate)
        {

            this.delegate = delegate;

        }

        protected String doInBackground(GeoPoint... geopoints)
        {
            String ret;
            try {
                GeocoderNominatim geoCoder = new GeocoderNominatim("Include-Bucket");
                List<Address> addresses = geoCoder.getFromLocation(geopoints[0].getLatitude(), geopoints[0].getLongitude(), 5);
                int lines = addresses.get(0).getMaxAddressLineIndex();

                String extras = addresses.get(0).getExtras().toString().split("=")[4];
                extras = extras.substring(0,extras.length()-2);
                String street = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();

                ret = ""+extras.split(",")[0]+", "+street+", "+city+", "+state;

            }catch(Exception e){
                ret = "Invalid street address";
            }
            return ret;
        }

        @Override
        protected void onProgressUpdate(Void... values) {}

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            delegate.processFinish(result);

        }
    }


