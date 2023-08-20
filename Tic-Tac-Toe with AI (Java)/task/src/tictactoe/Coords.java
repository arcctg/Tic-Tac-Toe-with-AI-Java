package tictactoe;

public class Coords {
    private int row;
    private int column;
    private int maxScore;

    public Coords(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Coords(int row, int column, int maxScore) {
        this.row = row;
        this.column = column;
        this.maxScore = maxScore;
    }

    public Coords(int maxScore) {
        this.maxScore = maxScore;
    }

    public Coords() {

    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public Coords setMaxScore(int maxScore) {
        this.maxScore = maxScore;
        return this;
    }
}
