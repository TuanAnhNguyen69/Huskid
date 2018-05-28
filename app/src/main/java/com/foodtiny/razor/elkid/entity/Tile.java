package com.foodtiny.razor.elkid.entity;

/**
 * Created by razor on 26/03/2018.
 */

public class Tile {
    private Character character;
    private int horizontalNumber;
    private int verticalNumber;
    private boolean isShow;
    private boolean isSelected;

    public Tile() {
        horizontalNumber = 0;
        character = ' ';
        isShow = false;
        isSelected = false;
    }

    public Tile(Character character, int number) {
        this.character = character;
        this.horizontalNumber = number;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public int getHorizontalNumber() {
        return horizontalNumber;
    }

    public void setHorizontalNumber(int horizontalNumber) {
        this.horizontalNumber = horizontalNumber;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public int getVerticalNumber() {
        return verticalNumber;
    }

    public void setVerticalNumber(int verticalNumber) {
        this.verticalNumber = verticalNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
