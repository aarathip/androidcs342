package edu.amherst.cs.myamherst;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
  */
public class MenuFragment extends Fragment {

    private TextView menuTxt;


     ListView listview;

    private OnFragmentInteractionListener mListener;

    public MenuFragment() {
        // Required empty public constructor
    }

//    // TODO: Rename and change types and number of parameters
//    public static MenuFragment newInstance(String param1, String param2) {
//        MenuFragment fragment = new MenuFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_menu, container, false);

        //Text view only
//        menuTxt = (TextView) v.findViewById(R.id.menuTxt);
//        menuTxt.setText("Hello hello");


        //List view
        listview = (ListView) v.findViewById(R.id.listview);

        //Simple string list
        ArrayList<Menu> items = FileUtil.readFromFile(getActivity());

        ArrayList<String> listItems = new ArrayList<String>();
        for(int i = 0; i < items.size(); i++){
            Menu mItem = items.get(i);
            listItems.add(mItem.getItems());
        }

//        ArrayAdapter adapter = new ArrayAdapter
//                (getActivity(), android.R.layout.simple_list_item_1, listItems);
////
//        //Custom implementation
//
        MenuArrayAdapter adapter = new MenuArrayAdapter(getActivity(), items);
        listview.setAdapter(adapter);
        return v;

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
