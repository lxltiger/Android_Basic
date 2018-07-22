package com.example.lixiaolin.crimeintent.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lixiaolin.crimeintent.R;
import com.example.lixiaolin.crimeintent.activity.CrimePagerActivity;
import com.example.lixiaolin.crimeintent.entity.Crime;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by lixiaolin on 15/10/18.
 */
public class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.ViewHolder> {
    private List<Crime> mCrimeList;
    private Context mContext;
    private static int mPosition;
    private static SimpleDateFormat mSimpleDateFormat;
    public CrimeAdapter(List<Crime> crimeList, Context context) {
        mCrimeList = crimeList;
        mContext = context;
        mSimpleDateFormat = new SimpleDateFormat("E yyyy-MM-dd 'at' hh:mm:ss a zzz", Locale.CHINA);
    }

    @Override
    public CrimeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.crime_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CrimeAdapter.ViewHolder holder, int position) {
        Crime crime = mCrimeList.get(position);
//        holder.mTextViewTitle.setText(crime.getTitle());
//        holder.mTextViewDate.setText(crime.getDate().toString());
//        holder.mCheckBoxSolved.setChecked(crime.isSolved());
        holder.bindCrime(crime);
    }

    @Override
    public int getItemCount() {
        return mCrimeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private CheckBox mCheckBoxSolved;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mTextViewDate = (TextView) itemView.findViewById(R.id.tv_date);
            mCheckBoxSolved = (CheckBox) itemView.findViewById(R.id.cb_isSolved);
            itemView.setOnClickListener(this);
        }

        public void bindCrime(Crime crime) {
            mTextViewTitle.setText(crime.getTitle());
            mTextViewDate.setText(mSimpleDateFormat.format(crime.getDate()));
            mCheckBoxSolved.setChecked(crime.isSolved());
        }

        @Override
        public void onClick(View v) {
            mPosition=getAdapterPosition();
            Intent intent = CrimePagerActivity.newIntent(v.getContext(), getAdapterPosition());
            v.getContext().startActivity(intent);
        }
    }

    public int getPosition() {
        return mPosition;
    }

    public void setCrimeList(List<Crime> crimelist) {
        mCrimeList=crimelist;
    }
}
