package com.example.rob.homo_crypto;

/**
 * Created by rob on 6/15/15.
 */


public class Polynomial {

    String term;
    int coef;
    int exp;

    public Polynomial(String term, int coef,int exp){
        this.term =term;
        this.coef = coef;
        this.exp =exp;
    }
    public void addTerm(){

        term+="+x";
    }
   
}


