package com.kumar.braintrainerapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    CardView startbutton,result;
    TextView score,time,question,textOptoin1,textOptoin2,textOptoin3,textOptoin4,resulttextview,highestscore;
    RelativeLayout relativelayout_score_time;
    ScrollView gridviewtile;
    int questioncount=0,presentscore=0,a=0,b=0;
    Random randomnumber;
    int answerplace=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=this.getPreferences(0);
        startbutton=findViewById(R.id.start);
        relativelayout_score_time=findViewById(R.id.relativelayout_score_time);
        score=findViewById(R.id.score);
        time=findViewById(R.id.timeinseconds);
        result=findViewById(R.id.results);
        gridviewtile=findViewById(R.id.scrollview);
        question=findViewById(R.id.question);
        resulttextview=findViewById(R.id.resulttextview);
        textOptoin1=findViewById(R.id.text_option1);
        textOptoin2=findViewById(R.id.text_option2);
        textOptoin3=findViewById(R.id.text_option3);
        textOptoin4=findViewById(R.id.text_option4);
        highestscore=findViewById(R.id.highestscore);
        int lastscore=sharedPreferences.getInt("highest",0);
        highestscore.setText("Highest Score : "+lastscore);
        showQuestion();
    }

    private void showQuestion() {
        randomnumber=new Random();
        a=randomnumber.nextInt(20);
        b=randomnumber.nextInt(20);
        question.setText(String.valueOf(a)+" + "+String.valueOf(b)+" ?");


        answerplace=randomnumber.nextInt(4);
        ArrayList<Integer> options=new ArrayList<>();
        for (int i = 0; i <4 ; i++) {

            int num=randomnumber.nextInt(40);
            if(answerplace==i)
            {
                while(num!=(a+b))
                {
                    num=randomnumber.nextInt(40);
                }
                options.add(num);
            }
            else
            {
                if((a+b)==num)
                    i--;
                options.add(num);
            }

        }
        textOptoin1.setText(String.valueOf(options.get(0)));
        textOptoin2.setText(String.valueOf(options.get(1)));
        textOptoin3.setText(String.valueOf(options.get(2)));
        textOptoin4.setText(String.valueOf(options.get(3)));

    }


    public void checkAnswer(View view) {
        if (String.valueOf(answerplace).equals(view.getTag())) {
            resulttextview.setText("Correct");
            result.setCardBackgroundColor(0xff2ecc71);
            score.setText(String.valueOf(++presentscore)+"/"+(++questioncount));
        } else {
            resulttextview.setText("Wrong");
            result.setCardBackgroundColor(0xffff6347);
            score.setText(String.valueOf(presentscore)+"/"+(++questioncount));
        }
        showQuestion();
    }


    public void startBrainTrain(View view)
    {
        startbutton.setVisibility(View.GONE);
        highestscore.setVisibility(View.GONE);
        relativelayout_score_time.setVisibility(View.VISIBLE);
        gridviewtile.setVisibility(View.VISIBLE);
        result.setVisibility(View.VISIBLE);
        score.setText("0/0");
        startTimer();
    }

    private void startTimer() {

       new CountDownTimer(30000,1000){

           @Override
           public void onTick(long millisUntilFinished) {
               String time1=String.valueOf(millisUntilFinished/1000);
               time.setText(time1+"s");
           }

           @Override
           public void onFinish() {
               showResults();
           }
       }.start();
    }

    private void showResults() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" Your score is "+presentscore+" of "+questioncount+" Do you want to continue?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        score.setText("0/0");
                        showQuestion();
                        startTimer();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        int lastscore=sharedPreferences.getInt("highest",0);
                        if(lastscore>presentscore) {
                            highestscore.setText("Highest Score : " + lastscore);
                        }
                        else {
                            highestscore.setText("Highest Score : " + presentscore);
                            sharedPreferences.edit().putInt("highest", presentscore).apply();
                        }
                        presentscore=0;
                        questioncount=0;
                        startbutton.setVisibility(View.VISIBLE);
                        highestscore.setVisibility(View.VISIBLE);
                        relativelayout_score_time.setVisibility(View.GONE);
                        gridviewtile.setVisibility(View.GONE);
                        result.setVisibility(View.GONE);

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Oops! Time UP");
        if(!((Activity) this).isFinishing())
        {
        alert.show();
        }
    }

}
;