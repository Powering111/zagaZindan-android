package com.payrespect.jagazindan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private String name,pwd,directLink,schoolName,schoolNumber,kName,kBirth;
    private CheckBox directCheck,schoolCheck;
    private EditText nameEdit,pwdEdit,directEdit,schoolNameEdit,schoolNumberEdit,kNameEdit,kBirthEdit;
    private SharedPreferences pref;
    private threading th;
    private TextView asdfTxt;
    private boolean isDirect,isSchool,executing;
    public enum mode{
        normal,direct,school
    };
    private mode MODE;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        executing=false;
        final Button exebtn = findViewById(R.id.exebtn);
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        webView.loadUrl(directLink);
                        break;
                    case 2:
                        webView.loadUrl("https://eduro.gne.go.kr/stv_cvd_co00_010.do");
                        break;
                    case 3:
                        webView.evaluateJavascript("var macro = function () {var btn = document.getElementById(\"btnConfirm\");var nName = document.getElementById(\"pName\");nName.value=\"" + name + "\";var nCrt = document.getElementById(\"qstnCrtfcNo\");nCrt.value=\"" + pwd + "\";btn.click(); };", null);
                        webView.evaluateJavascript("macro();", null);
                        break;
                    case 4:
                        webView.evaluateJavascript("var hi = function(){var rad1 = document.getElementById(\"rspns011\");\n" +
                                "rad1.checked=true;\n" +
                                "var rad2 = document.getElementById(\"rspns02\");\n" +
                                "rad2.checked=true;\n" +
                                "var rad3 = document.getElementById(\"rspns070\");\n" +
                                "rad3.checked=true;\n" +
                                "var rad4 = document.getElementById(\"rspns080\");\n" +
                                "rad4.checked=true;\n" +
                                "var rad5 = document.getElementById(\"rspns090\");\n" +
                                "rad5.checked=true;\n" +
                                "var conf = document.getElementById(\"btnConfirm\");\n" +
                                "conf.click();};", null);
                        webView.evaluateJavascript("hi();", null);
                        break;
                    case 5:
                        Toast.makeText(getApplicationContext(), "자가진단 완료", Toast.LENGTH_SHORT).show();
                        executing=false;
                        exebtn.setEnabled(true);
                        break;
                    case 6:
                        Toast.makeText(getApplicationContext(), "에러 발생", Toast.LENGTH_SHORT).show();
                        executing=false;
                        exebtn.setEnabled(true);
                        break;
                    case 7:
                        webView.evaluateJavascript("document.getElementById(\"btnSrchSchul\").click();",null);
                        break;
                    case 8:
                        webView.loadUrl("https://eduro.gne.go.kr/stv_cvd_co00_002.do");
                        break;
                    case 9:
                        webView.evaluateJavascript("document.getElementById(\"schulNm\").value=\""+schoolName+"\";\n" +
                                "document.getElementsByClassName(\"btn_sm\")["+schoolNumber+"].click();",null);
                        break;
                    case 10:
                        webView.evaluateJavascript("document.querySelectorAll(\"a\")[0].click();\n" +
                                "document.getElementById(\"pName\").value=\""+kName+"\";\n" +
                                "document.getElementById(\"frnoRidno\").value=\""+kBirth+"\";\n" +
                                "document.getElementById(\"btnConfirm\").click();",null);
                        break;
                }
                return true;
            }
        });
        th=new threading(handler);
        pref=getSharedPreferences("sFile",MODE_PRIVATE);
        directCheck=findViewById(R.id.direct);
        schoolCheck=findViewById(R.id.isSchool);
        schoolNameEdit=findViewById(R.id.pKey);
        schoolNumberEdit=findViewById(R.id.pN);
        kNameEdit=findViewById(R.id.pName);
        kBirthEdit=findViewById(R.id.pBirth);
        asdfTxt=findViewById(R.id.pasdf);

        directEdit=findViewById(R.id.directLink);
        nameEdit=findViewById(R.id.name);
        pwdEdit=findViewById(R.id.pwd);
        nameEdit.setText(pref.getString("name",""));
        pwdEdit.setText(pref.getString("pwd",""));
        directEdit.setText(pref.getString("directLink",""));
        schoolNameEdit.setText(pref.getString("schoolname",""));
        schoolNumberEdit.setText(pref.getString("schoolnum","1"));
        kNameEdit.setText(pref.getString("kname",""));
        kBirthEdit.setText(pref.getString("kbirth",""));

        schoolCheck.setChecked(pref.getBoolean("school",false));
        directCheck.setChecked(pref.getBoolean("direct",false));
        Change_UI();
        directCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    schoolCheck.setChecked(false);
                }
                Change_UI();
            }
        });
        schoolCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    directCheck.setChecked(false);
                }
                Change_UI();
            }
        });
        webView = findViewById(R.id.web);
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view,String url){
                th.setLoaded(true);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){

        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);

        exebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!executing) {
                    executing = true;

                save();
                exebtn.setEnabled(false);
                th.setMode(MODE);
                th.start();
                }
            }

        });
    }
    @Override
    protected void onStop(){
        super.onStop();
        save();
    }
    private void changeVars(){

        name=nameEdit.getText().toString();
        pwd=pwdEdit.getText().toString();
        directLink=directEdit.getText().toString();
        kName=kNameEdit.getText().toString();
        kBirth=kBirthEdit.getText().toString();
        schoolName=schoolNameEdit.getText().toString();
        schoolNumber=schoolNumberEdit.getText().toString();
        isDirect=directCheck.isChecked();
        isSchool=schoolCheck.isChecked();
        if(isSchool){
            MODE=mode.school;
        }else if(isDirect){
            MODE=mode.direct;
        }else{
            MODE=mode.normal;
        }
    }
    private void save(){
        changeVars();
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("direct",isDirect);
        editor.putBoolean("school",isSchool);
        editor.putString("directLink",directLink);
        editor.putString("name",name);
        editor.putString("pwd",pwd);
        editor.putString("kname",kName);
        editor.putString("kbirth",kBirth);
        editor.putString("schoolname",schoolName);
        editor.putString("schoolnum",schoolNumber);
        editor.apply();
    }
    private void Change_UI(){
        changeVars();

        switch(MODE){
            case normal:
                directEdit.setVisibility(View.GONE);
                nameEdit.setVisibility(View.VISIBLE);
                pwdEdit.setVisibility(View.VISIBLE);
                schoolNameEdit.setVisibility(View.GONE);
                schoolNumberEdit.setVisibility(View.GONE);
                asdfTxt.setVisibility(View.GONE);
                kBirthEdit.setVisibility(View.GONE);
                kNameEdit.setVisibility(View.GONE);

                break;
            case direct:
                directEdit.setVisibility(View.VISIBLE);
                nameEdit.setVisibility(View.GONE);
                pwdEdit.setVisibility(View.GONE);
                schoolNameEdit.setVisibility(View.GONE);
                schoolNumberEdit.setVisibility(View.GONE);
                asdfTxt.setVisibility(View.GONE);
                kBirthEdit.setVisibility(View.GONE);
                kNameEdit.setVisibility(View.GONE);

                break;
            case school:
                directEdit.setVisibility(View.GONE);
                nameEdit.setVisibility(View.GONE);
                pwdEdit.setVisibility(View.GONE);
                schoolNameEdit.setVisibility(View.VISIBLE);
                schoolNumberEdit.setVisibility(View.VISIBLE);
                asdfTxt.setVisibility(View.VISIBLE);
                kBirthEdit.setVisibility(View.VISIBLE);
                kNameEdit.setVisibility(View.VISIBLE);

                break;
        }
    }
}
