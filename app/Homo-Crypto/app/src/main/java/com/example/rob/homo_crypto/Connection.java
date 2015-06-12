package com.example.rob.homo_crypto;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Connection extends ActionBarActivity  {
    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;
    static EditText num1, num2;
    static TextView btn_result;
    static Spinner spinner;
    static Button send;
    static String op;
    String user_result;
    CustomOnItemSelectedListener l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       l = new CustomOnItemSelectedListener();

        /**
         *
         *
         *          Not that the two seperate section are not integrated yet...
         *
         *          The way it is suppoesd to work is a mobile app connects to a server that the feeds it back a public key generated from an encryption system.
         *
         *          Once the public key is taken in it supposed to encrpyt user data that has already been calculated.
         *
         *          1) the user needs to input two numbers and preform some arithemetic operation on the them.
         *          2) One the result is calculated it is encrypted via public key. This encrypted data is the sent back to the server in order to preform calculation on...Homomorphic Cryptography.
         *
         *
         *
         */

        /**
         *
         *      Connection to Server Code: Buttons for GUI
         *
         */
        setContentView(R.layout.activity_connection2);

        editTextAddress = (EditText)findViewById(R.id.address);
        editTextPort = (EditText)findViewById(R.id.port);
        buttonConnect = (Button)findViewById(R.id.connect);
        buttonClear = (Button)findViewById(R.id.clear);
        textResponse = (TextView)findViewById(R.id.response);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        buttonClear.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                textResponse.setText("");
            }});

        /**
         *
         *      User input and calculation code
         *
         */

        num1 = (EditText) findViewById(R.id.editText_num1);
        num2 = (EditText) findViewById(R.id.editText_num2);
        Button calc = (Button) findViewById(R.id.btn_calc);
        btn_result  =  (TextView)findViewById(R.id.textView_result);
        send = (Button)findViewById(R.id.send);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num1Text = num1.getText().toString();
                String num2Text = num2.getText().toString();
                int result = Integer.parseInt(num1Text) + Integer.parseInt(num2Text);
                //btn_result.setText(Integer.toString(result));
                String op = getOP();
                //THis will be used to send to user input to server....
                user_result = num1Text+ " " + op+ " " + num2Text;
                btn_result.setText(user_result);

            }
        });

        spinner = (Spinner)findViewById(R.id.spinner);
        //spinner.setOnItemSelectedListener(this);
        List<String> list = new ArrayList<String>();
        list.add("+");
        list.add("-");
        list.add("*");
        list.add("/");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,android.R.id.text1,list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

        // Button click Listener
        addListenerOnButton();


    }


    // Add spinner dataOn

    public void addListenerOnSpinnerItemSelection(){

        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }

    //get the selected dropdown list value

    public void addListenerOnButton() {

        spinner = (Spinner) findViewById(R.id.spinner);

        //btnSubmit = (Button) findViewById(R.id.btnSubmit);

        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {



               /* Toast.makeText(Connection.this,
                        "On Button Click : " +
                                "\n" + String.valueOf(spinner.getSelectedItem()),
                        Toast.LENGTH_LONG).show();*/
            }

        });
    }
    public String getOP(){
        return l.getData();

    }


    /**
     *
     *
     *          Network Methodds
     *
     *
     */



    OnClickListener buttonConnectOnClickListener =
            new OnClickListener(){

                @Override
                public void onClick(View arg0) {

                    MyClientTask myClientTask = new MyClientTask(
                            editTextAddress.getText().toString(),
                            Integer.parseInt(editTextPort.getText().toString()));
                    myClientTask.execute();
                }};

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";
        //DataOutputStream dataOutputStream = null;
        BufferedOutputStream dataOutputStream = null;

        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                //dataOutputStream = new DataOutputStream(socket.getOutputStream());
                //dataOutputStream.writeUTF(user_result);
                dataOutputStream = new BufferedOutputStream(socket.getOutputStream());
                dataOutputStream.write(user_result.getBytes());
                dataOutputStream.flush();


                /**
                 * This has the abillity to recieve data from the server, but it is not neccessary for right now.
                 *
                 * I am keeping in case I need it in the future..
                 *
                 */
                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

    /*
     * notice:
     * inputStream.read() will block if no data return
     */
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);

                   //THis will udpdate the UI with the text from the server
                    //uncomment to pass info from the server to the UI...
                    //response += byteArrayOutputStream.toString("UTF-8");
                }

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textResponse.setText(response);
            super.onPostExecute(result);
        }

    }



public void connect(){
    Toast.makeText(getApplicationContext(),"Connecting...",Toast.LENGTH_LONG).show();
    final String DEBUG_TAG = "HttpExample";

    InputStream is = null;
    // Only display the first 500 characters of the retrieved
    // web page content.
    int len = 500;

    try {
        URL url = new URL("10.107.237.17:1000");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        int response = conn.getResponseCode();
        Log.d(DEBUG_TAG, "The response is: " + response);
        is = conn.getInputStream();

        // Convert the InputStream into a string
        // String contentAsString = readIt(is, len);
        //return contentAsString;

        // Makes sure that the InputStream is closed after the app is
        // finished using it.
    }catch (Exception e) {
        e.printStackTrace();
    }/*finally {
            if (is != null) {
                is.close();
            }
        }*/
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
