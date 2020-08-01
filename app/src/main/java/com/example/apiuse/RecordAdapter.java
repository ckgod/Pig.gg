package com.example.apiuse;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecordAdapter extends BaseAdapter {

    Context context;
    int layout;
    LayoutInflater inf;
    ArrayList<Record> records = new ArrayList<>();

    public RecordAdapter(Context context, int layout, ArrayList<Record> records) {
        this.context = context;
        this.layout = layout;
        this.records = records;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Record getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
            convertView = inf.inflate(layout, null);

        ImageView iv_championimg = convertView.findViewById(R.id.iv_championImg);
        TextView tv_kills = convertView.findViewById(R.id.tv_kills);
        TextView tv_deaths = convertView.findViewById(R.id.tv_deaths);
        TextView tv_assists = convertView.findViewById(R.id.tv_assists);
        Button btn_kda = convertView.findViewById(R.id.btn_kda);

        float k,d,a;
        float kda;

        Record record = records.get(position);
        if(record.getWins().equals("true")){
            convertView.setBackgroundColor(0xFF080336);
        }
        else{
            convertView.setBackgroundColor(0xFF360303);
        }

        int cpNum = record.getImres();

        iv_championimg.setImageResource(cpNum);
        tv_kills.setText(record.getKills());
        tv_deaths.setText(record.getDeaths());
        tv_assists.setText(record.getAssists());

        k = Integer.parseInt(record.getKills());
        d = Integer.parseInt(record.getDeaths());
        a = Integer.parseInt(record.getAssists());
        if(d != 0){
            kda = (k+a)/d;
            String strNumber = String.format("%.2f", kda);
            btn_kda.setText(strNumber);
        }
        else{
            btn_kda.setText("perpect");
        }

        return convertView;
    }
}
