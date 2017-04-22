package app.ut.mafialottery;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LotteryActivity extends AppCompatActivity {

    Lottery lottery;
    TextView lotteryTv;
    private static final int WAIT_STATE = 0;
    private static final int SEEN_STATE = 1;
    private static final int END_STATE = 2;
    private int currentState = WAIT_STATE;
    RelativeLayout lotteryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
        Gson gson = new GsonBuilder().create();
        lottery = gson.fromJson(getIntent().getStringExtra("lottery"), Lottery.class);
        lotteryTv = (TextView)findViewById(R.id.lotteryTv);
        lotteryLayout = (RelativeLayout)findViewById(R.id.activity_lottery);
        lotteryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentState == WAIT_STATE) {
                    String role = lottery.getRandomRole();
                    lotteryTv.setText(role + "\n\n" + getString(R.string.lottery_seen_state_msg));
                    lotteryTv.setTextColor(Color.WHITE);
                    lotteryLayout.setBackgroundColor(Color.BLACK);
                    currentState = SEEN_STATE;
                }
                else if(currentState == SEEN_STATE) {
                    if(!lottery.roleLeft()) {
                        lotteryTv.setText(R.string.lottery_end_state_msg);
                        currentState = END_STATE;
                    } else {
                        lotteryTv.setText(R.string.lottery_wait_state_msg);
                        lotteryTv.setTextColor(Color.BLACK);
                        lotteryLayout.setBackgroundColor(Color.WHITE);
                        currentState = WAIT_STATE;
                    }
                }
                else if (currentState == END_STATE) {
                    Intent i = new Intent(LotteryActivity.this, SetupActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }


}
