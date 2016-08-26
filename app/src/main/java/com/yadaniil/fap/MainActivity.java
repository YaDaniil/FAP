package com.yadaniil.fap;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yadaniil.fap.db.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton plusButton;
    private ImageButton minusButton;
    private TextView balanceTextView;
    private RecyclerView recordsRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initRecyclerView();
        initPlusMinusButtons();
        getBalanceFromUserIfNecessary();
    }

    private void initPlusMinusButtons() {
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlusDialog();
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMinusDialog();
            }
        });
    }

    private void showMinusDialog() {
        showAlertDialogWithEditText(false, false);
    }

    private void showPlusDialog() {
        showAlertDialogWithEditText(true, false);
    }

    private void showAlertDialogWithEditText(final boolean isPlus, final boolean isBalanceAsking) {
        String title;
        if(isBalanceAsking) {
            title = getString(R.string.balance_asking);
        } else {
            title = isPlus ? getString(R.string.input_your_income)
                    : getString(R.string.input_your_costs);
        }

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_change_balance, null);

        final EditText amountEditText = (EditText) dialogView.findViewById(R.id.dialog_amount_edit_text);
        final EditText descriptionEditText = (EditText) dialogView.findViewById(R.id.dialog_description_edit_text);

        if(isBalanceAsking) {
            descriptionEditText.setVisibility(View.GONE);
            dialogView.findViewById(R.id.add_description).setVisibility(View.GONE);
        }

        showKeyboard(amountEditText);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle(title)
                .setPositiveButton(getString(R.string.ok), null)
                .setNegativeButton(getString(R.string.cancel), null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(amountEditText.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.wrong_amount_input_toast), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            if(isBalanceAsking) {
                                String amount = amountEditText.getText().toString();
                                SharedPrefHelper.getInstance().setBalance(amount);
                                setBalanceTextView(amount);
                                dialog.dismiss();
                            } else {
                                saveAndProcessAmount(amountEditText.getText().toString(),
                                        descriptionEditText.getText().toString(), isPlus);
                                dialog.dismiss();
                            }
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    private void showKeyboard(final EditText editText) {
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(editText, 0);
            }
        },50);
    }

    private void saveAndProcessAmount(String amount, String description, boolean isPlus) {
        Record record = new Record(new BigDecimal(amount), isPlus, new Date(), description);
        record.save();
        ((RecordsAdapter) recordsRecyclerView.getAdapter()).addData(record);
        BigDecimal oldBalance = new BigDecimal(SharedPrefHelper.getInstance().getBalance());
        BigDecimal newBalance = isPlus ? oldBalance.add(new BigDecimal(amount))
                : oldBalance.subtract(new BigDecimal(amount));
        SharedPrefHelper.getInstance().setBalance(newBalance.toPlainString());
        setBalanceTextView(newBalance.toPlainString());
    }

    private void initViews() {
        plusButton = (ImageButton) findViewById(R.id.plusButton);
        minusButton= (ImageButton) findViewById(R.id.minusButton);
        balanceTextView = (TextView) findViewById(R.id.balanceTextView);
        recordsRecyclerView = (RecyclerView) findViewById(R.id.recordsRecyclerView);
        getSupportActionBar().setElevation(0);
    }


    private void initRecyclerView() {
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordsRecyclerView.addItemDecoration(new SimpleRecyclerViewDivider(this));

        List<Record> records;
        try {
            records = Record.listAll(Record.class);
        } catch (SQLiteException e) {
            records = new ArrayList<>();
            Log.e("MainActivity", "initRecyclerView:" + e.getMessage());
        }
        recordsRecyclerView.setAdapter(new RecordsAdapter(this, records));
    }

    public void getBalanceFromUserIfNecessary() {
        if(SharedPrefHelper.getInstance().getBalance() == null) {
            showAskingBalanceDialog();
        } else {
            setBalanceTextView(SharedPrefHelper.getInstance().getBalance());
        }
    }

    private void showAskingBalanceDialog() {
        showAlertDialogWithEditText(true, true);
    }

    public void setBalanceTextView(String balanceTextView) {
        this.balanceTextView.setText(getString(R.string.balance) + ": " + balanceTextView
                + " " + getString(R.string.uah));
    }
}
