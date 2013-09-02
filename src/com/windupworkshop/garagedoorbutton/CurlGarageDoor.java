package com.windupworkshop.garagedoorbutton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import java.text.DateFormat;
//import java.util.Date;
 
public class CurlGarageDoor extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
      
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curl_garage_door);  
         
        //TextClock tc = (TextClock) findViewById(R.id.textClock1);
        //tc.setText( Content );
        //setContentView(R.layout.textclock);
        //setContentView(R.id.textClock1);
        
        final Button GetServerData = (Button) findViewById(R.id.GetServerData);
          
        GetServerData.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View arg0) {           
                // Use AsyncTask execute Method To Prevent ANR Problem
                // Note: pass something into execute and it will get set to unused here: doInBackground(String... unused)
            	new LongOperation().execute();
            }
        });    
          
    }
      
      
    // Class with extends AsyncTask class    
    private class LongOperation  extends AsyncTask<String, Void, Void> {         
        // Required initialization        
        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(CurlGarageDoor.this);
        TextView uiUpdate = (TextView) findViewById(R.id.output);
        //EditText serverText = (EditText) findViewById(R.id.serverText);
        //String uri = ""+serverText.getText(); 
        String uri = "http://10.10.0.111/button";
        
        protected void onPreExecute() {
            // NOTE: You can call UI Element here.
            //Start Progress Dialog (Message)  
            Dialog.setMessage("Please wait..");
            Dialog.show();
        }
  
        // Call after onPreExecute method
        protected Void doInBackground(String... unused) {             
            /************ Make GET Call To Web Server ***********/
            BufferedReader reader=null;
            
                try
                {           
					// Defined URL  where to send data
					URL url = new URL(uri);
					     
					// Set http.keepAlive system property to false to really disconnect
					System.setProperty("http.keepAlive", "false");
					
					// Send GET request
					URLConnection conn = url.openConnection();
					conn.setUseCaches(false); 
					conn.setRequestProperty("User-Agent", "WindUp Workshop Android GarageDoorOpener/0.1");
					conn.setRequestProperty("Connection","close");
					conn.setConnectTimeout(2000);
					conn.setDoOutput(true);
					conn.setDoOutput(false);
					   
					// Get the server response         
					reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = null;
					 
					// Read Server Response
					while((line = reader.readLine()) != null)
					{
						// Append server response in string
						sb.append(line + "\n");
					}
					 
					// Append Server Response To Content String 
					Content = sb.toString();
                }
                catch(Exception ex)
                {
                    Error = ex.getMessage();
                }
                finally
                {
                    try
                    {
                        reader.close();
                    }
        
                    catch(Exception ex) {}
                }
             
            /*****************************************************/
            return null;
        }
          
        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.
              
            // Close progress dialog
            Dialog.dismiss();
              
            if (Error != null) {                 
                uiUpdate.setText("Error: "+Error);                 
            } else {
            	// Show the response with a timestamp
            	Calendar now = Calendar.getInstance();
            	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
            	String now_stamp = sdf.format(now.getTime());

            	uiUpdate.setText( now_stamp + " - " + Content );
            }
        }
          
    }
     
}