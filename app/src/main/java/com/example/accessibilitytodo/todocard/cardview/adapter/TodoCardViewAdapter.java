package com.example.accessibilitytodo.todocard.cardview.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.accessibilitytodo.R;
import com.example.accessibilitytodo.Uiconfiguration.CongratulationHelper;
import com.example.accessibilitytodo.todocard.cardview.entity.Card;
import com.example.accessibilitytodo.todocard.cardview.widget.TodoCardView;

import java.util.List;

public class TodoCardViewAdapter extends RecyclerView.Adapter<TodoCardViewAdapter.ViewHolder> {
    List<Card> mData;
    Context mContext;
    private DoneCardViewAdapter doneCardViewAdapter;
    int[] colors = {0xff01a3a1, 0xff91bbeb, 0xff01b1bf, 0xffff9d62, 0xff2d3867, 0xffee697e};//颜色组

    public void setData(List<Card> list) {
        this.mData = list;
    }

    public TodoCardViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = new TodoCardView(mContext);
        ViewHolder holder = new ViewHolder(view);
        ((TodoCardView)holder.itemView).setCardFont(CongratulationHelper.getFrontSize()* CongratulationHelper.BASICFRONTSIZE_TEXT,CongratulationHelper.getTextColor());
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //设置颜色变化
        ((TodoCardView) holder.itemView).changeTheme(colors[position % 6]);
        holder.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneCardViewAdapter.addItem(mData.get(position));
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

    public void setDoneCardViewAdapter(DoneCardViewAdapter doneCardViewAdapter) {
        this.doneCardViewAdapter = doneCardViewAdapter;
    }

    public void addItem(Card card){
        mData.add(card);
        notifyItemInserted(mData.size());
        notifyDataSetChanged();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout top;
        ImageView center;
        EditText comment;
        TextView content;
        ImageButton doneButton;
        ImageButton delButton;

        ViewHolder(View view) {
            super(view);
            top = (RelativeLayout) view.findViewById(R.id.top);
            center = (ImageView) view.findViewById(R.id.center);
            content = (TextView) view.findViewById(R.id.content);
            doneButton=(ImageButton)view.findViewById(R.id.button_done);
            delButton=(ImageButton)view.findViewById(R.id.button_del);
        }
    }
}
