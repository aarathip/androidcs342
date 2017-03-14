package edu.amherst.cs.amhersttodo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aprasad on 3/5/17.
 */

public class TodoArrayAdapter extends ArrayAdapter<TodoItem> {

    private final Context context;
    

    ArrayList<TodoItem> items;

    public TodoArrayAdapter(Context ctx, ArrayList<TodoItem> items)
    {
        super(ctx, R.layout.list_todo);
        this.context = ctx;
        // items = FileUtil.readFromFile(context);


        this.items = items;

    }

    public View getView (int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_todo, parent, false);
        final TextView textView1 = (TextView) rowView.findViewById(R.id.title);
        final TextView textView2 = (TextView) rowView.findViewById(R.id.desc);
        final TextView textView3 = (TextView) rowView.findViewById(R.id.priority);
        final CheckBox checkbox1 = (CheckBox) rowView.findViewById(R.id.checkbox);


        final String desc = getItem(position).getDesc();
        final String priority = getItem(position).getPriority();
        final int red = parent.getResources().getColor(R.color.colorRed);
        final int green = parent.getResources().getColor(R.color.colorGreen);
        final int purple = parent.getResources().getColor(R.color.colorPurple);

        textView1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text2 = textView2.getText().toString();
                if(text2.equals(desc)) {
                    textView2.setText("");
                    textView3.setText("");
                }
                else {
                    textView2.setText(desc);
                    textView3.setText(priority);
                    switch(priority)
                    {
                        case "Important":
                            textView3.setTextColor(red);
                            break;
                        case "Urgent":
                            textView3.setTextColor(purple);
                            break;
                        case "Beneficial":
                            textView3.setTextColor(green);
                            break;
                    }
                }

            }
        });

        textView1.setText(getItem(position).getTitle());
        checkbox1.setOnClickListener( new View.OnClickListener()
        {
            public void onClick (View v)
            {
                if(checkbox1.isChecked()) {
                    textView1.setEnabled(false);
                    textView1.setPaintFlags(textView1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    textView2.setText("");
                    textView3.setText("");
                }
                else {
                    textView1.setPaintFlags(textView1.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    textView1.setEnabled(true);
                }
            }
        }


        );
        return rowView;
    }

    //1
    @Override
    public int getCount() {
        return items.size();
    }

    //2
    @Override
    public TodoItem getItem(int position) {
        return items.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

}
