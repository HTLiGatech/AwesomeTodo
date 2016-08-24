package com.jiuzhang.guojing.awesometodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jiuzhang.guojing.awesometodo.models.Todo;
import com.jiuzhang.guojing.awesometodo.utils.AlarmUtils;
import com.jiuzhang.guojing.awesometodo.utils.DateUtils;
import com.jiuzhang.guojing.awesometodo.utils.UIUtils;

import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("ConstantConditions")
public class TodoEditActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public static final String KEY_TODO = "todo";

    private EditText todoEdit;
    private TextView dateTv;
    private TextView timeTv;
    private CheckBox completeCb;

    private Todo todo;
    private Date remindDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        todo = getIntent().getParcelableExtra(KEY_TODO);
        remindDate = todo != null
                ? todo.remindDate
                : Calendar.getInstance().getTime();

        setupUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        setTitle(null);
    }

    private void setupUI(){
        setContentView(R.layout.activity_todo_detail);
        setupToolbar();

        todoEdit = (EditText) findViewById(R.id.toto_detail_todo_edit);
        dateTv = (TextView) findViewById(R.id.todo_detail_date);
        timeTv = (TextView) findViewById(R.id.todo_detail_time);
        completeCb = (CheckBox) findViewById(R.id.todo_detail_complete);

        dateTv.setText(DateUtils.dateToStringDate(remindDate));
        timeTv.setText(DateUtils.dateToStringTime(remindDate));

        if (todo != null) {
            todoEdit.setText(todo.text);
            UIUtils.setTextViewStrikeThrough(todoEdit, todo.done);
            completeCb.setChecked(todo.done);
        }

        final Calendar c = Calendar.getInstance();
        c.setTime(remindDate);

        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new DatePickerDialog(
                        TodoEditActivity.this,
                        TodoEditActivity.this,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new TimePickerDialog(
                        TodoEditActivity.this,
                        TodoEditActivity.this,
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true);
                dialog.show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.todo_detail_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndExit();
            }
        });

        completeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UIUtils.setTextViewStrikeThrough(todoEdit, isChecked);
                todoEdit.setTextColor(isChecked ? Color.GRAY : Color.WHITE);
            }
        });

        View completeWrapper = findViewById(R.id.todo_detail_complete_wrapper);
        completeWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeCb.setChecked(!completeCb.isChecked());
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.setTime(remindDate);
        c.set(year, monthOfYear, dayOfMonth);

        remindDate = c.getTime();
        dateTv.setText(DateUtils.dateToStringDate(remindDate));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(remindDate);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);

        remindDate = c.getTime();
        timeTv.setText(DateUtils.dateToStringTime(remindDate));
    }

    private void saveAndExit() {
        if (todo == null) {
            todo = new Todo(todoEdit.getText().toString(), remindDate);
        }

        todo.done = completeCb.isChecked();
        todo.text = todoEdit.getText().toString();
        todo.remindDate = remindDate;

        AlarmUtils.setAlarm(this, remindDate);

        Intent result = new Intent();
        result.putExtra(KEY_TODO, todo);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
