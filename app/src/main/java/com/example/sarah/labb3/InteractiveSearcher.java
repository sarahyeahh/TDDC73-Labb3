package com.example.sarah.labb3;

/*  Sarah Fosse sarfo265
    Malin Wetterskog malwe794
    TDDC73
    Labb 3
*/

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class InteractiveSearcher extends LinearLayout{

    //Variables
    Context c;
    private EditText editText;
    public int id = -1;
    public int previousid = -1;
    LinearLayout listLayout;
    PopupWindow pw;

    //Constructor
    public InteractiveSearcher(Context context) {
        super(context);
        this.c = context;

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        editText = new EditText(context);
        editText.setLayoutParams(layoutParams);

        listLayout = new LinearLayout(context);
        listLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        ScrollView scrollView = new ScrollView(c);
        scrollView.addView(listLayout);

        pw = new PopupWindow(scrollView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        pw.setFocusable(false);
        pw.setOutsideTouchable(true);
        pw.update();

        addView(editText);
        textChange();
    }


    public InteractiveSearcher(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InteractiveSearcher(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InteractiveSearcher(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void textChange() {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                //Get text from input.
                String searchText = editText.getText().toString().toLowerCase();
                pw.dismiss();

                if(searchText.equals("")){
                    //Do nothing
                    pw.dismiss();
                }
                else {
                    id++;
                    //Search
                    new GetText().execute("http://getnames-getnames.a3c1.starter-us-west-1.openshiftapps.com/getnames/" + id + "/" + searchText);
                }
            }
        });
    }


    //Use AsyncTask to get a JSONObject.
    public class GetText extends AsyncTask<String, String, JSONObject>{

        /** progress dialog to show user that the backup is processing. */

        @Override
        protected JSONObject doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            String result ="";

            //Try to connect with server.
            try {
                URL url = new URL(strings[0]);
                System.out.println(url.toString());

                //Open a connection with the url
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                //Read the JSON-file
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String line = null;

                //Writes the lines into the string result
                while((line = br.readLine()) != null){
                    result += line;
                }

                br.close();

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e) {
                e.fillInStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }

            JSONObject json = null;

            //Creates a JSONObject from the string result.
            try {
                json = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Return a JSONObject
            return json;
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            JSONArray jsonArray = null;
            String jsonString = "";
            ArrayList<String> results = new ArrayList<String>();


            try {
                //Creates a JSONArray from the JSON-file
                jsonArray = jsonObject.getJSONArray("result");

                //Creates a string object from the JSON-file
                jsonString = jsonObject.getString("id");

                //Add all the elements from the JSONArray to an Array of strings.
                for(int i = 0; i < jsonArray.length(); i++){
                    results.add(jsonArray.getString(i));
                }

                //Convert the ID to an int.
                id = Integer.parseInt(jsonString);

                //Send the results to createList().
                createList(results);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void createList(final ArrayList<String> results) {

        //Attach the PopUpWindow to editText.
        pw.showAsDropDown(editText);

        //If the new id is not equal to the previous id
        if(previousid != id ) {

            //Remove the list from old search
            listLayout.removeAllViews();

            for (int i = 0; i < results.size(); i++) {

                //Creates a new item in the list with the name.
                final InteractiveList names = new InteractiveList(c, results.get(i), 50, 70);
                names.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                //Add an OnClickListener which adds the current name that is clicked into the input field.
                names.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editText.setText(names.getName());
                    }
                });

                listLayout.setOrientation(LinearLayout.VERTICAL);
                listLayout.addView(names);

                //Previous id is equal to the new id
                previousid = id;

                //Attach the PopUpWindow to editText.
                pw.showAsDropDown(editText);

            }

        }
    }
}