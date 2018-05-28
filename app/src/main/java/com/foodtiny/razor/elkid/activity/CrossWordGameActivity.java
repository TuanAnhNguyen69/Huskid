package com.foodtiny.razor.elkid.activity;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.foodtiny.razor.elkid.GlideApp;
import com.foodtiny.razor.elkid.R;

import com.foodtiny.razor.elkid.adapter.TileAdapter;
import com.foodtiny.razor.elkid.adapter.WordTableAdapter;
import com.foodtiny.razor.elkid.entity.CrossWord;
import com.foodtiny.razor.elkid.entity.EnglishWord;
import com.foodtiny.razor.elkid.entity.Tile;
import com.foodtiny.razor.elkid.fragment.CrossWordFragment;
import com.foodtiny.razor.elkid.helper.DatabaseHelper;
import com.foodtiny.razor.elkid.helper.SoundPlayer;
import com.foodtiny.razor.elkid.helper.Storage;
import com.foodtiny.razor.elkid.helper.Utils;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.foodtiny.razor.elkid.activity.HomeActivity.BG_SOUND;
import static com.foodtiny.razor.elkid.activity.HomeActivity.LEVEL;
import static com.foodtiny.razor.elkid.activity.HomeActivity.TOPIC;


public class CrossWordGameActivity extends AppCompatActivity implements
        CrossWordFragment.OnFragmentInteractionListener {

    BlurPopupWindow exitBuilder;
    BlurPopupWindow winBuilder;

    @BindView(R.id.setting)
    ImageView setting;

    @BindView(R.id.mute)
    ImageView mute;

    @BindView(R.id.about)
    ImageView about;

    @BindView(R.id.setting_layout)
    ConstraintLayout settingLayout;

    boolean settingOpen;
    Storage storage;

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

    @BindView(R.id.word_table_title)
    TextView wordTableTitle;

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
    private ShowcaseView showcaseView;
    private int showCaseCount;

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        words = new ArrayList<>();
        order = new ArrayList<>();
        tiles = new ArrayList<>();
        added = new ArrayList<>();
        //getMedia = new GetMedia();
        currentWordLength = 0;
        currentSelectWordTileIndex = new ArrayList<>();
        height = getIntent().getIntExtra(LEVEL, 8);
        topic = getIntent().getStringExtra(TOPIC);

        crossWord = new CrossWord(height, height);

        tileAdapter = new TileAdapter(this, tiles);
        loadWord();
        genCrossWord();
        genTiles();
        initBoard();
        initWordsList();
        initInput();
        initSuggest();

        exitBuilder = new BlurPopupWindow.Builder(this)
            .setContentView(R.layout.popup_exit)
            .bindClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exitBuilder.dismiss();
                }
            }, R.id.no)
            .bindClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exit();
                }
            }, R.id.yes)
            .setGravity(Gravity.CENTER)
            .setScaleRatio(0.2f)
            .setBlurRadius(10)
            .setTintColor(0x30000000)
            .build();

        winBuilder = new BlurPopupWindow.Builder(this)
                .setContentView(R.layout.popup_win)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        words = new ArrayList<>();
                        order = new ArrayList<>();
                        tiles = new ArrayList<>();
                        added = new ArrayList<>();
                        horizontal = new ArrayList<>();
                        crossWord = new CrossWord(height, height);
                        currentWordLength = 0;
                        currentSelectWordTileIndex = new ArrayList<>();
                        loadWord();
                        genCrossWord();
                        genTiles();
                        initBoard();
                        initWordsList();
                        winBuilder.dismiss();
                    }
                }, R.id.again)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exit();
                    }
                }, R.id.exit)
                .setGravity(Gravity.CENTER)
                .setScaleRatio(0.2f)
                .setBlurRadius(10)
                .setTintColor(0x30000000)
                .build();

//        crossWordFragment = CrossWordFragment.newInstance(getIntent().getIntExtra(LEVEL, 8), getIntent().getStringExtra(TOPIC));
//        attachFragment(crossWordFragment);
        showCaseCount = 0;
        mute.setVisibility(View.INVISIBLE);
        about.setVisibility(View.INVISIBLE);
        settingOpen = false;
        storage = new Storage(this);
        settingLayout.setBackgroundResource(R.drawable.setting_back);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resID = getResources().getIdentifier("_click", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(CrossWordGameActivity.this, resID);
                if (settingOpen) {
                    settingLayout.setBackgroundResource(R.drawable.setting_back);
                    mute.setVisibility(View.INVISIBLE);
                    about.setVisibility(View.INVISIBLE);
                    settingOpen = false;
                    return;
                }
                settingLayout.setBackgroundResource(R.drawable.setting_back_full);
                mute.setVisibility(View.VISIBLE);
                about.setVisibility(View.VISIBLE);
                settingOpen =true;
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resID = getResources().getIdentifier("_click", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(CrossWordGameActivity.this, resID);
                if (storage.get(BG_SOUND).equals("ON")) {
                    mute.setImageResource(R.drawable.music_off);
                    SoundPlayer.getInstance().pauseBackgroundMedia();
                    storage.set(BG_SOUND, "OFF");
                    return;
                }

                mute.setImageResource(R.drawable.music_on);
                SoundPlayer.getInstance().playBackgroundMedia();
                storage.set(BG_SOUND, "ON");
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resID = getResources().getIdentifier("_click", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(CrossWordGameActivity.this, resID);
                buildShowCase(board, "This is the crossword board", "Click white tile to select an word");
            }
        });

        buildFirstShowCase(board, "This is the crossword board", "Click white tile to select an word");
    }

    private void buildFirstShowCase(View view, String contentTitle, String contentText) {
        Target target = new ViewTarget(view);
        showcaseView = new ShowcaseView.Builder(this)
                .setTarget(target)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .blockAllTouches()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextShowCase();
                    }
                })
                .setStyle(R.style.CustomShowcaseTheme)
                .singleShot(3)
                .build();
        showcaseView.setButtonText("Next");
    }

    private void buildShowCase(View view, String contentTitle, String contentText) {
        Target target = new ViewTarget(view);
        showcaseView = new ShowcaseView.Builder(this)
                .setTarget(target)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .blockAllTouches()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextShowCase();
                    }
                })
                .setStyle(R.style.CustomShowcaseTheme)
                .build();
        showcaseView.setButtonText("Next");
        showcaseView.setShouldCentreText(true);
    }

    void nextShowCase() {
        ArrayList<EnglishWord> current = horizontal;
        EnglishWord englishWord = new EnglishWord();

        englishWord.setWord("dog");
        int wordPicID = getResources().getIdentifier("_" + "dog_pic", "raw", getPackageName());

        int wordResID = getResources().getIdentifier("_" + "dog", "raw", getPackageName());


        switch (showCaseCount) {


            case 0:
                currentSelectWord = englishWord;
                wordInput.setItemCount(currentSelectWord.getWord().length());
                suggestLayout.setVisibility(View.VISIBLE);
                boardLayout.setVisibility(View.INVISIBLE);

                exitSuggest.setVisibility(View.VISIBLE);
                wordInput.getText().clear();
                currentInputWord = new StringBuilder();

                GlideApp
                        .with(CrossWordGameActivity.this)
                        .load(wordPicID)
                        .placeholder(R.drawable.ic_launcher_background)
                        .fitCenter()
                        .into(suggestImage);

                showcaseView.setShowcase(new ViewTarget(suggestImage), true);
                showcaseView.setContentTitle("This is picture describe the word");
                showcaseView.setContentText("");
                break;

            case 1:
                showcaseView.setShowcase(new ViewTarget(exitSuggest), true);
                showcaseView.setContentTitle("Touch this button return to board");
                showcaseView.setContentText("");
                break;

            case 2:
                showcaseView.setShowcase(new ViewTarget(wordInput), true);
                showcaseView.setContentTitle("This is where your typed chacracter placed");
                showcaseView.setContentText("");
                break;

            case 3:
                showcaseView.setShowcase(new ViewTarget(alphabet), true);
                showcaseView.setContentTitle("Use this keyboard to input word");
                showcaseView.setContentText("");
                break;

            case 4:
                showcaseView.setShowcase(new ViewTarget(reset), true);
                showcaseView.setContentTitle("Touch this to reset input");
                showcaseView.setContentText("");
                break;

            case 5:
                showcaseView.setShowcase(new ViewTarget(check), true);
                showcaseView.setContentTitle("Touch this to check if you have input right word");
                showcaseView.setContentText("");
                break;

            case 6:
                wordInput.setText("DAG");
                showError();
                showcaseView.setShowcase(new ViewTarget(wordInput), true);
                showcaseView.setContentTitle("If you input the wrong word, this will be red");

                showcaseView.setContentText("");
                break;

            case 7:
                wordInput.setText("DOG");
                showCorrect();
                showcaseView.setShowcase(new ViewTarget(wordInput), true);
                showcaseView.setContentTitle("If you input the right word, this will be green and the word will be read");

                showcaseView.setContentText("");
                break;

            case 8:
                wordInput.setBackgroundColor(Color.WHITE);
                suggestLayout.setVisibility(View.INVISIBLE);
                boardLayout.setVisibility(View.VISIBLE);
                exitSuggest.setVisibility(View.INVISIBLE);
                showcaseView.setShowcase(new ViewTarget(wordTableTitle), true);
                showcaseView.setContentTitle("This is all words you have answer");
                showcaseView.setContentText("Click the speaker icon to listen to the word again");
                break;

            case 9:
                horizontal.remove(horizontal.size() - 1);
                wordTableAdapter.notifyDataSetChanged();
                showcaseView.hide();
                showCaseCount = -1;
                showcaseView.setButtonText("Done");
                break;
        }
        showCaseCount++;
    }

    private void initWordsList() {
        horizontal = new ArrayList<>();
        wordTableAdapter = new WordTableAdapter(this, horizontal);
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
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height * (width/12),
                height * (width/12));
        int margin = Utils.convertDpToPixel(10, this);
        tileAdapter = new TileAdapter(this, tiles);
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
                    int resID = getResources().getIdentifier("_clicknot", "raw", getPackageName());
                    SoundPlayer.getInstance().playMedia(CrossWordGameActivity.this, resID);
                    return;
                }

                if (tiles.get(position).isShow()) {
                    int resID = getResources().getIdentifier("_clicknot", "raw", getPackageName());
                    SoundPlayer.getInstance().playMedia(CrossWordGameActivity.this, resID);
                    return;
                }

                int resID = getResources().getIdentifier("_click", "raw", getPackageName());
                SoundPlayer.getInstance().playMedia(CrossWordGameActivity.this, resID);
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
                int wordResID = getResources().getIdentifier("_" + currentSelectWord.getWord().replaceAll(" ", "").toLowerCase() + "_pic", "raw", getPackageName());
                GlideApp
                        .with(CrossWordGameActivity.this)
                        .load(wordResID)
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
        buildWinLayout();
    }

    private void showCorrect() {
        wordInput.setBackgroundColor(Color.GREEN);
        SoundPlayer.getInstance().pauseBackgroundMedia();
        int resID = getResources().getIdentifier("_" + currentSelectWord.getWord().replaceAll(" ", "").toLowerCase(), "raw", getPackageName());
        SoundPlayer.getInstance().playMedia(this, resID);
        horizontal.add(currentSelectWord);
        wordTableAdapter.notifyDataSetChanged();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wordInput.setBackgroundColor(Color.WHITE);
                showWord();
                exitSuggest();
                if (!storage.get(BG_SOUND).equals("OFF")) {
                    SoundPlayer.getInstance().playBackgroundMedia();
                }

            }
        }, 3000);
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
        int resID = getResources().getIdentifier("_wrong", "raw", getPackageName());
        SoundPlayer.getInstance().playMedia(this, resID);
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
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

//    private void attachFragment(Fragment crossWordFragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_holder, crossWordFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }

    @Override
    public void onBackPressed() {
        buildExitLayout();
    }

    public void exit() {
        if (exitBuilder.isShown()) {
            exitBuilder.dismiss();
        }

        if (winBuilder.isShown()) {
            winBuilder.dismiss();
        }
        finish();
    }

    public void buildExitLayout() {
        exitBuilder.show();
    }

    @Override
    public void buildWinLayout() {
        winBuilder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundPlayer.getInstance().pauseBackgroundMedia();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int resID = getResources().getIdentifier("_bgm3", "raw", getPackageName());
        SoundPlayer.getInstance().prepairBackgroundMedia(this, resID);
        if (!storage.get(BG_SOUND).equals("OFF")) {
            SoundPlayer.getInstance().playBackgroundMedia();
            storage.set(BG_SOUND, "ON");
            return;
        }

        mute.setImageResource(R.drawable.music_off);
    }

    public Button createButton(final Character letter) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.width = (width/120) * 8;
        params.height = (int) (params.width * 1.5);
        int margin = (width/120);
        params.setMargins(margin, margin, margin, margin);
        Button fancyButton = new Button(this);
        fancyButton.setLayoutParams(params);
        fancyButton.setText(letter.toString());
        fancyButton.setTextColor(Color.WHITE);
        fancyButton.setBackgroundResource(R.drawable.key_bg);
        fancyButton.setTextSize((int) Utils.pixelsToSp(this, (params.width / 10) * 8));
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
}
