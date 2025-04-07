package com.example.Demo_Product.controller;

public class ReverseString {

    public static void main(String [] args){

        String str ="My name is sumit Raj";
        StringBuilder s = new StringBuilder(" ");
        String [] words=str.split(" ");

        for(String c:words){
            StringBuilder sb= new StringBuilder(c);
            s.append(sb.reverse()+" ");

        }
        System.out.println(s);


    }

}
