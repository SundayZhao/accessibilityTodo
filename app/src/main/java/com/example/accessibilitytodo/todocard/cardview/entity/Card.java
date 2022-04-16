package com.example.accessibilitytodo.todocard.cardview.entity;



public class Card {
    public static int TYPE_DONE=0;
    public static int TYPE_TODO=1;

    private String content;
    private String comment = "";
    private int type=0;

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
}
