package com.yadaniil.fap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yadaniil.fap.db.Record;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by daniil on 25.08.16.
 */
public class RecordsAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Record> recordList;

    public RecordsAdapter(Context contex, List<Record> recordList) {
        this.context = contex;
        this.recordList = recordList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);
        return new RecordHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Record record = recordList.get(position);
        RecordHolder recordHolder = (RecordHolder) holder;

        String amountString = record.isEarned() ? "+" : "-" + record.getAmount();
        recordHolder.amount.setText(amountString);

        recordHolder.description.setText(record.getDescription());

        String dateString = new SimpleDateFormat("HH:mm dd-MM-yyyy").format(record.getDate());
        recordHolder.date.setText(dateString);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }


    private class RecordHolder extends RecyclerView.ViewHolder {

        private TextView amount;
        private TextView date;
        private TextView description;

        public RecordHolder(View itemView) {
            super(itemView);
            amount = (TextView) itemView.findViewById(R.id.amount_textView);
            date = (TextView) itemView.findViewById(R.id.date_textView);
            description = (TextView) itemView.findViewById(R.id.description_textView);
        }
    }
}
