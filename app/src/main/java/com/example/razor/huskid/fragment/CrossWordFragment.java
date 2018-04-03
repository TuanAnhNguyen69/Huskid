package com.example.razor.huskid.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.example.razor.huskid.GlideApp;
import com.example.razor.huskid.R;
import com.example.razor.huskid.adapter.AlphabetAdapter;
import com.example.razor.huskid.adapter.TileAdapter;
import com.example.razor.huskid.entity.CrossWord;
import com.example.razor.huskid.entity.EnglishWord;
import com.example.razor.huskid.entity.Tile;
import com.example.razor.huskid.helper.DatabaseHelper;

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
    GridView board;

    @BindView(R.id.alphabet)
    GridView alphabet;

    @BindView(R.id.suggest)
    ConstraintLayout suggestLayout;

    @BindView(R.id.suggestQuestion)
    TextView suggestQuestion;

    @BindView(R.id.sugggestImage)
    ImageView suggestImage;

    @BindView(R.id.input)
    ConstraintLayout inputLayout;

    @BindView(R.id.wordInput)
    PinView wordInput;

    @BindView(R.id.exit)
    ImageView exitSuggest;

    @BindView(R.id.reset)
    ImageButton reset;

    private int mParam1;
    private int mParam2;

    ArrayList<EnglishWord> words;
    ArrayList<EnglishWord> order;
    ArrayList<EnglishWord> added;
    ArrayList<Tile> tiles;
    CrossWord crossWord;
    TileAdapter tileAdapter;
    AlphabetAdapter alphabetAdapter;

    StringBuilder currentInputWord;
    int currentWordLength;
    EnglishWord currentSelectWord;
    ArrayList<Integer> currentSelectWordTileIndex;

    String topic;

    private OnFragmentInteractionListener mListener;

    public CrossWordFragment() {
        // Required empty public constructor
        words = new ArrayList<>();
        order = new ArrayList<>();
        tiles = new ArrayList<>();
        added = new ArrayList<>();
        topic = "the body";
        currentWordLength = 0;
        currentSelectWordTileIndex = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param width Width of board.
     * @param height Height of board.
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
        tileAdapter = new TileAdapter(getContext(), tiles);
        loadWord();
        genCrossWord();
        genTiles();
        initBoard();
        initInput();
        initSuggest();

        return rooView;
    }

    private void initSuggest() {
        suggestLayout.setVisibility(View.GONE);
        exitSuggest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                suggestLayout.setVisibility(View.GONE);
            }
        });
    }

    private void genTiles() {
        for (int rowIndex = 0; rowIndex < crossWord.getWidth(); rowIndex++) {
            for (int colIndex = 0; colIndex < crossWord.getHeight(); colIndex++)
            {
                int currentWordIndex;
                Tile tile = new Tile();
                char character = crossWord.getBoard()[rowIndex][colIndex];

                if (character == '*') {
                    tile.setCharacter(' ');
                } else {
                    tile.setCharacter(character);
                }

                currentWordIndex = crossWord.getHorizontalWords()[rowIndex][colIndex];
                if (currentWordIndex != 0){
                    tile.setHorizontalNumber(currentWordIndex);
                }

                currentWordIndex = crossWord.getVerticalWords()[rowIndex][colIndex];
                if (currentWordIndex != 0){
                    tile.setVerticalNumber(currentWordIndex + crossWord.getHorizontalCount());
                }

                tiles.add(tile);
            }
        }
    }

    private void initBoard() {
        tileAdapter = new TileAdapter(getContext(), tiles);
        board.setAdapter(tileAdapter);
        board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tiles.get(position).getVerticalNumber() <= 0 && tiles.get(position).getHorizontalNumber() <=0) {
                    return;
                }

                setCurrentSelectedTiles(false);
                String word = getWord(position);
                currentSelectWord = getEnglishWord(word);
                wordInput.setItemCount(currentWordLength);
                suggestLayout.setVisibility(View.VISIBLE);

                GlideApp
                        .with(getContext())
                        .load(currentSelectWord.getImage())
                        .placeholder(R.drawable.ic_launcher_background)
                        .fitCenter()
                        .into(suggestImage);
                setCurrentSelectedTiles(true);
            }
        });


    }

    private void setCurrentSelectedTiles(boolean selected) {
        for (Integer integer : currentSelectWordTileIndex) {
            tiles.get(integer).setSelected(selected);
        }

        tileAdapter.notifyDataSetChanged();
    }

    private void checkResult() {
        if (currentInputWord.toString().equalsIgnoreCase(currentSelectWord.getWord())) {
            showWord();
        } else {
            showError();
        }

        wordInput.getText().clear();
        currentInputWord = new StringBuilder();
    }

    private void showError() {
        wordInput.setBackgroundColor(Color.RED);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wordInput.setBackgroundColor(Color.WHITE);
            }
        }, 500);
    }

    private EnglishWord getEnglishWord(String word) {
        for (EnglishWord englishWord : added) {
            if (englishWord.getWord().equalsIgnoreCase(word)) {
                return englishWord;
            }
        }
        return new EnglishWord();
    }

    private String getWord(int position) {
        StringBuilder word = new StringBuilder();
        Tile tile = tiles.get(position);
        ArrayList<Integer> tileIndex = new ArrayList<>();


        if (tile.getHorizontalNumber() > 0) {
            int rowStart = position / 10 * 10;
            for (int i = position; i >= rowStart ; i--) {
                char character = tiles.get(i).getCharacter();
                if (character == ' ') {
                    break;
                }
                word.append(tiles.get(i).getCharacter().charValue());
                tileIndex.add(i);
            }
            word.reverse();

            for (int i = position + 1; i < rowStart + 10; i++) {
                char character = tiles.get(i).getCharacter();
                if (character == ' ') {
                    break;
                }
                word.append(tiles.get(i).getCharacter().charValue());
                tileIndex.add(i);
            }

        } else if (tile.getVerticalNumber() > 0) {
            int colStart = position % 10;
            for (int i = position; i >= colStart; i-=10) {
                char character = tiles.get(i).getCharacter();
                if (character == ' ') {
                    break;
                }
                word.append(tiles.get(i).getCharacter().charValue());
                tileIndex.add(i);
            }
            word.reverse();

            for (int i = position + 10; i < colStart + 100; i += 10) {
                char character = tiles.get(i).getCharacter();
                if (character == ' ') {
                    break;
                }
                word.append(tiles.get(i).getCharacter().charValue());
                tileIndex.add(i);
            }
        }

        this.currentSelectWordTileIndex = tileIndex;
        this.currentWordLength = word.length();
        return word.toString();
    }

    private void initInput() {
        alphabetAdapter = new AlphabetAdapter(getContext());
        alphabet.setAdapter(alphabetAdapter);
        alphabet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentInputWord == null) {
                    currentInputWord = new StringBuilder();
                }

                if (currentInputWord.length() >= wordInput.getItemCount()) {
                    return;
                }

                assert ((Character) alphabetAdapter.getItem((position))) != null;
                currentInputWord.append(((Character) alphabetAdapter.getItem((position))).charValue());
                wordInput.setText(currentInputWord.toString());
            }
        });

        wordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == currentWordLength) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkResult();
                        }
                    }, 1000);
                }
            }
        });
        wordInput.setClickable(false);
        wordInput.setFocusableInTouchMode(false);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetInput();
            }
        });
    }

    private void resetInput() {
        wordInput.getText().clear();
        currentInputWord = new StringBuilder();
        wordInput.setBackgroundColor(Color.WHITE);
    }

    private void showWord() {
        for (Integer integer : currentSelectWordTileIndex) {
            tiles.get(integer).setShow(true);
        }

        wordInput.setBackgroundColor(Color.GREEN);
        tileAdapter.notifyDataSetChanged();
        suggestLayout.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetInput();
            }
        }, 500);
    }

    private void genCrossWord() {
        crossWord.inRTL = false;
        crossWord.reset();

        genOrder();


        for (EnglishWord word: order) {
            if (crossWord.addWord(word.getWord()) != -1) {
                added.add(word);
            }
        }
    }

    private void loadWord() {
        words = (ArrayList<EnglishWord>) DatabaseHelper.getInstance(getContext()).getTopicWords(topic);
    }

    private void genOrder() {
        order.clear();
        Random random = new Random();
        for (EnglishWord word: words) {
            if (random.nextDouble() > 0.3) {
                order.add(word);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

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
    }
}
