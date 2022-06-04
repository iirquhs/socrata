package sg.edu.np.mad.socrata;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeworkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeworkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeworkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeworkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeworkFragment newInstance(String param1, String param2) {
        HomeworkFragment fragment = new HomeworkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<Module> data = new ArrayList<>();
        Module web = new Module("Web Applications Development", "AD", 30, R.color.secondary_color, getContext());
        Module ooad = new Module("Object-Oriented Analysis and Design", "AD", 5, R.color.text_color, getContext());
        data.add(web);
        data.add(ooad);
        data.add(web);
        data.add(ooad);
        data.add(web);
        data.add(ooad);
        data.add(web);
        data.add(ooad);
        data.add(web);
        data.add(ooad);
        data.add(web);
        data.add(ooad);
        data.add(web);
        data.add(ooad);
        data.add(web);
        data.add(ooad);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homework, container, false);

        RecyclerView rv = view.findViewById(R.id.homework_rcv);
        HomeworkAdapter adapter = new HomeworkAdapter(data);
        LinearLayoutManager layout = new LinearLayoutManager(view.getContext());

        rv.setAdapter(adapter);
        rv.setLayoutManager(layout);
        rv.setNestedScrollingEnabled(false);

        return view;
    }
}