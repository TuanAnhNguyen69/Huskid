package com.foodtiny.razor.elkid.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.foodtiny.razor.elkid.GlideApp;
import com.foodtiny.razor.elkid.R;
import com.foodtiny.razor.elkid.adapter.TileAdapter;
import com.foodtiny.razor.elkid.adapter.WordTableAdapter;
import com.foodtiny.razor.elkid.entity.CrossWord;
import com.foodtiny.razor.elkid.entity.EnglishWord;
import com.foodtiny.razor.elkid.entity.GetMedia;
import com.foodtiny.razor.elkid.entity.Tile;
import com.foodtiny.razor.elkid.helper.DatabaseHelper;
import com.foodtiny.razor.elkid.helper.SoundPlayer;
import com.foodtiny.razor.elkid.helper.Utils;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

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


    @BindView(R.id.parent)
    ConstraintLayout parent;

    @BindView(R.id.board)
    GridView board;

    @BindView(R.id.alphabet)
    TableLayout alphabet;

    @BindView(R.id.qrow)
    TableRow qRow;

    @BindView(R.id.arow)
    TableRow aRow;

    @BindView(R.id.zrow)
    TableRow zRow;

    @BindView(R.id.suggest)
    ConstraintLayout suggestLayout;

    @BindView(R.id.sugggestImage)
    ImageView suggestImage;

    @BindView(R.id.input)
    ConstraintLayout inputLayout;

    @BindView(R.id.wordInput)
    PinView wordInput;

    @BindView(R.id.exit)
    ImageView exitSuggest;

    @BindView(R.id.check)
    ImageButton check;

    @BindView(R.id.reset)
    ImageButton reset;

    @BindView(R.id.horizontal_word)
    GridView horizontalWordsListView;

    @BindView(R.id.board_layout)
    ConstraintLayout boardLayout;

    private int height;
    private String topic;

    ArrayList<EnglishWord> words;
    ArrayList<EnglishWord> order;
    ArrayList<EnglishWord> added;
    ArrayList<EnglishWord> horizontal;
    ArrayList<String> vertical;
    ArrayList<Tile> tiles;
    CrossWord crossWord;
    TileAdapter tileAdapter;
    WordTableAdapter wordTableAdapter;

    StringBuilder currentInputWord;
    int currentWordLength;
    EnglishWord currentSelectWord;
    ArrayList<Integer> currentSelectWordTileIndex;
    //GetMedia getMedia;

    private OnFragmentInteractionListener mListener;

    public CrossWordFragment() {
        // Required empty public constructor
        words = new ArrayList<>();
        order = new ArrayList<>();
        tiles = new ArrayList<>();
        added = new ArrayList<>();
        //getMedia = new GetMedia();
        currentWordLength = 0;
        currentSelectWordTileIndex = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param height Height of board.
     * @return A new instance of fragment CrossWordFragment.
     */
    public static CrossWordFragment newInstance(int height, String topic) {
        CrossWordFragment fragment = new CrossWordFragment();
        Bundle args = new Bundle();
        fragment.height = height;
        fragment.topic = topic;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crossWord = new CrossWord(height, height);

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
        initWordsList();
        initInput();
        initSuggest();
        return rooView;
    }

    private void initWordsList() {
        horizontal = new ArrayList<>();
        wordTableAdapter = new WordTableAdapter(getContext(), horizontal);
        horizontalWordsListView.setAdapter(wordTableAdapter);
    }

    private void initSuggest() {
        suggestLayout.setVisibility(GONE);
        exitSuggest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               exitSuggest();
            }
        });

        suggestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do nothing
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height * (width/12),
                height * (width/12));
        int margin = Utils.convertDpToPixel(10, getContext());
        tileAdapter = new TileAdapter(getContext(), tiles);
        params.setMargins(margin, margin, margin, margin);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);


//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.centerHorizontally(R.id.board, R.id.parent);
//        constraintSet.centerVertically(R.id.board, R.id.parent);
//        constraintSet.constrainHeight(R.id.board, height * Utils.convertDpToPixel(40, getContext()));
//        constraintSet.constrainWidth(R.id.board,  height * Utils.convertDpToPixel(40, getContext()));
//        constraintSet.applyTo(parent);
        board.setLayoutParams(params);
        board.setNumColumns(height);
        board.setAdapter(tileAdapter);
        board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tiles.get(position).getVerticalNumber() == 0 && tiles.get(position).getHorizontalNumber() == 0) {
                    return;
                }

                if (tiles.get(position).isShow()) {
                    return;
                }

                setCurrentSelectedTiles(false);
                String word = getWord(position);
                currentSelectWord = getEnglishWord(word);
                wordInput.setItemCount(currentWordLength);
                suggestLayout.setVisibility(View.VISIBLE);
                boardLayout.setVisibility(View.INVISIBLE);
//                getMedia.cancel(true);
//                getMedia = new GetMedia();
//                getMedia.execute(currentSelectWord.getAudio());
                exitSuggest.setVisibility(View.VISIBLE);
                wordInput.getText().clear();
                currentInputWord = new StringBuilder();

                //alphabetAdapter.setWord(currentSelectWord.getWord());
                //alphabetAdapter.notifyDataSetChanged();

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
            showCorrect();
            if (horizontal.size() == added.size()) {
                showComplete();
            }
        } else {
            showError();
        }
        currentInputWord = new StringBuilder();
    }

    private void showComplete() {
        mListener.buildWinLayout();
    }

    private void showCorrect() {
        wordInput.setBackgroundColor(Color.GREEN);
        int resID = getResources().getIdentifier("_" + currentSelectWord.getId(), "raw", getContext().getPackageName());
        //int resID = getResources().getIdentifier("_" + currentSelectWord.getWord().replaceAll(" ", "").toLowerCase(), "raw", getContext().getPackageName());
        SoundPlayer.getInstance().playMedia(getContext(), resID);
        horizontal.add(currentSelectWord);
        wordTableAdapter.notifyDataSetChanged();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wordInput.setBackgroundColor(Color.WHITE);
                showWord();
                exitSuggest();

            }
        }, 2000);
    }

    private void exitSuggest() {
        suggestLayout.setVisibility(GONE);
        boardLayout.setVisibility(View.VISIBLE);
        SoundPlayer.getInstance().stopMedia();
        exitSuggest.setVisibility(View.INVISIBLE);
        resetInput();
    }

    private void showError() {
        wordInput.setBackgroundColor(Color.RED);
        int resID = getResources().getIdentifier("_wrong", "raw", getContext().getPackageName());
        SoundPlayer.getInstance().playMedia(getContext(), resID);
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
            int rowStart = position / height * height;
            for (int i = position; i >= rowStart ; i--) {
                char character = tiles.get(i).getCharacter();
                if (character == ' ') {
                    break;
                }
                word.append(tiles.get(i).getCharacter().charValue());
                tileIndex.add(i);
            }
            word.reverse();

            for (int i = position + 1; i < rowStart + height; i++) {
                char character = tiles.get(i).getCharacter();
                if (character == ' ') {
                    break;
                }
                word.append(tiles.get(i).getCharacter().charValue());
                tileIndex.add(i);
            }

        } else if (tile.getVerticalNumber() > 0) {
            int colStart = position % height;
            for (int i = position; i >= colStart; i -= height) {
                char character = tiles.get(i).getCharacter();
                if (character == ' ') {
                    break;
                }
                word.append(tiles.get(i).getCharacter().charValue());
                tileIndex.add(i);
            }
            word.reverse();

            for (int i = position + height; i < colStart + (height * height); i += height) {
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
        String qRowChar = "QWERTYUIOP";
        for (int i = 0; i < qRowChar.length(); i++) {
            qRow.addView(createButton(qRowChar.charAt(i)));
        }

        String aRowChar = "ASDFGHJKL";
        for (int i = 0; i < aRowChar.length(); i++) {
            aRow.addView(createButton(aRowChar.charAt(i)));
        }

        String zRowChar = "ZXCVBNM";
        for (int i = 0; i < zRowChar.length(); i++) {
            zRow.addView(createButton(zRowChar.charAt(i)));
        }
//        alphabetAdapter = new AlphabetAdapter(getContext());
//        alphabet.setAdapter(alphabetAdapter);
//        alphabet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (currentInputWord == null) {
//                    currentInputWord = new StringBuilder();
//                }
//
//                if (currentInputWord.length() >= wordInput.getItemCount()) {
//                    return;
//                }
//
//                assert ((Character) alphabetAdapter.getItem((position))) != null;
//                currentInputWord.append(((Character) alphabetAdapter.getItem((position))).charValue());
//                wordInput.setText(currentInputWord.toString());
//                view.setVisibility(View.INVISIBLE);
//                alphabetAdapter.addClickPosition(position);
//
//            }
//        });
        wordInput.setClickable(false);
        wordInput.setFocusableInTouchMode(false);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetInput();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkResult();
            }
        });
    }

    private void deleteChar() {
        if (wordInput.getText().length() > 0) {
            wordInput.setText(currentInputWord.deleteCharAt(currentInputWord.length() - 1));
        }
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

        tileAdapter.notifyDataSetChanged();
    }

    private void genCrossWord() {
        crossWord.inRTL = false;
        crossWord.reset();

        genOrder();


        for (EnglishWord word: order) {
            if (crossWord.addWord(word.getWord()) != -1) {
                added.add(word);
                //SoundPlayer.getInstance().addMedia(word.getAudio());
            }
        }
    }

    private void loadWord() {
        words = (ArrayList<EnglishWord>) DatabaseHelper.getInstance().getTopicWords(topic);
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

    public Button createButton(final Character letter) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.width = (width/120) * 8;
        params.height = (int) (params.width * 1.5);
        int margin = (width/120);
        params.setMargins(margin, margin, margin, margin);
        Button fancyButton = new Button(getContext());
        fancyButton.setLayoutParams(params);
        fancyButton.setText(letter.toString());
        fancyButton.setTextColor(Color.WHITE);
        fancyButton.setBackgroundResource(R.drawable.key_bg);
        fancyButton.setTextSize((int) Utils.pixelsToSp(getContext(), (params.width / 10) * 8));
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentInputWord == null) {
                    currentInputWord = new StringBuilder();
                }

                if (currentInputWord.length() >= wordInput.getItemCount()) {
                    return;
                }
                currentInputWord.append(letter);
                wordInput.setText(currentInputWord.toString());
                //alphabetAdapter.addClickPosition(position);
            } }
        );
        return fancyButton;
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
        void buildWinLayout();
    }
}
