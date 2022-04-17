package com.example.accessibilitytodo.todocard.cardview.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.accessibilitytodo.R;
import com.example.accessibilitytodo.Uiconfiguration.CongratulationHelper;
import com.example.accessibilitytodo.todocard.TodoListActivity;
import com.example.accessibilitytodo.todocard.addNewListActivity;
import com.example.accessibilitytodo.todocard.cardview.entity.Card;
import com.example.accessibilitytodo.todocard.cardview.widget.DoneCardView;
import com.example.accessibilitytodo.todocard.cardview.widget.TodoCardView;

import java.util.List;

public class DoneCardViewAdapter extends RecyclerView.Adapter<DoneCardViewAdapter.ViewHolder> {
    List<Card> mData;
    Context mContext;
    private  TodoCardViewAdapter todoCardView;

    int[] colors = {0xff01a3a1, 0xff91bbeb, 0xff01b1bf, 0xffff9d62, 0xff2d3867, 0xffee697e};//颜色组
    public void setData(List<Card> list) {
        this.mData = list;
    }

    public DoneCardViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = new DoneCardView(mContext);
        ViewHolder holder = new ViewHolder(view);
        ((DoneCardView)holder.itemView).setCardFont(CongratulationHelper.getFrontSize()* CongratulationHelper.BASICFRONTSIZE_TEXT,CongratulationHelper.getTextColor());
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //设置颜色变化
        ((DoneCardView) holder.itemView).changeTheme(colors[position % 6]);
        holder.content.setText(mData.get(position).getContent());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.get(position).getListener().notifyItem(TodoListActivity.ADD_RESULT_NOTIFY_DONE,mData.get(position));
            }
        });
        holder.header.setText(mData.get(position).getDate());
        holder.withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoCardView.addItem(mData.get(position));
                mData.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mData.size());
            }
        });
        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mData.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(Card card){
        mData.add(card);
        notifyItemInserted(mData.size());
        notifyDataSetChanged();
    }

    public void setTodoCardView(TodoCardViewAdapter todoCardView) {
        this.todoCardView = todoCardView;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout top;
        ImageView center;
        TextView header;
        TextView content;
        ImageButton withdrawButton;
        ImageButton delButton;

        ViewHolder(View view) {
            super(view);
            top = (RelativeLayout) view.findViewById(R.id.top);
            center = (ImageView) view.findViewById(R.id.center);
            header=(TextView)view.findViewById(R.id.header) ;
            content = (TextView) view.findViewById(R.id.content);
            withdrawButton=(ImageButton)view.findViewById(R.id.button_withdraw);
            delButton=(ImageButton)view.findViewById(R.id.button_del);
        }
    }

    public interface OnItemClickListener  {
        public void notifyItem(int Type,Card card) ;
    }
}
