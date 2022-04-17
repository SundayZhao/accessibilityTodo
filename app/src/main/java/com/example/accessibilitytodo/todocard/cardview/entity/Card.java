package com.example.accessibilitytodo.todocard.cardview.entity;


import com.example.accessibilitytodo.todocard.cardview.adapter.DoneCardViewAdapter;

public class Card {
    public static int TYPE_DONE=0;
    public static int TYPE_TODO=1;

    private String id ;
    private String content;
    private String comment = "";
    private int type=0;
    private String date;
    private DoneCardViewAdapter.OnItemClickListener listener;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getState() {
        return type;
    }

    public void setState(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DoneCardViewAdapter.OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(DoneCardViewAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
