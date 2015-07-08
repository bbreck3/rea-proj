package com.example.homocrypto;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class HomomorphicCrypto extends ActionBarActivity {
    static EditText num1, num2;
    static TextView btn_result;
    static Spinner spinner;
    static Button send;
    static String num1_bin,num2_bin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homomorphic_crypto);
        num1 = (EditText) findViewById(R.id.editText_num1);
        num2 = (EditText) findViewById(R.id.editText_num2);
        Button calc = (Button) findViewById(R.id.btn_calc);
        btn_result  =  (TextView)findViewById(R.id.textView_result);

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                String result = getNumbers(convert_to_binary(Integer.parseInt(num1.getText().toString())),convert_to_binary(Integer.parseInt(num2.getText().toString())));//String.valueOf(spinner.getSelectedItem()));
                //Toast.makeText(getApplicationContext(),"Testing...",Toast.LENGTH_SHORT).show();
                //int result = getNumbers(Integer.parseInt(num1.getText().toString()),Integer.parseInt(num2.getText().toString()));
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                String num1Text = num1.getText().toString();
                String num2Text = num2.getText().toString();
                //int result = Integer.parseInt(num1Text) + Integer.parseInt(num2Text);
                String op = "Test";
                btn_result.setText(num1Text+ " " + op+ " " + num2Text);
            }
        });
        spinner = (Spinner)findViewById(R.id.spinner);
        //spinner.setOnItemSelectedListener(this);
        List<String> list = new ArrayList<String>();
        list.add("+");
        list.add("-");
        list.add("*");
        list.add("/");

        send = (Button)findViewById(R.id.send);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,android.R.id.text1,list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

        // Button click Listener
        addListenerOnButton();



    }

    /**
     *
     * For native C library Call
     */

       private native String getNumbers(int num1,int num2);

    /**
     *
     * Load the static library
     */

    static{
        System.loadLibrary("fullyhomo");
        //System.loadLibrary("libgmpso");
    }

    // convert to binary:
    public static int convert_to_binary(int dec_num){
        String converted_num = Integer.toBinaryString(dec_num);
        int result = Integer.parseInt(converted_num);
        return result;

    };
    // Add spinner data

    public void addListenerOnSpinnerItemSelection(){

        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    //get the selected dropdown list value

    public void addListenerOnButton() {

        spinner = (Spinner) findViewById(R.id.spinner);

        //btnSubmit = (Button) findViewById(R.id.btnSubmit);

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(HomomorphicCrypto.this,
                        "On Button Click : " +
                                "\n" + String.valueOf(spinner.getSelectedItem()),
                        Toast.LENGTH_LONG).show();
            }

        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_homomorphic_crypto, menu);
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
