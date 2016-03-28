package com.weebly.helloworldclub.phoenixnow;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hunai on 3/4/2016.
 */
public class BackEnd {
    EditText email;
   EditText name;
    EditText password;
    static String emailstring;
    String message;
    int code;
    static String token;
    public BackEnd(EditText emailEditText, EditText passwordEditText, EditText nameEditText) {
        email=emailEditText;
        name=nameEditText;
        password=passwordEditText;
    }

    public void register() {
        RegisterThread thread = new RegisterThread();
        thread.start();
    }
    public void login(){
        LoginThread thread=new LoginThread();
        thread.start();
    }
    public class LoginThread extends Thread{
        public void run(){
            try {
                URL url = new URL("http://helloworldapi.nickendo.com/login");
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                JSONObject user=new JSONObject();
                user.put("email",email.getText().toString());
                user.put("password",password.getText().toString());
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                OutputStream os=urlConnection.getOutputStream();
                os.write(user.toString().getBytes());
                code=urlConnection.getResponseCode();
                message=urlConnection.getResponseMessage();
                token=urlConnection.getHeaderField("Authorization");
                emailstring=email.getText().toString();
            }catch(java.net.MalformedURLException e){
                e.printStackTrace();
            }catch(org.json.JSONException e){
                e.printStackTrace();
            }catch(java.net.ProtocolException e){
                e.printStackTrace();
            }catch(java.io.IOException e){
                e.printStackTrace();
            }
        }
    }

    public class RegisterThread extends Thread {
        public void run() {
            try {
                URL url = new URL("http://helloworldapi.nickendo.com/register");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                JSONObject user = new JSONObject();
                user.put("email", email.getText().toString());
                user.put("name", name.getText().toString());
                user.put("password", password.getText().toString());
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");
                OutputStream os = urlConnection.getOutputStream();
                os.write(user.toString().getBytes());
                os.close();
                code = urlConnection.getResponseCode();
                message = urlConnection.getResponseMessage();
                token=urlConnection.getHeaderField("Authorization");
                emailstring=email.getText().toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (org.json.JSONException e) {

            }
        }
    }
        public String getReturned() {
            return message;
        }

        public int getCode() {
            return code;
        }
    public String getToken(){
        return token;
    }
    public String getEmail(){return emailstring;}


}
