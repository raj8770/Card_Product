package com.example.Demo_Product.controller;

public class MyRunnable implements Runnable{

    public void run() {

        System.out.println("Thread is starting");
    }
}

class Main{
    public static void main(String [] args){

        Thread t1 = new Thread(new MyRunnable());
        t1.start();
    }
}

