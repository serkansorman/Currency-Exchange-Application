package com.example.serkan.CurrencyExchange;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    
    private TextView currencyData;
    private ProgressDialog pDialog;
    private StringBuilder stringBuilder;
    private VolleyRequestQueue volleyRequestQueue;
    private final String URL = "https://www.doviz.com/api/v1/currencies/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        volleyRequestQueue = new VolleyRequestQueue(this);
        currencyData =  findViewById(R.id.currencyTextView);
       
    }

    /**
     * Displays loader until get currencies data from URL
     */
    private void showProgress(){
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading Currencies..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Takes currency code from user and add it to URL
     * @return currency url
     */
    private String getCurrencyURL(){

        EditText editText = findViewById(R.id.search);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(URL);
        if(!editText.getText().toString().isEmpty())
            stringBuilder.append(editText.getText().toString().toUpperCase()).append("/latest");

        return stringBuilder.toString();

    }

    /**
     * Parse JSON object and gets currency data then adds it to stringBuilder
     * @param response received JSON object from URL
     * @throws JSONException
     */
    private void displayCurrency(JSONObject response) throws JSONException {


        Double selling = response.getDouble("selling");
        Double buying = response.getDouble("buying");
        Double changeRate = response.getDouble("change_rate");
        String name = response.getString("full_name");
        String code = response.getString("code");

        stringBuilder.append("Name: ").append(name).append("\n");
        stringBuilder.append("Code: ").append(code).append("\n");
        stringBuilder.append("Selling: ").append(selling).append(" TL\n");
        stringBuilder.append("Buying: ").append(buying).append(" TL\n");
        stringBuilder.append("Change Rate: ").append(changeRate).append("\n\n");

    }



    /**
     * Creates a request and gets single currency data as JSON object from URL
     * then parse JSON data and shows it.
     * @param view
     */
    public void loadSingleCurrency(View view) {

        showProgress();

        // Creates a GET http request with given currency URL
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, getCurrencyURL(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        stringBuilder = new StringBuilder();
                        try {
                            displayCurrency(response);

                            //Shows currency data on screen
                            currencyData.setText(stringBuilder.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Invalid code,try again", Toast.LENGTH_SHORT).show();
                    }
                });

        // Adds json object request to queue
        volleyRequestQueue.addToRequestQueue(jsObjRequest);
    }

    /**
     * Creates a request and gets all currency data as JSON array from URL
     * then parse JSON objects in this array and shows currencies.
     * @param view
     */
    public void loadCurrencyList(View view) {

        showProgress();
        stringBuilder = new StringBuilder();

        // Creates a GET http request for getting all currencies data from URL
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, URL+"all/latest", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray responseArray) {
                        Log.i("MainActivity","Response received");
                        pDialog.dismiss();
                        try {
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                displayCurrency(response);
                            }
                            //Shows all currencies data on screen
                            currencyData.setText(stringBuilder.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Log.e("MainActivity",error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                "Something went wrong" , Toast.LENGTH_SHORT).show();

                    }
                });

        // Adds json array request to queue
        volleyRequestQueue.addToRequestQueue(jsArrayRequest);
    }

}