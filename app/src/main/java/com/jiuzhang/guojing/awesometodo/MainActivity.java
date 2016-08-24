package com.jiuzhang.guojing.awesometodo;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.jiuzhang.guojing.awesometodo.models.Todo;
import com.jiuzhang.guojing.awesometodo.utils.ModelUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQ_CODE_TODO_EDIT = 100;

    private static final String TODOS = "todos";

    private TodoListAdapter adapter;
    private List<Todo> todos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TodoEditActivity.class);
                startActivityForResult(intent, REQ_CODE_TODO_EDIT);
            }
        });

        loadData();

        adapter = new TodoListAdapter(this, todos);
        ((ListView) findViewById(R.id.main_list_view)).setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_TODO_EDIT && resultCode == Activity.RESULT_OK) {
            Todo todo = data.getParcelableExtra(TodoEditActivity.KEY_TODO);
            updateTodo(todo);
        }
    }

    private void updateTodo(Todo todo) {
        boolean found = false;
        for (int i = 0; i < todos.size(); ++i) {
            Todo item = todos.get(i);
            if (TextUtils.equals(item.id, todo.id)) {
                found = true;
                todos.set(i, todo);
                break;
            }
        }

        if (!found) {
            todos.add(todo);
        }

        ModelUtils.save(this, TODOS, todos);

        adapter.notifyDataSetChanged();
    }

    public void updateTodo(int index, boolean done) {
        todos.get(index).done = done;
        adapter.notifyDataSetChanged();

        ModelUtils.save(this, TODOS, todos);
    }

    private void loadData() {
        todos = ModelUtils.read(this, TODOS, new TypeToken<List<Todo>>(){});
        if (todos == null) {
            todos = new ArrayList<>();
        }
    }
}
