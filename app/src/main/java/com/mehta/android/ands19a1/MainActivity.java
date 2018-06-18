package com.mehta.android.ands19a1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mehta.android.ands19a1.network.CallAddr;
import com.mehta.android.ands19a1.network.NetworkStatus;
import com.mehta.android.ands19a1.network.OnWebServiceResult;
import com.mehta.android.ands19a1.utils.CommonUtilities;
import com.squareup.okhttp.FormEncodingBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity implements OnWebServiceResult {

    //String url= "http://samples.openweathermap.org/data/2.5/weather?q=Goa,India&appid=b1b15e88fa797225412429c1c50c122a1";
    String url= "https://api.darksky.net/forecast/a799c95b1bb72d6b6b9dbd4df0531597/15.486756,%2073.821340?exclude=flags,minutely,hourly,daily,alerts,flags";
    // https://darksky.net/dev/docs#/dev/docs#api-request-types -- since openweathermap is not working properly...i am using another api

    TextView txtWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtWeather =(TextView)this.findViewById(R.id.txtWeather);

        hitRequest();
    }

    private void hitRequest(){

        FormEncodingBuilder parameters= new FormEncodingBuilder();
        parameters.add("page" , "1");
        if(NetworkStatus.getInstance(this).isConnectedToInternet()) {
            Toast.makeText(this, "Yes Network!!", Toast.LENGTH_SHORT).show();
            CallAddr call = new CallAddr(this, url,parameters, CommonUtilities.SERVICE_TYPE.GET_DATA, this);
            call.execute();
        }else {
            Toast.makeText(this, "No Network!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getWebResponse(String result, CommonUtilities.SERVICE_TYPE type) {
        Log.e("five", "type= "+ type+ " res= "+ result);
        try {
            JSONObject obj= new JSONObject(result);
            JSONObject jobj= obj.getJSONObject("currently");
            Log.e("six",  jobj.toString());
            double temp = jobj.getDouble("temperature");
            Log.e("six", Double.toString(temp));

            temp = (temp-32);
            temp = temp * 5/9;
            temp = round(temp);

            Log.e("seven", Double.toString(temp));

            txtWeather.setText("Goa Weather " + Double.toString(temp) + " degree");


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
