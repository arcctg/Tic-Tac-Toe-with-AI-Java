package tictactoe;

public class Coords {
    private int row;
    private int column;
    private int maxScore;

    public Coords(int row, int column) {
        this.row = row;
        this.column = column;
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
