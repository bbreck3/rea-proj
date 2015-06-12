package com.example.rob.homo_crypto;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    static EditText num1, num2;
    static TextView btn_result;
    static Spinner spinner;
    static Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        num1 = (EditText) findViewById(R.id.editText_num1);
        num2 = (EditText) findViewById(R.id.editText_num2);
        Button calc = (Button) findViewById(R.id.btn_calc);
        btn_result  =  (TextView)findViewById(R.id.textView_result);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   String num1Text = num1.getText().toString();
                   String num2Text = num2.getText().toString();
                    //int result = Integer.parseInt(num1Text) + Integer.parseInt(num2Text);
                String op = "Test";
                    btn_result.setText(num1Text+ " " + op+ " " + num2Text);
            }
        });
        spinner = (Spinner)findViewById(R.id.spinner);
        //spinner.setOnItemSelectedListener(this);
        List<String>  list = new ArrayList<String>();
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

    // Add spinner data

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

                Toast.makeText(MainActivity.this,
                        "On Button Click : " +
                                "\n" + String.valueOf(spinner.getSelectedItem()),
                        Toast.LENGTH_LONG).show();
            }

        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
