package com.example.accessibilitytodo.todocard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accessibilitytodo.R;
import com.example.accessibilitytodo.todocard.cardview.adapter.DoneCardViewAdapter;
import com.example.accessibilitytodo.todocard.cardview.adapter.TodoCardViewAdapter;
import com.example.accessibilitytodo.todocard.cardview.entity.Card;

import java.util.ArrayList;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {
    RecyclerView todolistview;
    RecyclerView donelistview;
    LinearLayoutManager mLayoutManager;
    TodoCardViewAdapter todoAdapter;
    DoneCardViewAdapter doneAdapter;
    List<Card> todoList = new ArrayList<>();
    List<Card> doneList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewRecyclerView();
    }

    public void initViewRecyclerView() {
        ScrollView view = (ScrollView) LayoutInflater.from(this).inflate(R.layout.activity_todolist, null);
        setContentView(view);

        todolistview = (RecyclerView) findViewById(R.id.recyclerview_todo);
        donelistview=(RecyclerView)findViewById(R.id.recyclerview_done);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        todolistview.setLayoutManager(mLayoutManager);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        donelistview.setLayoutManager(mLayoutManager);
        todoAdapter = new TodoCardViewAdapter(this);
        doneAdapter=new DoneCardViewAdapter(this);
        todolistview.setAdapter(todoAdapter);
        donelistview.setAdapter(doneAdapter);
        //模拟数据
        for (int i = 0; i < 10; i++) {
            Card card = new Card();
            String content = "刘雯、何穗、奚梦瑶、雎晓雯......在公开的 52 位模特名单里，一下子出现了四位中国模特。要知道，一年一度的顶级维密大秀可是为“国际超模”四个字加持的大秀啊。接下来，时间将印证这四位模特的未来身价和职业生涯。";
            card.setContent(content);
            card.setState(Card.TYPE_DONE);
            todoList.add(card);
            card.setState(Card.TYPE_TODO);
            doneList.add(card);
        }
        todoAdapter.setData(todoList);
        doneAdapter.setData(doneList);
        todoAdapter.setDoneCardViewAdapter(doneAdapter);
        doneAdapter.setTodoCardView(todoAdapter);
        todoAdapter.notifyDataSetChanged();
        doneAdapter.notifyDataSetChanged();
    }
}
