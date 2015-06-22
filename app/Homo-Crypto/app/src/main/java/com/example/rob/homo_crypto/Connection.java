package com.example.rob.homo_crypto;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class Connection extends ActionBarActivity {


    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear,buttonwRecieve,buttonSend;
    static EditText num1, num2;
    static TextView btn_result, num1_bin, num2_bin, rand_poly, double_poly, num1_sum, num2_sum;
    static Spinner spinner;
    static Button send;
    static String op;
    String user_result;
    static int flag;
   private static Socket socket;
   private static String message;
    static BufferedReader in;
    static PrintStream out;
    static
    CustomOnItemSelectedListener l;
    static BigInteger n, mod,pk_mod,pk_n,c1,c2,pk_n_toServer,pk_mod_toServer;

    private static FileInputStream ct1f,ct2f;
    private static FileInputStream pkio;
    static PublicKey pk = new PublicKey();
    static  PrivateKey sk = new PrivateKey(1024);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //this is a custom on item listener for the spinner--> Andorids version of a drop down menu: it prints off the selected operation and position at each position of the selected option
        l = new CustomOnItemSelectedListener();
        /**
         *
         *      Connection to Server Code: Buttons for GUI
         *
         */
        setContentView(R.layout.activity_connection2);


        // GUI stuff
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);
        num1 = (EditText) findViewById(R.id.editText_num1);
        num2 = (EditText) findViewById(R.id.editText_num2);
        buttonConnect = (Button) findViewById(R.id.connect);
        buttonClear = (Button) findViewById(R.id.clear);
        buttonSend = (Button)findViewById(R.id.send);
        buttonwRecieve = (Button)findViewById(R.id.recieve);
        textResponse = (TextView) findViewById(R.id.response);
        num1_bin = (TextView) findViewById(R.id.textView_num1_bin);
        num2_bin = (TextView) findViewById(R.id.textView_num2_bin);
        rand_poly = (TextView) findViewById(R.id.textView_rand_poly);
        double_poly = (TextView) findViewById(R.id.textView_double);
        num1_sum = (TextView) findViewById(R.id.textView_num1_sum);
        num2_sum = (TextView) findViewById(R.id.textView_num2_sum);

        /**
         *
         *  As for tight now, everything happens on the connect button:
         *  1) server generates a piublci private key pair.
         *  2) The public key ONLY is sent (this is important) to the app where the N and Mod values are extracted for use with encryption
         *  3) once the Values of N are extracted: A public and private are generated(each device must generate their own set).
         *  Then values of N and Mod of the newly generated public key are manually overiden and with the values from the server.This is used to encrypt the data.
         *  4) the newly generated cipher texts are then sent back over along with the N and Mod values of the pk from the device.
         *  5) The server uses the values of the pk to add homorphically the cipher text and the decrypts to yield the correct result.
         */
        buttonConnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    //this is your IPAddress
                    String address = "10.107.237.17";//editTextAddress.getText().toString();
                    int port = Integer.parseInt(editTextPort.getText().toString());
                    socket = new Socket(address, port);
                    //open up input and output stream on socket
                    in = new BufferedReader(new InputStreamReader((socket.getInputStream())));
                    out = new PrintStream(socket.getOutputStream());

                    //Grab tge N vaues of the PK from the server
                     n= new BigInteger(in.readLine());
                    //For debugging purposes...
                   // Toast.makeText(getApplicationContext(),"N from server:..." + n.toString(), Toast.LENGTH_SHORT).show();
                    //Grab the Mod value from the server
                    mod= new BigInteger(in.readLine());
                    //For debugging purposes
                    //Toast.makeText(getApplicationContext(),"mod from server:..." + n.toString(), Toast.LENGTH_SHORT).show();
                    //key gen
                    //generate the pk --> as two overide the value values of the pk with the values from the server
                    keyGen(sk, pk);
                    //construct two values to encrpyt
                    BigInteger p1= new BigInteger(num1.getText().toString());//"1234");//num1.getText().toString());
                    BigInteger p2= new BigInteger(num2.getText().toString());
                    //encrpyt them
                    c1=encrypt(p1,pk);
                    c2=encrypt(p2,pk);
                    //send the cipher text to the server
                    out.println(c1.toString());
                    out.println(c2.toString());
                    //send the N and Mod values to the server inorder to add homomorphically on the server
                   out.println(pk_n_toServer.toString());
                   out.println(pk_mod_toServer.toString());
                    //returns the encrpyted homomorphic ciphertext
                    BigInteger homo_cipher = new BigInteger(in.readLine());
                    //this is the result that is decrypted this should yield the result of the homomorphic operation
                    BigInteger result = decrypt(homo_cipher,sk);
                    Toast.makeText(getApplicationContext(),"Result:..." + result,Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                }

        }});


        buttonClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                textResponse.setText("");

            }
        });

        /**
         *
         *      User input and calculation code
         *
         */


        Button calc = (Button) findViewById(R.id.btn_calc);
        btn_result = (TextView) findViewById(R.id.textView_result);
        send = (Button) findViewById(R.id.send);

        /**   This allows for a socket to be opened on the main thread without having to create
         *     seperate thread which requires AyncTask                                           */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //sets up a string of the user input and the selected operation from the user
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num1Text = num1.getText().toString();
                String num2Text = num2.getText().toString();
                int result = Integer.parseInt(num1Text) + Integer.parseInt(num2Text);
                //btn_result.setText(Integer.toString(result));
                String op = getOP();
                //THis will be used to send to user input to server....
                user_result = num1Text + " " + op + " " + num2Text;
                btn_result.setText(user_result);

                /*convert the user input to binary string --> Skipped-Created a mapping from the number to the actual polynomial
                String num_bin1 = encodePoly(num1Text);
                String num_bin2 = encodePoly(num2Text);

                num1_bin.setText(num_bin1);
                num2_bin.setText(num_bin2);

                rand_poly.setText(genRandomPoly());
                double_poly.setText(doublePoly(rand_poly.getText().toString()));
                //num1 sum
                num1_sum.setText(sum_poly(double_poly.getText().toString(), num1_bin.getText().toString()));*/

            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);
        //spinner.setOnItemSelectedListener(this);
        List<String> list = new ArrayList<String>();
        list.add("+");
        list.add("-");
        list.add("*");
        list.add("/");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

        // Button click Listener
        addListenerOnButton();

    }


     private static void keyGen(PrivateKey sk, PublicKey pk) {
         int k2 = 40;
        try{

           //pk_n_toServer,pk_mod_toServer;

            // bit_length is set as half of k1 so that when pq is computed, the
            // result has k1 bits
            int bit_length = sk.k1 / 2;

            // Chooses a random prime of length k2. The probability that p is not
            // prime is at most 2^(-k2)
            BigInteger p = new BigInteger(bit_length, k2, rnd);
            BigInteger q = new BigInteger(bit_length, k2, rnd);

            pk.k1 = sk.k1;
            pk.n = p.multiply(q); // n = pq
            pk_n_toServer = pk.n;
            //pk_n=pk.n;
           // pk.n=n;
            //Log.e("",pk.n.toString());
            //int pk_n_len = String.valueOf(pk.n).length();


            pk.modulous = pk.n.multiply(pk.n); // modulous = n^2
            pk_mod_toServer = pk.modulous;
            //pk_mod=pk.modulous;
            //Log.e("",pk.modulous.toString());
            //int pk_mod_len = String.valueOf(pk.modulous).length();
            sk.lambda = p.subtract(BigInteger.ONE).multiply(
                    q.subtract(BigInteger.ONE));
            sk.mu = sk.lambda.modInverse(pk.n);
            sk.n = pk.n;
            sk.modulous = pk.modulous;
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    private static Random rnd = new Random();
    // Compute ciphertext = (mn+1)r^n (mod n^2) in two stages: (mn+1) and (r^n).
    public static BigInteger encrypt(BigInteger plaintext, PublicKey pk) {

        pk.n=n;
        pk.modulous=mod;
        BigInteger randomness = new BigInteger(pk.k1, rnd); // creates
        // randomness of
        // length k1
        BigInteger tmp1 = plaintext.multiply(pk.n).add(BigInteger.ONE)
                .mod(pk.modulous);
        BigInteger tmp2 = randomness.modPow(pk.n, pk.modulous);
        BigInteger ciphertext =tmp1.multiply(tmp2).mod(pk.modulous);

        return ciphertext;
    }

    // Compute plaintext = L(c^(lambda) mod n^2) * mu mod n
    public static BigInteger decrypt(BigInteger ciphertext, PrivateKey sk) {

        BigInteger plaintext = L(ciphertext.modPow(sk.lambda, sk.modulous),
                sk.n).multiply(sk.mu).mod(sk.n);

        return plaintext;
    }

    // On input two encrypted values, returns an encryption of the sum of the
    // values
    public static BigInteger add(BigInteger ciphertext1,
                                 BigInteger ciphertext2, PublicKey pk) {
        pk.n=n;
        pk.modulous = mod;
        BigInteger ciphertext =ciphertext1.multiply(ciphertext2).mod(
                pk.modulous);
        return ciphertext;
    }

    // On input an encrypted value x and a scalar c, returns an encryption of
    // cx.
    public static BigInteger multiply(BigInteger ciphertext1,
                                      BigInteger scalar, PublicKey pk) {
        BigInteger ciphertext = ciphertext1.modPow(scalar, pk.modulous);
        return ciphertext;
    }

    public static BigInteger multiply(BigInteger ciphertext1, int scalar,
                                      PublicKey pk) {
        return multiply(ciphertext1, BigInteger.valueOf(scalar), pk);
    }

    public static void display(BigInteger c, PrivateKey sk) {
        BigInteger tmp = decrypt(c, sk);
        byte[] content = tmp.toByteArray();
        System.out.print("Ciphertext contains " + content.length
                + " bytes of the following plaintext:");
        for (int i = 0; i < content.length; i++)
            System.out.print((0xFF & content[i]) + " ");
        System.out.println();
    }

    // L(u)=(u-1)/n
    private static BigInteger L(BigInteger u, BigInteger n) {
        return u.subtract(BigInteger.ONE).divide(n);
    }





    // Add spinner dataOn

    public void addListenerOnSpinnerItemSelection() {

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
    //ingnore this--> can be use get data from this activity woithout a bundle if neccessary
    public String getOP() {
        return l.getData();

    }



    /**
     * Network Methodds
     */


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


    //ingnore this--> this was something that I was working on the could still be usefull
    public static String getMessage(){

        return message;
    }





}

/**
 *
 *      Ignore all of the below that was something I was working on to try and convert the C code that was implemented in anohter cryptosystem
 *
 *      I will still go back and use that cryptosystem but will try something else. For you, ignore this...
 *
 */



/**
 //method that take the user input and maps it to a equevalent polynomial
 //only works for 1 -9 for now
 //
 /*
 Mapping as follows: Format:: number-->binary representation --> poly representation
 1--> 0001 --> 1
 2--> 0010 --> x
 3--> 0011 --> x + 1
 4--> 0100 --> x^2
 5--> 0101 --> x^2 +1
 6--> 0110 --> x^2 +x
 7--> 0111 --> x^2 + x + 1
 8--> 1000 --> x^3
 9--> 1001 -- > x^3 +1




public String encodePoly(String str) {
    String result = "";

    switch (str) {
        case "1":
            result += "1";
            flag = 1;
            break;

        case "2":
            result += "x";
            flag = 2;
            break;
        case "3":
            result += "x+1";
            flag = 3;
            break;
        case "4":
            result += "x^2";
            flag = 4;
            break;

        case "5":
            result += "x^2+1";
            flag = 5;
            break;

        case "6":
            result += "x^2 + x";
            flag = 6;
            break;

        case "7":
            result += "x^2 + x + 1";
            flag = 7;
            break;

        case "8":
            result += "x^3";
            flag = 8;
            break;

        case "9":
            result += "x^3 +1";
            flag = 9;
            break;

    } //end switch
    return result;
}// end encodePoly

    public String genRandomPoly() {
        Random rand = new Random();
        String str = Integer.toString(rand.nextInt(9) + 1);
        String result = "";
        switch (str) {
            case "1":
                result += "1";
                break;

            case "2":
                result += "x";
                break;
            case "3":
                result += "x+1";
                break;
            case "4":
                result += "x^2";
                break;

            case "5":
                result += "x^2+1";
                break;

            case "6":
                result += "x^2+x";
                break;

            case "7":
                result += "x^2+x+1";
                break;

            case "8":
                result += "x^3";
                break;

            case "9":
                result += "x^3+1";
                break;

        } //end switch
        return result;//str +"  : " + result;
    }

    public String flaggedPoly(int num) {
        String result = "";
        switch (num) {
            case 1:
                result += "1";
                break;

            case 2:
                result += "x";
                break;
            case 3:
                result += "x+1";
                break;
            case 4:
                result += "x^2";
                break;

            case 5:
                result += "x^2+1";
                break;

            case 6:
                result += "x^2+x";
                break;

            case 7:
                result += "x^2+x+1";
                break;

            case 8:
                result += "x^3";
                break;

            case 9:
                result += "x^3+1";
                break;

        }
        return result;
    }

    public int getFlagedPoly() {
        return flag;
    }

    public String doublePoly(String str) {
        String result = "";
        int len = str.length();
        switch (len) {
            case 1:
                if (str.equals("1")) {
                    result = "1";
                } else result = "x";
                break;

            case 2:
                if (str.charAt(1) == '1') {
                    result = "1";
                } else result = "2x";
                break;
            case 3:
                if (str.charAt(2) == '1') {
                    result = "2x +2";
                } else if (str.charAt(2) == '2') {
                    result = "2x^2";
                } else result = "2x^3";
                break;
            case 5:
                if (str.charAt(4) == '1' && str.charAt(2) == '2') {
                    result = "2x^2+2";

                } else if (str.charAt(4) == 'x') {
                    result = "2x^2+2x";
                } else result = "2x^3+2";
                break;
            case 7:
                result = "2x^2+2x+2";
                break;
            default:
                double_poly.setText("No Case for this one");


        }
        return result;
    }

    public String sum_poly(String str1, String str2) {
        //  num1_sum.setText(sum_poly(double_poly.getText().toString(), num1_bin.getText().toString()));
        String result = "";

        switch (str2) {
            //case 1
            case "1":
                if (str1.charAt(str1.length() - 1) == '1') {
                    String temp = str1.substring(0, str1.length() - 1);
                    int num = Integer.parseInt(str1.substring(str1.length() - 1));
                    int add_num = num + 1;
                    String concat = temp.concat(Integer.toString(add_num));
                    result = concat;
                    //case : x^2
                } else if (str1.charAt(str1.length() - 1) == '2') {
                    if (str1.charAt(str1.length() - 2) == '+') {
                        String temp = str1.substring(0, str1.length() - 1);
                        int num = Integer.parseInt(Character.toString(str1.charAt(str1.length() - 1)));
                        num++;
                        String concat = temp.concat(Integer.toString(num));
                        result = concat;

                    } else {
                        String temp = str1.substring(0, str1.length());

                        String concat = temp.concat("+1");
                        result = concat;
                    }
                    //case x^3
                } else if (str1.charAt(str1.length() - 1) == '3') {
                    if (str1.charAt(str1.length() - 2) == '+') {
                        String temp = str1.substring(0, str1.length() - 1);
                        int num = Integer.parseInt(Character.toString(str1.charAt(str1.length() - 1)));
                        num++;
                        String concat = temp.concat(Integer.toString(num));
                        result = concat;

                    } else {

                        String temp = str1.substring(0, str1.length());
                        String concat = temp.concat("+1");
                        result = concat;
                    }
                } else if (str1.charAt(str1.length() - 1) == 'x') {
                    Toast.makeText(getApplicationContext(), "Debug...", Toast.LENGTH_SHORT).show();
                    String temp = str1.concat("+1");
                    result = temp;

                }
                break;
            //case 3
            case "x+1":
                if (getFlagedPoly() == 1) {
                    result = "x+2";
                } else if (getFlagedPoly() == 2) {
                    result = "2x+2";
                } else if (getFlagedPoly() == 3) {
                    result = "2x+2";
                } else if (getFlagedPoly() == 4) {
                    result = "x^2+x+1";
                } else if (getFlagedPoly() == 5) {
                    result = "x^2+x+2";
                } else if (getFlagedPoly() == 6) {
                    result = "x^2+2x+1";
                } else if (getFlagedPoly() == 7) {
                    result = "x^2+2x+2";
                } else if (getFlagedPoly() == 8) {
                    result = "x^3+x+1";
                } else if (getFlagedPoly() == 1) {
                    result = "x^3+x+2";
                }

                break;
            //case 2
            case "x":
                if (getFlagedPoly() == 1) {
                    result = "x+1";
                } else if (getFlagedPoly() == 2) {
                    result = "2x";
                } else if (getFlagedPoly() == 3) {
                    result = "2x+1";
                } else if (getFlagedPoly() == 4) {
                    result = "x^2+x";
                } else if (getFlagedPoly() == 5) {
                    result = "x^2+x+1";
                } else if (getFlagedPoly() == 6) {
                    result = "x^2+2x +1";
                } else if (getFlagedPoly() == 7) {
                    result = "x^2+2x+1";
                } else if (getFlagedPoly() == 8) {
                    result = "x^3+x";
                } else if (getFlagedPoly() == 1) {
                    result = "x^3+2x+1";
                }

                break;
            //case 4
            case "x^2":
                if (getFlagedPoly() == 1) {
                    result = "x^2+1";
                } else if (getFlagedPoly() == 2) {
                    result = "x^2+x";
                } else if (getFlagedPoly() == 3) {
                    result = "x^2+x+1";
                } else if (getFlagedPoly() == 4) {
                    result = "2x^2";
                } else if (getFlagedPoly() == 5) {
                    result = "2x^2+1";
                } else if (getFlagedPoly() == 6) {
                    result = "x^2+x";
                } else if (getFlagedPoly() == 7) {
                    result = "2x^2+x+1";
                } else if (getFlagedPoly() == 8) {
                    result = "x^3=x^2";
                } else if (getFlagedPoly() == 9) {
                    result = "x^3+x^2+1";
                }

                break;
            //case 5
            case "x^2+1":
                if (getFlagedPoly() == 1) {
                    result = "x^2+2";
                } else if (getFlagedPoly() == 2) {
                    result = "x^2+x";
                } else if (getFlagedPoly() == 3) {
                    result = "x^2+x+1";
                } else if (getFlagedPoly() == 4) {
                    result = "2x^2+1";
                } else if (getFlagedPoly() == 5) {
                    result = "2x^2+2";
                } else if (getFlagedPoly() == 6) {
                    result = "2x^2+x+1";
                } else if (getFlagedPoly() == 7) {
                    result = "2x^2+x+2";
                } else if (getFlagedPoly() == 8) {
                    result = "x^3+x^2+1";
                } else if (getFlagedPoly() == 9) {
                    result = "x^3+x^2+2";
                }

                break;
            //case 6
            case "x^2+x":
                if (getFlagedPoly() == 1) {
                    result = "xx^2+x+1";
                } else if (getFlagedPoly() == 2) {
                    result = "x^2+2x";
                } else if (getFlagedPoly() == 3) {
                    result = "x^2+2x+1";
                } else if (getFlagedPoly() == 4) {
                    result = "2x^2+x";
                } else if (getFlagedPoly() == 5) {
                    result = "2x^2+x+1";
                } else if (getFlagedPoly() == 6) {
                    result = "2x^2+2x";
                } else if (getFlagedPoly() == 7) {
                    result = "2x^=2x+1";
                } else if (getFlagedPoly() == 8) {
                    result = "x^3+x^2+x";
                } else if (getFlagedPoly() == 9) {
                    result = "x^3+x^2+x+1";
                }

                break;
            //case 7
            case "x^2+x+1":
                if (getFlagedPoly() == 1) {
                    result = "x^2+x+1";
                } else if (getFlagedPoly() == 2) {
                    result = "x^2+2x+1";
                } else if (getFlagedPoly() == 3) {
                    result = "x^2+2x+2";
                } else if (getFlagedPoly() == 4) {
                    result = "2x^2+x+1";
                } else if (getFlagedPoly() == 5) {
                    result = "2x^2+x+2";
                } else if (getFlagedPoly() == 6) {
                    result = "2x^2+2x+1";
                } else if (getFlagedPoly() == 7) {
                    result = "2x^2+2x+2";
                } else if (getFlagedPoly() == 8) {
                    result = "x^3+2x^2+2x+1";
                } else if (getFlagedPoly() == 9) {
                    result = "x^3+x^2+x+2";
                }

                break;
            //case 8
            case "x^3":
                if (getFlagedPoly() == 1) {
                    result = "X^3+1";
                } else if (getFlagedPoly() == 2) {
                    result = "x^3+x";
                } else if (getFlagedPoly() == 3) {
                    result = "x^3+x+1";
                } else if (getFlagedPoly() == 4) {
                    result = "x^3+x^2";
                } else if (getFlagedPoly() == 5) {
                    result = "x^3+x^2+1";
                } else if (getFlagedPoly() == 6) {
                    result = "x^3+x^2+x";
                } else if (getFlagedPoly() == 7) {
                    result = "x^3+x^2+x+1";
                } else if (getFlagedPoly() == 8) {
                    result = "2x^3";
                } else if (getFlagedPoly() == 9) {
                    result = "2x^3+1";
                }

                break;
            //case 9
            case "x^3+1":
                if (getFlagedPoly() == 1) {
                    result = "x^3+2";
                } else if (getFlagedPoly() == 2) {
                    result = "x^3+x";
                } else if (getFlagedPoly() == 3) {
                    result = "x^3 +x+2";
                } else if (getFlagedPoly() == 4) {
                    result = "x^3+x^2+1";
                } else if (getFlagedPoly() == 5) {
                    result = "x^3+x^2+2";
                } else if (getFlagedPoly() == 6) {
                    result = "x^3+x^2+x+1";
                } else if (getFlagedPoly() == 7) {
                    result = "x^3+x^2+x+2";
                } else if (getFlagedPoly() == 8) {
                    result = "2x^3+1";
                } else if (getFlagedPoly() == 9) {
                    result = "2x^3+2";
                }

                break;


            default:
                result = "No Case";
        }
        return result;

    }



    /*public String solvePoly(String str){


    }*/













