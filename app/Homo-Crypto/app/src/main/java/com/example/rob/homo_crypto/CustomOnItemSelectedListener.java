package com.example.rob.homo_crypto;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


public class CustomOnItemSelectedListener implements OnItemSelectedListener {
    static String user_select;
    Connection con = new Connection();
    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {
             user_select=parent.getItemAtPosition(pos).toString();
       Toast.makeText(parent.getContext(),
                "On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public String getData(){
    return user_select;

    }

}