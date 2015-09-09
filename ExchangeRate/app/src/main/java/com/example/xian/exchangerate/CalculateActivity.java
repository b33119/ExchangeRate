package com.example.xian.exchangerate;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CalculateActivity extends Activity {

    public String country="", cashSell;
    public int tag;
    public RadioGroup rgroup;
    public RadioButton rdbtn1, rdbtn2;
    public TextView ch1, ch2;
    public EditText input1, input2;
    int Index_Value=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        //取得buundle的物件
        Bundle bundleMainActivity = this.getIntent().getExtras();
        country = bundleMainActivity.getString("country");
        cashSell = bundleMainActivity.getString("cashSell");
        //物件初始化
        ObjectsInitialization();
    }

    private void ObjectsInitialization() {
        //TextView初始化
        ch1 = (TextView) findViewById(R.id.ch1);
        ch1.setText("台幣 (NTD)");
        ch2 = (TextView) findViewById(R.id.ch2);
        ch2.setText(country);

        //RadioButton初始化
        rdbtn1 = (RadioButton)findViewById(R.id.rdbtn1);
        rdbtn1.setText(" 台幣 (NTD) 換" + country);
        rdbtn2 = (RadioButton)findViewById(R.id.rdbtn2);
        rdbtn2.setText(country+" 換 台幣 (NTD)");

        //EditText初始化
        input1 = (EditText)findViewById(R.id.input1);
        input2 = (EditText)findViewById(R.id.input2);
        input2.setText("0");
        input2.setEnabled(false);

        //RadioButton初始化
        rgroup = (RadioGroup)findViewById(R.id.rgroup);
        rgroup.setOnCheckedChangeListener(listener);

        //內建鍵盤, 執行Enter須做的事
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        input1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Calculate(input1.getText().toString());          //按下Enter呼叫Calculate function
                return false;
            }
        });
    }

    public RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            String tmp1=input1.getText().toString(), tmp2=input2.getText().toString();
            tag = group.indexOfChild((RadioButton) findViewById(checkedId));
            int count = group.getChildCount();
            switch(checkedId){
                case R.id.rdbtn1:
                    ch1.setText(" 台幣 (NTD)");
                    ch2.setText(country);
                    input1.setText("");
                    input2.setText("0");
                    input1.requestFocus();                  //固定游標
                    break;
                case R.id.rdbtn2:
                    ch1.setText(country);
                    ch2.setText(" 台幣 (NTD)");
                    input1.setText("");
                    input2.setText("0");
                    input1.requestFocus();                  //固定由標
                    break;
            }
        }
    };

    public void Calculate(String Cal){
        String inputValue;
        Double result;
        Double num1 = Double.valueOf(Cal).doubleValue();
        Double num2 = Double.valueOf(cashSell).doubleValue();
        if(tag==0) {
            result = num1/num2;
        }
        else {
            result = num1*num2;
        }
        NumberFormat numberFormat = new DecimalFormat("0.0000");        //設定Double變數的長度
        inputValue = numberFormat.format(result);
        input2.setText(inputValue);
    }
}
