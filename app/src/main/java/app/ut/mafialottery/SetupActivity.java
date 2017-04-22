package app.ut.mafialottery;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener, NewRoleDialog.DialogListener{

    private NumberPicker playerCountNp;
    Button startBtn, newRoleBtn;
    EditText mafiaCountEt, citizenCountEt;
    private LinearLayout newRolesContainer;
    private Lottery lottery;
    Gson lotteryGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        initializeViews();
        lotteryGson = new Gson();
        lottery = new Lottery();
    }

    @Override
    public void onPosBtnClick(DialogFragment dialog, View view) {
        if(view != null) {
            EditText roleNameEt = (EditText) view.findViewById(R.id.newRoleNameEditText);
            EditText roleCountEt = (EditText) view.findViewById(R.id.newRoleCountEditText);
            String name = roleNameEt.getText().toString().trim();
            String count = roleCountEt.getText().toString().trim();
            Log.d("DIALOG", "\"" + name + "\"" + count + "\"");
            Role newRole;
            if(!name.isEmpty() && !count.isEmpty()) {
                newRole = new Role(name, Integer.parseInt(count));
                if (lottery.addNewRole(newRole)) {
                    inflateNewRole(newRole);
                    Toast.makeText(this, R.string.dialog_newrole_success, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.dialog_newrole_errormsg_existed, Toast.LENGTH_LONG).show();
                }
            }
            else
                Toast.makeText(this, R.string.dialog_newrole_errormsg_fill, Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNegBtnClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNewRole:
                DialogFragment fragment = new NewRoleDialog();
                fragment.show(getFragmentManager(), "newrole");
                break;
            case R.id.btnStartLottery:
                if(validateData()) {
                    Intent i = new Intent(this, LotteryActivity.class);
                    i.putExtra("lottery", lotteryGson.toJson(lottery));
                    startActivity(i);
                    finish();
                }
                break;
        }
    }

    private boolean validateData() {
        String mafiaCnt = mafiaCountEt.getText().toString();
        String citizenCnt = citizenCountEt.getText().toString();
        if(!mafiaCnt.equals("") && !citizenCnt.equals("")) {
            lottery.updateRoleCount(Lottery.MAFIA_NAME, Integer.valueOf(mafiaCnt));
            lottery.updateRoleCount(Lottery.CITIZEN_NAME, Integer.valueOf(citizenCnt));
            int playerCntUser = playerCountNp.getValue() - 1;
            int playerCnt = lottery.getPlayerCount();
            Log.d("CNT", String.format("%d %d", playerCnt, playerCntUser));
            if (playerCnt == playerCntUser) {
                return true;
            } else {
                Toast.makeText(this, "تعداد بازیکنان با نقش ها برابر نیست", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else {
            Toast.makeText(this, R.string.dialog_newrole_errormsg_fill, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void onExtraRoleClicked(View v) {
        View view = (View) v.getParent();
        String name = ((TextView)view.findViewById(R.id.extraRoleNameTv)).getText().toString();
        lottery.removeRole(name);
        newRolesContainer.removeView(view);
    }

    private NumberPicker setNumberPicker(int minVal, int maxVal, int defaultVal, int id) {
        NumberPicker np = (NumberPicker)findViewById(R.id.playerCountNp);
        String[] displayedVals = new String[maxVal-minVal+1];
        for(int i = 0; i < displayedVals.length; i++)
            displayedVals[i] = Integer.toString(i);

        np.setMinValue(minVal);
        np.setMaxValue(maxVal);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(displayedVals);
        np.setValue(defaultVal);

        return np;
    }

    private void inflateNewRole(Role role) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.new_role_layout, null);
        TextView roleName = (TextView)view.findViewById(R.id.extraRoleNameTv);
        roleName.setText(role.getName());
        TextView roleCount = (TextView)view.findViewById(R.id.extraRoleCountTv);
        roleCount.setText(String.valueOf(role.getCount()));
        newRolesContainer.addView(view);
    }

    private void initializeViews() {
        playerCountNp = setNumberPicker(1, 100, 1, R.id.playerCountNp);
        newRoleBtn = (Button)findViewById(R.id.btnNewRole);
        startBtn = (Button)findViewById(R.id.btnStartLottery);
        mafiaCountEt = (EditText)findViewById(R.id.mafiaCountEditText);
        citizenCountEt = (EditText)findViewById(R.id.citizenCountEditText);
        citizenCountEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    citizenCountEt.clearFocus();
                }
                return false;
            }
        });
        newRolesContainer = (LinearLayout)findViewById(R.id.other_roles_layout);
        newRoleBtn.setOnClickListener(this);
        startBtn.setOnClickListener(this);
    }
}
