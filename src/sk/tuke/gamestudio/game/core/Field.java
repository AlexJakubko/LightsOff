package sk.tuke.gamestudio.game.core;

import java.util.Random;

public class Field {
    private final int rowCount;

    private final int columnCount;

    private final int level;

    private final String playersName;

    private final Dot[][] dots;

    private int previousRow = -1;

    private  int previousColumn = -1;


    private GameState state = GameState.PLAYING;

    public Field(int rowCount, int columnCount, int level , String playersName) {

        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.level = level;
        this.playersName = playersName;
        dots = new Dot[rowCount][columnCount];
        generate();
    }

    private void generate() {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                final Dot dot = dots[row][column];
                if (dot == null)
                    dots[row][column] = new Dot();
            }
        }
        createLights();
    }

    private void createLights() {
        switch (level) {
            case 1:
                setLights(1);
                break;
            case 2:
                setLights(2);
                break;
            case 3:
                setLights(3);
                break;
            case 4:
                setLights(4);
                break;
            case 5:
                setLights(5);
                break;
            case 6:
                setLights(6);
                break;
            case 7:
                setLights(7);
                break;
            case 8:
                setLights(8);
                break;
            case 9:
                setLights(9);
                break;
            case 10:
                setLights(10);
                break;
            default:
                System.out.println("You are out of range !");
                break;
        }
    }

    private void setLights(int generateCount) {
        while(generateCount!=0){
            Random random = new Random();
            int row = random.nextInt(rowCount);
            int column = random.nextInt(columnCount);
            if (row == previousRow && column == previousColumn)
                continue;
            crossDotsChange(row,column);
            previousRow = row;
            previousColumn = column;
            generateCount--;
        }
    }
    public void shineDots(int row, int column) {
        if (state == GameState.PLAYING) {
            crossDotsChange(row, column);
            if (isSolved()) {
                state = GameState.SOLVED;
            }
        }
    }
    private void crossDotsChange(int row , int column) {
        changeLightState(dots[row][column]);
        if (row - 1 >= 0) {
            changeLightState(dots[row - 1][column]);
        }
        if (row + 1 < rowCount) {
            changeLightState(dots[row + 1][column]);
        }
        if (column - 1 >= 0) {
            changeLightState(dots[row][column - 1]);
        }
        if (column + 1 < columnCount)  {
            changeLightState(dots[row][column+1]);
        }
    }

    private void changeLightState(final Dot dot) {
        if (dot.getState() == DotState.SHINE) {
            dot.setState(DotState.DONTSHINE);
        } else {
            dot.setState(DotState.SHINE);
        }
    }

    private boolean isSolved() {
        int lightsCount = 0;
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                final Dot dot = dots[row][column];
                if (dot.getState() == DotState.SHINE) {
                    lightsCount++;
                }
            }
        }
        if (lightsCount == 0) {
            return true;
        }
        return false;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public GameState getState() {
        return state;
    }

    public String getPlayersName(){return playersName;}

    public Dot getDot(int row, int column) {
        return dots[row][column];
    }


}
