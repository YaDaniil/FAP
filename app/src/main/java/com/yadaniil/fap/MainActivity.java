package com.yadaniil.fap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
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
import android.widget.TextView;

import com.yadaniil.fap.db.Record;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button plusButton;
    private Button minusButton;
    private TextView balanceTextView;
    private RecyclerView recordsRecyclewView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initRecyclerView();
        initPlusMinusButtons();
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
        showAlertDialogWithEditText(false);
    }

    private void showPlusDialog() {
        showAlertDialogWithEditText(true);
    }

    private void showAlertDialogWithEditText(final boolean isPlus) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = isPlus ? getString(R.string.input_your_income) : getString(R.string.input_your_costs);
        builder.setTitle(title);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_change_balance, null);
        builder.setView(dialogView);

        final EditText amountEditText = (EditText) dialogView.findViewById(R.id.dialog_amount_edit_text);
        final EditText descriptionEditText = (EditText) dialogView.findViewById(R.id.dialog_description_edit_text);

        showKeyboard(amountEditText);

        final AlertDialog alertDialog = builder.create();

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveAndProcessAmount(amountEditText.getText().toString(),
                        descriptionEditText.getText().toString(), isPlus);
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.cancel();
            }
        });

        builder.show();
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
        ((RecordsAdapter) recordsRecyclewView.getAdapter()).addData(record);
    }

    private void initViews() {
        plusButton = (Button) findViewById(R.id.plusButton);
        minusButton= (Button) findViewById(R.id.minusButton);
        balanceTextView = (TextView) findViewById(R.id.balanceTextView);
        recordsRecyclewView = (RecyclerView) findViewById(R.id.recordsRecyclerView);
    }


    private void initRecyclerView() {
        recordsRecyclewView.setLayoutManager(new LinearLayoutManager(this));
        recordsRecyclewView.addItemDecoration(new SimpleRecyclerViewDivider(this));

        List<Record> records;
        try {
            records = Record.listAll(Record.class);
        } catch (SQLiteException e) {
            records = new ArrayList<>();
            Log.e("MainActivity", "initRecyclerView:" + e.getMessage());
        }
        recordsRecyclewView.setAdapter(new RecordsAdapter(this, records));
    }
}
