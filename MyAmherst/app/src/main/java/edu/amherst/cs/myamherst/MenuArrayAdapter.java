package edu.amherst.cs.myamherst;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.amherst.cs.myamherst.FileUtil;
import edu.amherst.cs.myamherst.Menu;
import edu.amherst.cs.myamherst.R;

/**
 * Created by aprasad on 1/29/17.
 */

public class MenuArrayAdapter extends ArrayAdapter<Menu> {

    private final Context context;
//    private final String[] values;

    String priorType = "";
    String priorSection = "";

    ArrayList<Menu> items;

    public MenuArrayAdapter(Context ctx, ArrayList<Menu> items)
    {
        super(ctx, R.layout.list_menu);
        this.context = ctx;
       // items = FileUtil.readFromFile(context);

        this.items = items;
        Log.d("MenuArrayAdapter", "Read " + items.size() + " items");
    }

    public View getView (int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_menu, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.type);
        TextView textView2 = (TextView) rowView.findViewById(R.id.section);
        TextView textView3 = (TextView) rowView.findViewById(R.id.items);

        Log.d("MenuArrayAdapter", "Row "+position);

        String type = getItem(position).getType();
        if(!priorType.equals(type)) {
            priorType=type;
            textView1.setText(type);

        }

        String section = items.get(position).getSection();
        if(!priorSection.equals(section)) {
            priorSection=section;
            textView2.setText(section);

        }

        textView3.setText(items.get(position).getItems());

        return rowView;
    }

    //1
    @Override
    public int getCount() {
        return items.size();
    }

    //2
    @Override
    public Menu getItem(int position) {
        return items.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

//    //4
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get view for row item
//        View rowView = mInflater.inflate(R.layout.list_item_recipe, parent, false);
//
//        return rowView;
//    }

}
