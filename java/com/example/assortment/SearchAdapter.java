package com.example.assortment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.assortment.model.SearchMesureDust;

import java.util.ArrayList;


/**
 * Created by Administrator on 2018-04-04.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemViewHolder> {
    //크롤링한 뉴스정보를 실제로 List 화 시켜주는 Adapter 이다.


    private Context mContext;
    private ArrayList<SearchMesureDust> mSearchMesureDustList;


    public SearchAdapter(Context mContext) {
        this.mContext = mContext;
        this.mSearchMesureDustList = new ArrayList<>();
    }


    public synchronized void insertData(SearchMesureDust news) {
        // 데이터 받을시마다  추가시킨다.
        this.mSearchMesureDustList.add(news);
        notifyItemInserted(mSearchMesureDustList.size() - 1);
    }

    public synchronized void addAll(ArrayList<SearchMesureDust> news) {
        // 데이터 다받은후 한꺼번에 추가시킨다.
        mSearchMesureDustList.clear();
        mSearchMesureDustList.addAll(news);
        notifyDataSetChanged();
    }


    public ArrayList<SearchMesureDust> getData() {
        return mSearchMesureDustList;
    }

    public void delete(int index) {
        this.mSearchMesureDustList.remove(index);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mSearchMesureDustList != null && mSearchMesureDustList.size() > 0) {
            mSearchMesureDustList.clear();
        }
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //ListView에 보여줄 View를 만들어주는 부분이다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        //각각의 View의 Text에 각 정보를 View에 표시해주기위해 사용한다.
        final SearchMesureDust news = mSearchMesureDustList.get(position);

        holder.tvname.setText("도시명 : " + news.getCityName());
        holder.tvpm10.setText("미세먼지 : " + news.getPm10Value());
        holder.tvpm25.setText("초미세먼지 : " + news.getPm25Value());


    }

    @Override
    public int getItemCount() {
        return mSearchMesureDustList == null ? 0 : mSearchMesureDustList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rootlayout;
        private TextView tvname;
        private TextView tvpm10;
        private TextView tvpm25;

        public ItemViewHolder(View itemView) {
            super(itemView);
            rootlayout = (RelativeLayout) itemView.findViewById(R.id.view);
            tvname = (TextView) itemView.findViewById(R.id.name);
            tvpm10 = (TextView) itemView.findViewById(R.id.date);
            tvpm25 = (TextView) itemView.findViewById(R.id.publisher);
        }
    }
}
