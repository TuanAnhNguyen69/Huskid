package com.foodtiny.razor.elkid.entity;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Razor on 3/19/2018.
 */

public class CrossWord
{
    final String letters = "abcdefghijklmnopqrstuvwxyz";
    int[] directionX = { 0, 1 };
    int[] directionY = { 1, 0 };
    char [][] board;
    int [][] horizontalWords;
    int [][] verticalWords;
    int width;
    int height;
    int horizontalCount, verticalCount;
    static Random random;
    private static ArrayList<String> wordsToInsert;
    private static char[][] tempBoard;
    private static int _bestSol;
    public boolean inRTL;

    public CrossWord(int xDimen, int yDimen) {
        board = new char[xDimen][yDimen];
        horizontalWords = new int[xDimen][yDimen];
        verticalWords = new int[xDimen][yDimen];
        width = xDimen;
        height = yDimen;
        random = new Random();

        initBoard();

    }

    private void initBoard() {
        for (int rowIndex = 0; rowIndex < width; rowIndex++) {
            for (int colIndex = 0; colIndex < height; colIndex++) {
                board[rowIndex][colIndex] = ' ';
            }
        }
    }

    public String toString() {
     String result = "";
        for (int rowIndex = 0; rowIndex < width; rowIndex++) {
            for (int colIndex = 0; colIndex < height; colIndex++)
            {
                result += letters.contains(Character.toString(board[rowIndex][colIndex])) ? board[rowIndex][colIndex] : ' ';
            }
        }
        return result;
    }

    boolean isValidPosition(int x , int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    int canBePlaced(String word, int x, int y, int dir) {
        int result = 0;
        if (dir == 0) {
            for (int j = 0; j < word.length(); j++) {
                int x1 = x, y1 = y + j;
                if (!(isValidPosition(x1, y1) && (board[x1][y1] == ' ' || board[x1][y1] == word.charAt(j))))
                return -1;
                if (isValidPosition(x1 - 1, y1))
                    if (horizontalWords[x1 - 1][y1] > 0)
                return -1;
                if (isValidPosition(x1 + 1, y1))
                    if (horizontalWords[x1 + 1][y1] > 0)
                return -1;
                if (board[x1][y1] == word.charAt(j))
                result++;
            }

        } else {
            for (int j = 0; j < word.length(); j++) {
                int x1 = x + j, y1 = y;
                if (!(isValidPosition(x1, y1) && (board[x1][y1] == ' ' || board[x1][y1] == word.charAt(j))))
                return -1;
                if (isValidPosition(x1, y1 - 1))
                    if (verticalWords[x1][y1 - 1] > 0)
                return -1;
                if (isValidPosition(x1, y1 + 1))
                    if (verticalWords[x1][y1 + 1] > 0)
                return -1;
                if (board[x1][y1] == word.charAt(j))
                result++;
            }
        }

        int xStar = x - directionX[dir], yStar = y - directionY[dir];
        if (isValidPosition(xStar, yStar)) {
            if (!(board[xStar][yStar] == ' ' || board[xStar][yStar] == '*')) {
                return -1;
            }
        }


        xStar = x + directionX[dir]*word.length();
        yStar = y + directionY[dir]*word.length();
        if (isValidPosition(xStar, yStar)) {
            if (!(board[xStar][yStar] == ' ' || board[xStar][yStar] == '*')) {
                return -1;
            }
        }
        return result == word.length() ? -1 : result;

    }

    void putWord(String word , int x , int y , int dir, int value) {
        int[][] mat = dir==0 ? horizontalWords : verticalWords;

        for (int i = 0; i < word.length(); i++) {
            int x1 = x + directionX[dir]*i, y1 = y + directionY[dir]*i;
            board[x1][y1] = word.charAt(i);
            mat[x1][y1] = value;
        }

        int xStar = x - directionX[dir], yStar = y - directionY[dir];
        if (isValidPosition(xStar, yStar)) board[xStar][yStar] = '*';
        xStar = x + directionX[dir]*word.length();
        yStar = y + directionY[dir]*word.length();
        if (isValidPosition(xStar, yStar)) board[xStar][yStar] = '*';
    }

    public int addWord(String word) {

        //var max = int.MaxValue;
        String wordToInsert = word;
        Triplet<Integer, Integer, Integer> info = bestPosition(wordToInsert);
        if (info!=null) {
            if (info.getThird() == 0)
            {
                horizontalCount++;
                if (inRTL)
                    wordToInsert = new StringBuilder(word).reverse().toString();
            }
            else
                verticalCount++;
            Integer value = info.getThird() == 0 ? horizontalCount : verticalCount;
            putWord(wordToInsert , info.getFirst() , info.getSecond() , info.getThird() , value);
            return info.getThird();
        }

        return -1;

    }

    ArrayList<Triplet<Integer, Integer, Integer>> FindPositions(String word)
    {
        int max = 0;
        ArrayList<Triplet<Integer, Integer, Integer>> positions = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++)
            {
                for (int i = 0; i < directionX.length; i++)
                {
                    int dir = i;
                    String wordToInsert = i == 0 && inRTL ? new StringBuilder(word).reverse().toString() : word;
                    int count = canBePlaced(wordToInsert, x, y, dir);

                    if (count < max) continue;
                    if (count > max)
                        positions.clear();

                    max = count;
                    positions.add(new Triplet<Integer, Integer, Integer>(x, y, dir));
                }
            }
        }
        return positions;
    }

    Triplet<Integer, Integer, Integer> bestPosition(String word)
    {
        ArrayList<Triplet<Integer, Integer, Integer>> positions = FindPositions(word);
        if (positions.size() > 0)
        {
            int index = random.nextInt(positions.size());
            return positions.get(index);
        }
        return null;
    }

    public boolean isLetter(char a)
    {
        return letters.contains(Character.toString(a));
    }

    public void reset()
    {
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                board[i][j] = ' ';
                verticalWords[i][j] = 0;
                horizontalWords[i][j] = 0;
                horizontalCount = verticalCount = 0;
            }
        }
    }

    public void addWords(ArrayList<String> words)
    {
        wordsToInsert = words;
        _bestSol = width * height;
        gen(0);
        board = tempBoard;
    }

    int freeSpaces()
    {
        int count = 0;
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (board[i][j] == ' ' || board[i][j] == '*')
                count++;
            }
        }
        return count;
    }

    void gen(int pos)
    {

        if (pos >= wordsToInsert.size() )
            return;

        for (int i = pos; i < wordsToInsert.size(); i++)
        {

            Triplet<Integer, Integer, Integer> posi = bestPosition(wordsToInsert.get(i));
            if (posi!=null)
            {
                String word = wordsToInsert.get(i);
                if (posi.getThird() == 0 && inRTL)
                    word = new StringBuilder(word).reverse().toString();
                Integer value = posi.getThird() == 0 ? horizontalCount : verticalCount;
                putWord(word,posi.getFirst() ,posi.getSecond(), posi.getThird(), value);
                gen(pos + 1);
                removeWord(word, posi.getFirst(), posi.getSecond(), posi.getThird());
            }
            else
            {
                gen(pos + 1);
            }
        }

        int c = freeSpaces();
        if (c >= _bestSol) return;
        _bestSol = c;
        tempBoard = board.clone();
    }

    private void removeWord(String word, int x, int y, int dir)
    {
        int[][] mat = dir == 0 ? horizontalWords : verticalWords;
        int[][] mat1 = dir == 0 ? verticalWords : horizontalWords;

        for (int i = 0; i < word.length(); i++)
        {
            int x1 = x + directionX[dir] * i, y1 = y + directionY[dir] * i;
            if (mat1[x1][y1] == 0)
            board[x1][y1] = ' ';
            mat[x1][y1] = 0;
        }

        int xStar = x - directionX[dir], yStar = y - directionY[dir];
        if (isValidPosition(xStar, yStar) && hasFactibleValueAround(xStar, yStar))
            board[xStar][yStar] = ' ';

        xStar = x + directionX[dir] * word.length();
        yStar = y + directionY[dir] * word.length();
        if (isValidPosition(xStar, yStar) && hasFactibleValueAround(xStar, yStar))
            board[xStar][yStar] = ' ';
    }

    boolean hasFactibleValueAround(int x , int y)
    {
        for (int i = 0; i < directionX.length; i++)
        {
            int x1 = x + directionX[i] , y1 = y + directionY[i] ;
            if (isValidPosition(x1, y1) && (board[x1][y1] != ' ' || board[x1][y1] == '*'))
            return true;
            x1 = x - directionX[i];
            y1 = y - directionY[i];
            if (isValidPosition(x1, y1) && (board[x1][y1] != ' ' || board[x1][y1] == '*'))
            return true;

        }
        return false;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public int[][] getHorizontalWords() {
        return horizontalWords;
    }

    public void setHorizontalWords(int[][] horizontalWords) {
        this.horizontalWords = horizontalWords;
    }

    public int[][] getVerticalWords() {
        return verticalWords;
    }

    public void setVerticalWords(int[][] verticalWords) {
        this.verticalWords = verticalWords;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHorizontalCount() {
        return horizontalCount;
    }

    public void setHorizontalCount(int horizontalCount) {
        this.horizontalCount = horizontalCount;
    }

    public int getVerticalCount() {
        return verticalCount;
    }

    public void setVerticalCount(int verticalCount) {
        this.verticalCount = verticalCount;
    }

    class Triplet<T, U, V> {
        private final T first;
        private final U second;
        private final V third;

        public Triplet(T first, U second, V third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public T getFirst() { return first; }
        public U getSecond() { return second; }
        public V getThird() { return third; }
    }

}
