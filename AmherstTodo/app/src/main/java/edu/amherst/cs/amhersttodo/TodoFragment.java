package edu.amherst.cs.amhersttodo;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aprasad on 3/5/17.
 */

public class TodoFragment extends Fragment {

    ListView listview;
    TextView searchText;
    ArrayList<TodoItem> items;
    Button searchBtn, clearBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_todo, container, false);
        listview = (ListView) v.findViewById(R.id.listview);
        searchBtn = (Button) v.findViewById(R.id.searchBtn);
        clearBtn = (Button) v.findViewById(R.id.clearBtn);
        searchText = (TextView) v.findViewById(R.id.searchTxt);
        items = loadItems();

        TodoArrayAdapter adapter = new TodoArrayAdapter(getActivity(), items);
        listview.setAdapter(adapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filterTxt = searchText.getText().toString();
                ArrayList<TodoItem> filterList = new ArrayList<TodoItem>();

                items = loadItems();

                for(int i=0; i<items.size(); i++)
                {
                    String title = items.get(i).getTitle();
                    String desc = items.get(i).getDesc();

                    if(title.toLowerCase().indexOf(filterTxt)!=-1 ||
                            desc.toLowerCase().indexOf(filterTxt) != -1) {
                        filterList.add(items.get(i));
                    }
                }

                TodoArrayAdapter adapter = new TodoArrayAdapter(getActivity(), filterList);
                listview.setAdapter(adapter);
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
                items = loadItems();
                TodoArrayAdapter adapter = new TodoArrayAdapter(getActivity(), items);
                listview.setAdapter(adapter);
            }
        });

        return v;

    }

    public ArrayList<TodoItem> loadItems()
    {
        items = new ArrayList<>();
        items.add(new TodoItem("342 Exam", "Write Exam", "Urgent"));
        items.add(new TodoItem("111 Exam", "Write Exam", "Urgent"));
        items.add(new TodoItem("Paper", "Read related work", "Important"));
        items.add(new TodoItem("Run", "Go for a run", "Beneficial"));
        return items;
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
