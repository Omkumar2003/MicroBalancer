package com.github.omkumargithub.helper;


interface rp {
    int roundTripper = 0;
    int flushinterval = 0;
    int errorlog = 0;
    int bufferPool = 0;
 
    void rewrite();
 
    void director();
 
    void modifyResponse();
 
    void ErrorHandler();
 
    void serveHttp();
 
 }
 
 public class ReverseProxy implements rp {
 
    public void rewrite() {
    }
 
    public void director() {
    }
 
    public void modifyResponse() {
    }
 
    public void ErrorHandler() {
    }
    public    void serveHttp(){}
 
 }
