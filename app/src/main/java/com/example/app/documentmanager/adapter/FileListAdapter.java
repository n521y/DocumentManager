package com.example.app.documentmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.app.documentmanager.R;
import com.example.app.documentmanager.bean.CommonBean;
import java.util.ArrayList;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private boolean mFlag;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnRecyclerItemLongListener mOnItemLong = null;
    private Context mcontext;
    private LayoutInflater inflater;
    private ArrayList<CommonBean> arrayList;

    public FileListAdapter (Context context, ArrayList<CommonBean> arrayList, boolean flag){
        this.mcontext=context;
        this.arrayList=arrayList;
        inflater=LayoutInflater.from(context);
        this.mFlag=flag;

    }

    static class  ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private OnRecyclerViewItemClickListener mOnItemClickListener = null;
        private OnRecyclerItemLongListener mOnItemLong = null;
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView,OnRecyclerViewItemClickListener mListener,OnRecyclerItemLongListener longListener) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.id_icon);
            textView=(TextView)itemView.findViewById(R.id.id_filename);
            this.mOnItemClickListener = mListener;
            this.mOnItemLong = longListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnItemLong != null) {
                mOnItemLong.onItemLongClick(v, getPosition());
            }
            return false;
        }
    }


    @NonNull
    @Override
    public FileListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
          if(mFlag) {
              view = inflater.inflate(R.layout.item_listview_common_grid, viewGroup, false);
          }else {
              view = inflater.inflate(R.layout.item_listview_common, viewGroup, false);
              }
        ViewHolder holder = new ViewHolder(view, mOnItemClickListener,mOnItemLong);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FileListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.imageView.setImageBitmap(arrayList.get(i).getIcon(mcontext));
        viewHolder.textView.setText(arrayList.get(i).getTitle());

    }

    public void removeItem(int position){
        arrayList.remove(position);
        //notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void renameItem(int position,String newName){
        arrayList.get(position).setTitle(newName);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int data);

    }
    public interface OnRecyclerItemLongListener{
        void onItemLongClick(View view,int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public void setOnItemLongClickListener(OnRecyclerItemLongListener listener){
        this.mOnItemLong =  listener;
    }


}
