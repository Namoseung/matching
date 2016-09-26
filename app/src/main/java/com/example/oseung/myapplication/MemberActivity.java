package com.example.oseung.myapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;





/**
 * Created by oseung on 2016-07-24.
 */
public class MemberActivity extends Activity {

        private EditText editTextId;
        private EditText editTextPwd;
        private EditText editTextNick;
        private EditText editTextEmail;
        private EditText editTextBirth;
        private TextView man,woman,sex1;
        private TextView et;
        private TextView et2,wndqhr,wndqhr2;
        private EditText editTextAdress;
        private EditText editTextPhone;
        private EditText editTextSns;
        ProgressDialog dialog = null;
        private RadioButton radiobutton,radiobutton1;
        Button overlab;
        Button overlab1;
        Button memberok;



        HttpPost httppost;
        StringBuffer buffer;
        HttpResponse response;
        HttpClient httpclient;
        List<NameValuePair> nameValuePairs;

        HttpPost httppost1;
        StringBuffer buffer1;
        HttpResponse response1;
        HttpClient httpclient1;
        List<NameValuePair> nameValuePairs1;
        HttpPost httppost2;
        StringBuffer buffer2;
        HttpResponse response2;
        HttpClient httpclient2;
        List<NameValuePair> nameValuePairs2;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.member_activity);
                et = (TextView)findViewById(R.id.tva);
                et2= (TextView)findViewById(R.id.tva2);
                editTextId = (EditText) findViewById(R.id.editText);
                editTextPwd = (EditText) findViewById(R.id.editText1);
                editTextNick = (EditText) findViewById(R.id.editText2);
                editTextEmail = (EditText) findViewById(R.id.editText3);
                editTextBirth = (EditText) findViewById(R.id.editText4);
                man = (TextView)findViewById(R.id.man);
                woman = (TextView)findViewById(R.id.woman);
                sex1 = (TextView)findViewById(R.id.sex);
                memberok =(Button)findViewById(R.id.memberok);
                wndqhr = (TextView)findViewById(R.id.wndqhr);
                wndqhr2 = (TextView)findViewById(R.id.wndqhr2);

                //sex1 = (EditText)findViewById(R.id.sex1);


                radiobutton = (RadioButton)findViewById(R.id.radioButton);
                radiobutton1 = (RadioButton)findViewById(R.id.radioButton1);

                editTextAdress = (EditText) findViewById(R.id.editText5);
                editTextPhone = (EditText) findViewById(R.id.editText6);
                editTextSns = (EditText) findViewById(R.id.editText7);


                getWindow().setWindowAnimations(0);
                overlab = (Button) findViewById(R.id.Overlab);
                overlab1  = (Button) findViewById(R.id.Overlab2);
                memberok = (Button) findViewById(R.id.memberok);

                memberok.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {

                                OverlabCheck();
                                OverlabCheck1();
                                String id = editTextId.getText().toString();
                                String pwd = editTextPwd.getText().toString();
                                String nick = editTextNick.getText().toString();
                                String email = editTextEmail.getText().toString();
                                String birth = editTextBirth.getText().toString();
                                if (radiobutton.isChecked() == true) {
                                        sex1.setText("남자");
                                } else if (radiobutton1.isChecked() == true){
                                        sex1.setText("여자");
                                }
                                String sex = sex1.getText().toString();
                                String address = editTextAdress.getText().toString();
                                String phone = editTextPhone.getText().toString();
                                String sns= editTextSns.getText().toString();

                                if(wndqhr.getText().toString()=="사용불가"){
                                        Toast.makeText(MemberActivity.this, "Id가 중복됩니다. 다시한번 확인하세요.", Toast.LENGTH_SHORT).show();
                                }else if(wndqhr2.getText().toString()=="사용불가"){
                                        Toast.makeText(MemberActivity.this,"NickName이 중복됩니다. 다시한번 확인하세요.",Toast.LENGTH_LONG).show();
                                }else if(wndqhr.getText().toString()==""||wndqhr2.getText().toString()==""){
                                        Toast.makeText(MemberActivity.this,"Id 및 NickName 중복체크 해주세요.",Toast.LENGTH_LONG).show();
                                }
                                else{
                                        insertToDatabase(id,pwd,nick,email,birth,sex,address,phone,sns);
                                        Intent intent = new Intent(MemberActivity.this,StartActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(MemberActivity.this,editTextId.getText().toString()+"님! 회원가입을 축하드립니다.",Toast.LENGTH_LONG).show();


                                }


                        }
                });

                overlab.setOnClickListener(new View.OnClickListener() {


                        @Override
                        public void onClick(View View) {
                                et.setText("0");
                                et2.setText("0");
                                dialog = ProgressDialog.show(MemberActivity.this, "",
                                        "Validating user...", true);
                                new Thread(new Runnable() {
                                        public void run() {
                                                OverlabCheck();
                                        }
                                }).start();
                        }
                });

                overlab1.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View View) {
                                et.setText("0");
                                et2.setText("0");
                                dialog = ProgressDialog.show(MemberActivity.this, "",
                                        "기다려주세요...", true);
                                new Thread(new Runnable() {
                                        public void run() {
                                                OverlabCheck1();
                                        }
                                }).start();
                        }
                });

        }

        void OverlabCheck(){

                try{
                        httpclient=new DefaultHttpClient();
                        httppost= new HttpPost("http://matching.dothome.co.kr/member/Overlab.php"); // make sure the url is correct.
                        //add your data
                        nameValuePairs = new ArrayList<NameValuePair>(1);
                        // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                        nameValuePairs.add(new BasicNameValuePair("ids",editTextId.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];

                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        //Execute HTTP Post Request
                        response=httpclient.execute(httppost);
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response = httpclient.execute(httppost, responseHandler);
                        System.out.println("Response : " + response);
                        runOnUiThread(new Runnable() {
                                public void run() {
                                        et.setText("Response from PHP : " + response);
                                        dialog.dismiss();
                                }
                        });

                        if(response.equalsIgnoreCase("Overlab")){
                                runOnUiThread(new Runnable() {
                                        public void run() {
                                                //  Toast.makeText(MemberActivity.this,"중복된 아이디가 있습니다\n 새로운 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                                wndqhr.setText("사용불가");
                                                showAlert1();
                                        }
                                });


                        }else{
                                wndqhr.setText("사용가능");
                                showAlert();

                        }


                }catch(Exception ex){}
        }
        public void showAlert() {
                MemberActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MemberActivity.this);

                                builder.setTitle("중복된아이디가 없습니다.");
                                builder.setMessage("사용가능한 아이디 입니다.")
                                        .setCancelable(false)
                                        .setPositiveButton("사용하기", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                        }
                });
        }
        public void showAlert1() {
                MemberActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MemberActivity.this);

                                builder1.setTitle("중복된 아이디가 있습니다");
                                builder1.setMessage("새로운 아이디를 입력해주세요.")
                                        .setCancelable(false)
                                        .setPositiveButton("다시입력", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                        });
                                AlertDialog alert = builder1.create();
                                alert.show();
                        }
                });
        }

        void OverlabCheck1(){

                try{
                        httpclient1=new DefaultHttpClient();
                        httppost1= new HttpPost("http://matching.dothome.co.kr/member/Overlab1.php"); // make sure the url is correct.
                        //add your data
                        nameValuePairs1 = new ArrayList<NameValuePair>(1);
                        // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                        nameValuePairs1.add(new BasicNameValuePair("nick",editTextNick.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];

                        httppost1.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
                        //Execute HTTP Post Request
                        response=httpclient1.execute(httppost1);
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response1 = httpclient1.execute(httppost1, responseHandler);
                        System.out.println("Response : " + response1);
                        runOnUiThread(new Runnable() {
                                public void run() {
                                        et2.setText("Response from PHP : " + response1);
                                        dialog.dismiss();
                                }
                        });

                        if(response1.equalsIgnoreCase("Overlab1")){
                                runOnUiThread(new Runnable() {
                                        public void run() {
                                                //  Toast.makeText(MemberActivity.this,"중복된 아이디가 있습니다\n 새로운 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                                wndqhr2.setText("사용불가");
                                                showAlert3();
                                        }
                                });


                        }else{
                                wndqhr2.setText("사용가능");
                                showAlert2();
                        }


                }catch(Exception ex){}
        }
        public void showAlert2() {
                MemberActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MemberActivity.this);
                                builder.setTitle("중복된 닉네임이 없습니다.");
                                builder.setMessage("사용가능한 닉네임이 입니다.")
                                        .setCancelable(false)
                                        .setPositiveButton("사용하기", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                        }
                });
        }
        public void showAlert3() {
                MemberActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MemberActivity.this);
                                builder.setTitle("중복된 넥네임이 있습니다");
                                builder.setMessage("새로운 닉네임이 입력해주세요.")
                                        .setCancelable(false)
                                        .setPositiveButton("다시입력", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                        }
                });
        }



        ///////////////////////////////////////////////////////////////////////////
        private void insertToDatabase(final String id, String pwd, String nick, String email,String birth,String sex, String address, String phone, String sns){

                class InsertData extends AsyncTask<String, Void, String>{
                        ProgressDialog loading;

                        @Override
                        protected void onPreExecute() {
                                super.onPreExecute();
                                loading = ProgressDialog.show(MemberActivity.this, "잠시만 기다려 주세요", null, true, true);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                        }

                        @Override
                        protected String doInBackground(String... params) {

                                try{

                                        String id = (String)params[0];
                                        String pwd = (String)params[1];
                                        String nick = (String)params[2];
                                        String email= (String)params[3];
                                        String birth= (String)params[4];
                                        String sex= (String)params[5];
                                        String address = (String)params[6];
                                        String phone = (String)params[7];
                                        String sns= (String)params[8];

                                        String link="http://matching.dothome.co.kr/member/insert.php";
                                        String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                                        data += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" + URLEncoder.encode(pwd, "UTF-8");
                                        data += "&" + URLEncoder.encode("nick", "UTF-8") + "=" + URLEncoder.encode(nick, "UTF-8");
                                        data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                                        data += "&" + URLEncoder.encode("birth", "UTF-8") + "=" + URLEncoder.encode(birth, "UTF-8");
                                        data += "&" + URLEncoder.encode("sex", "UTF-8") + "=" + URLEncoder.encode(sex, "UTF-8");
                                        data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");
                                        data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
                                        data += "&" + URLEncoder.encode("sns", "UTF-8") + "=" + URLEncoder.encode(sns, "UTF-8");


                                        URL url = new URL(link);
                                        URLConnection conn = url.openConnection();

                                        conn.setDoOutput(true);
                                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                                        wr.write( data );
                                        wr.flush();

                                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                                        StringBuilder sb = new StringBuilder();
                                        String line = null;

                                        // Read Server Response
                                        while((line = reader.readLine()) != null)
                                        {
                                                sb.append(line);
                                                break;
                                        }
                                        return sb.toString();
                                }
                                catch(Exception e){
                                        return new String("Exception: " + e.getMessage());
                                }

                        }
                }

                InsertData task = new InsertData();
                task.execute(id,pwd,nick,email,birth,sex,address,phone,sns);
        }

}