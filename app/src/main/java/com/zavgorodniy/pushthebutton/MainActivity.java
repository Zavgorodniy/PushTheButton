package com.zavgorodniy.pushthebutton;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView ask;
    TextView countText;
    Button start;
    Button stop;
    int count = 0;
    int id = -1;
    final int DIFFICULT = 1000;
    final int ITERATIONS_NUM = 10;
    boolean stopGame;
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ask = (TextView) findViewById(R.id.tv_color);
        countText = (TextView) findViewById(R.id.tv_count);
        start = (Button) findViewById(R.id.bt_start);
        stop = (Button) findViewById(R.id.bt_stop);

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what != -1)
                    ask.setText(getResources().getStringArray(R.array.colors)[msg.what]);
                else
                    stopGame();
            }
        };

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGame();
            }
        });
    }

    public void startGame() {
        stopGame = false;
        count = 0;
        countText.setText(String.valueOf(count));
        Thread game = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < ITERATIONS_NUM; i++) {
                    if (stopGame)
                        break;
                    id = makeColourId();
                    h.sendEmptyMessage(id);
                    sleep(DIFFICULT);
                }
                h.sendEmptyMessage(-1);
            }
        });
        game.start();
        start.setEnabled(false);
    }

    public void stopGame() {
        stopGame = true;
        start.setEnabled(true);
        ask.setText("");
    }

    public void checkColor(int i) {
        if (i == id) {
            count++;
            countText.setText(String.valueOf(count));
        }
    }

    public void sleep(int difficult) {
        try {
            TimeUnit.MILLISECONDS.sleep(difficult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int makeColourId() {
        return (int) (Math.random() * 6);
    }

    public void onclick(View v) {
        if (!stopGame) {
            switch (v.getId()) {
                case R.id.bt_1:
                    checkColor(0);
                    break;
                case R.id.bt_2:
                    checkColor(1);
                    break;
                case R.id.bt_3:
                    checkColor(2);
                    break;
                case R.id.bt_4:
                    checkColor(3);
                    break;
                case R.id.bt_5:
                    checkColor(4);
                    break;
                case R.id.bt_6:
                    checkColor(5);
                    break;
            }
        }
    }
}
