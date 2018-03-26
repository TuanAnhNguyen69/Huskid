package com.example.razor.huskid.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.razor.huskid.R;
import com.example.razor.huskid.entity.CrossWord;
import com.github.glomadrian.codeinputlib.CodeInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrossWordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrossWordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrossWordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.board)
    TableLayout board;

    @BindView(R.id.alphabet)
    TableLayout alphabet;

    @BindView(R.id.suggest)
    ConstraintLayout suggestLayout;

    @BindView(R.id.suggestQuestion)
    TextView suggestQuestion;

    @BindView(R.id.sugggestImage)
    ImageView suggestImage;

    @BindView(R.id.input)
    ConstraintLayout inputLayout;

    @BindView(R.id.wordInput)
    CodeInput wordInput;

    private int mParam1;
    private int mParam2;

    ArrayList<String> words;
    ArrayList<String> order;
    CrossWord crossWord;

    private OnFragmentInteractionListener mListener;

    public CrossWordFragment() {
        // Required empty public constructor
        words = new ArrayList<>();
        order = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param width Width of board.
     * @param height Height ò board.
     * @return A new instance of fragment CrossWordFragment.
     */
    public static CrossWordFragment newInstance(int width, int height) {
        CrossWordFragment fragment = new CrossWordFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, width);
        args.putInt(ARG_PARAM2, height);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1, 8);
            mParam2 = getArguments().getInt(ARG_PARAM2, 8);
        }
        crossWord = new CrossWord(mParam1, mParam2);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rooView = inflater.inflate(R.layout.fragment_cross_word, container, false);
        ButterKnife.bind(this, rooView);
        genCrossWord();
        return rooView;
    }

    private void genCrossWord() {
        crossWord.inRTL = false;
        crossWord.reset();

        genOrder();


        for (String word: order) {
            crossWord.addWord(word);
        }
    }

    private void loadWord() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("word.txt")));
            while (bufferedReader.readLine() != null) {
                String line = bufferedReader.readLine();
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void genOrder() {
        order.clear();
        Random random = new Random();
        for (String word: words) {
            if (random.nextDouble() > 0.3) {
                order.add(word);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
