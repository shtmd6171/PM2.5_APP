package com.example.assortment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assortment.model.DbHelper;
import com.example.assortment.model.SearchMesureDust;

import java.util.ArrayList;

public class Search_Adapter  extends RecyclerView.Adapter<Search_Adapter.ItemViewHolder> {
    //크롤링한 뉴스정보를 실제로 List 화 시켜주는 Adapter 이다.


    private Context mContext;
    public  ArrayList<SearchMesureDust> mSearchMesureDustList;


    public Search_Adapter(Context mContext) {
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
    public synchronized void addlist(ArrayList<SearchMesureDust> news) {
        // 데이터 다받은후 한꺼번에 추가시킨다.
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
    public Search_Adapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //ListView에 보여줄 View를 만들어주는 부분이다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new Search_Adapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Search_Adapter.ItemViewHolder holder, int position) {
        //각각의 View의 Text에 각 정보를 View에 표시해주기위해 사용한다.
        final SearchMesureDust news = mSearchMesureDustList.get(position);

        ArrayList<DbHelper.MiseModel> miseModels = ((BaseApp) mContext.getApplicationContext()).getDbHelper().getResult();
        DbHelper.MiseModel obj = null;

        for (DbHelper.MiseModel mise : miseModels){
            Log.d("<TAG>>>" ," MISE idx  : " + mise.getIdx());
            Log.d("<TAG>>>" ," MISE item  : " + mise.getItem());
            Log.d("<TAG>>>" ," CITY   : " + news.getCityName());
            Log.d("<TAG>>>" ," Validate   : " + news.getCityName().equals(mise.getItem()));
            if (news.getCityName().equals(mise.getItem())){
                news.setFavor(true);
                news.setIdx(mise.getIdx());
                break;
            }
        }


        holder.tvname.setText(news.getCityName());
        holder.tvpm10.setText(news.getPm10Value());
        holder.tvpm25.setText(news.getPm25Value());
        if (news.isFavor()){
            holder.imageView.setImageResource(R.drawable.star_pressed);
        }else{
            holder.imageView.setImageResource(R.drawable.star);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!news.isFavor()){
                    news.setFavor(true);
                    holder.imageView.setImageResource(R.drawable.star_pressed);
                    ((BaseApp) mContext.getApplicationContext()).getDbHelper().insert(news.getCityName(),news.getRootcity());
                    Log.d("<TAG>>>","ADD " + news.getRootcity());
                    Toast.makeText(mContext, "추가되었습니다", Toast.LENGTH_SHORT).show();
                }else{
                    news.setFavor(false);
                    holder.imageView.setImageResource(R.drawable.star);
                    ((BaseApp) mContext.getApplicationContext()).getDbHelper().delete(news.getIdx());
                    Toast.makeText(mContext, "삭제되었습니다", Toast.LENGTH_SHORT).show();

                }


                notifyDataSetChanged();



            }
        });



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
        private ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            rootlayout = (RelativeLayout) itemView.findViewById(R.id.view);
            tvname = (TextView) itemView.findViewById(R.id.item_location1);
            tvpm10 = (TextView) itemView.findViewById(R.id.dust_value2);
            tvpm25 = (TextView) itemView.findViewById(R.id.dust_value3);
            imageView = itemView.findViewById(R.id.star1);
        }
    }
}