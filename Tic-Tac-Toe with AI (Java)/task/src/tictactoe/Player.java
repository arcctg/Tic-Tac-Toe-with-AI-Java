package tictactoe;

import java.util.*;

public enum Player {
    EASY {
        @Override
        public void play() {
            Player.easy();
        }
    },
    MEDIUM {
        @Override
        public void play() {
            Player.medium();
        }
    },
    HARD {
        @Override
        public void play() {
            Player.hard();
        }
    },
    USER {
        @Override
        public void play() {
            Player.user();
        }
    };

    private static Character[][] arr = new Character[3][3];
    private static final Scanner scanner = new Scanner(System.in);

    public void play() {}
    private static boolean con() {
        String result = gameResults();

        boolean b = !result.contains("not") && !result.contains("_");

        if (b) {
            System.out.println(result);
        }

        return !b;
    }

    private static void user() {
        int row;
        int column;

        while (true) {
            System.out.print("Enter the coordinates: ");

            try {
                row = Integer.parseInt(scanner.next()) - 1;
                column = Integer.parseInt(scanner.next()) - 1;

                if (row < 0 || row > 2 || column < 0 || column > 2) {
                    throw new RuntimeException("Coordinates should be from 1 to 3!");
                } else if (arr[row][column].equals('X') || arr[row][column].equals('O')) {
                    throw new RuntimeException("This cell is occupied! Choose another one!");
                }
            } catch(NumberFormatException | NullPointerException e) {
                System.out.println("You should enter numbers!");
                continue;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                continue;
            }

            break;
        }

        arr[row][column] = getCurrentChar();
        print();
    }

    public static void process(String firstPlayer, String secondPlayer) {
        fill();
        print();

        while (con()) {
            if (count('_') % 2 != 0) {
                Player.valueOf(firstPlayer.toUpperCase()).play();
            } else {
                Player.valueOf(secondPlayer.toUpperCase()).play();
            }
        }
    }

    private static void easy(){
        System.out.println("Making move level \"easy\"");

        Coords coords = getFree();

        arr[coords.getRow()][coords.getColumn()] = getCurrentChar();
        print();
    }

    private static void medium(){
        System.out.println("Making move level \"medium\"");

        Coords coords = getMedium();

        arr[coords.getRow()][coords.getColumn()] = getCurrentChar();
        print();
    }

    private static void hard(){
        System.out.println("Making move level \"hard\"");

        Coords coords = getHard(arr,true, null);

        setChar(arr, coords);
        print();
    }

    private static char getCurrentChar() {
        return count('_') % 2 != 0 ? 'X' : 'O';
    }

    private static char getCurrentChar(Character[][] arr) {
        return count('_', arr) % 2 != 0 ? 'X' : 'O';
    }

    private static Coords getMedium() {
        Coords coords = null;
        boolean b = count('_') % 2 != 0;

        for (int k = 0; k < 2; k++) {
            if (arr[0][0] + arr[1][1] + arr[2][2] == (b ? 253 : 271)) {
                if (arr[0][0] == '_') coords = new Coords(0, 0);
                else if (arr[1][1] == '_') coords = new Coords(1, 1);
                else coords = new Coords(2, 2);
            } else if (arr[0][2] + arr[1][1] + arr[2][0] == (b ? 253 : 271)) {
                if (arr[0][2] == '_') coords = new Coords(0, 2);
                else if (arr[1][1] == '_') coords = new Coords(1, 1);
                else coords = new Coords(2, 0);
            } else {
                for (int i = 0; i < 3; i++) {
                    if (arr[i][0] + arr[i][1] + arr[i][2] == (b ? 253 : 271)) {
                        for (int j = 0; j < 3; j++) {
                            if (arr[i][j] == '_') {
                                coords = new Coords(i, j);
                                break;
                            }
                        }
                        break;
                    } else if (arr[0][i] + arr[1][i] + arr[2][i] == (b ? 253 : 271)) {
                        for (int j = 0; j < 3; j++) {
                            if (arr[j][i] == '_') {
                                coords = new Coords(j, i);
                                break;
                            }
                        }
                        break;
                    }
                }
            }

            b = !b;
        }

        if (coords == null) {
            coords = getFree();
        }

        return coords;
    }

    private static Coords getHard(Character[][] arr, int mSc, int ii) {
        //System.out.println("new");
        Coords coords = null;
        //boolean b = count('_') % 2 != 0;
        //Character[][] chars = copy(arr);

        int mScore = mSc;

        int i = ii;
        for (Coords cds : getFree(arr)) {
            Character[][] arrCh = copy(arr);
            int score = 0;

            setChar(arrCh, cds);
            int res = gameResults(arrCh);
            score += res - ++ii;
            //print(arrCh);
            //System.out.println(score);

            if (res == 0) {
                Coords cr = getHard(arrCh, mScore, ii);
                score += cr.getMaxScore();
                setChar(arrCh, cr);
            }

            if (score > mScore) {
                mScore = score;
                coords = cds.setMaxScore(mScore);
            }
        }


        if (coords == null) {
            coords = getFree();
        }

        return coords;
    }

    private static Coords getHard(Character[][] arr, boolean maxPl, Coords coordsw) {
        Coords coords = null;
        int mScore;

        int results = gameResults(arr, !maxPl);

        if (results != 0) {
            //System.out.println("res != 0");
            return coordsw.setMaxScore(results);
        }

        if (maxPl) {
            mScore = Integer.MIN_VALUE;

            //System.out.println("maxPl");

            for (Coords cds : getFree(arr)) {
                Character[][] arrCh = copy(arr);

                setChar(arrCh, cds);
                //print(arrCh);
                int score = getHard(arrCh, false, cds).getMaxScore();

                //mScore = Math.max(mScore, score);

                if (score > mScore) {
                    //System.out.println(score + " > mScore");
                    mScore = score;
                    coords = cds.setMaxScore(mScore);
                }
            }
        } else {
            mScore = Integer.MAX_VALUE;

            //System.out.println("not maxPl");

            for (Coords cds : getFree(arr)) {
                Character[][] arrCh = copy(arr);

                setChar(arrCh, cds);
                //print(arrCh);
                int score = getHard(arrCh, true, cds).getMaxScore();


                //mScore = Math.min(mScore, score);

                if (score < mScore) {
                    //System.out.println(score + " < mScore");
                    mScore = score;
                    coords = cds.setMaxScore(mScore);
                }
            }
        }

        return coords;



//        int mScore = mSc;
//
//        int i = ii;
//        for (Coords cds : getFree(arr)) {
//            Character[][] arrCh = copy(arr);
//            int score = 0;
//
//            setChar(arrCh, cds);
//            int res = gameResults(arrCh);
//            score += res - ++ii;
//            //print(arrCh);
//            //System.out.println(score);
//
//            if (res == 0) {
//                Coords cr = getHard(arrCh, mScore, ii);
//                score += cr.getMaxScore();
//                setChar(arrCh, cr);
//            }
//
//            if (score > mScore) {
//                mScore = score;
//                coords = cds.setMaxScore(mScore);
//            }
//        }

    }

    private static void setChar(Character[][] arr, Coords coords) {
        arr[coords.getRow()][coords.getColumn()] = getCurrentChar(arr);
    }

    private static Character[][] copy(Character[][] src) {
        if (src == null) {
            return null;
        }

        return Arrays.stream(src).map(Character[]::clone).toArray(Character[][]::new);
    }

    private static void fill() {
        Arrays.stream(arr).forEach(c -> Arrays.fill(c, '_'));
    }

    private static void fillTest() {
        arr = new Character[][]{
                {'X', 'O', 'O'},
                {'_', 'O', '_'},
                {'_', 'X', 'X'}
        };
    }

    private static Coords getFree() {
        List<Coords> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] == '_') {
                    list.add(new Coords(i, j));
                }
            }
        }

        return list.get(new Random().nextInt(0, list.size()));
    }

    private static List<Coords> getFree(Character[][] arr) {
        List<Coords> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] == '_') {
                    list.add(new Coords(i, j));
                }
            }
        }

        return list;
    }

    private static long count(char ch) {
        return Arrays.stream(arr).flatMap(Arrays::stream).filter(c -> c.equals(ch)).count();
    }

    private static long count(char ch, Character[][] arr) {
        return Arrays.stream(arr).flatMap(Arrays::stream).filter(c -> c.equals(ch)).count();
    }

    private static String gameResults() {
        String result = "Game not finished";

        if (arr[0][0] == arr[1][1] && arr[1][1] == arr[2][2] ||
                arr[0][2] == arr[1][1] && arr[1][1] == arr[2][0]) {
            result = arr[1][1] + " wins";
        } else {
            for (int i = 0; i < 3; i++) {
                if (arr[i][0] == arr[i][1] && arr[i][1] == arr[i][2]) {
                    result = arr[i][1] + " wins";
                    break;
                } else if (arr[0][i] == arr[1][i] && arr[1][i] == arr[2][i]) {
                    result = arr[1][i] + " wins";
                    break;
                }
            }

            if (count('_') == 0 && !result.contains("wins")) {
                result = "Draw";
            }
        }

        return result + "\n";
    }

    private static int gameResults(Character[][] arr) {
        String result = "Game not finished";
        int res = 0;

        char ch = getCurrentChar();

        if ((arr[0][0] == arr[1][1] && arr[1][1] == arr[2][2] ||
                arr[0][2] == arr[1][1] && arr[1][1] == arr[2][0]) && arr[1][1] != '_') {
            res = ch == arr[1][1] ? 100 : -100;
        } else {
            for (int i = 0; i < 3; i++) {
                if (arr[i][0] == arr[i][1] && arr[i][1] == arr[i][2] && arr[i][1] != '_') {
                    res = ch == arr[i][1] ? 100 : -100;
                    break;
                } else if (arr[0][i] == arr[1][i] && arr[1][i] == arr[2][i] && arr[1][i] != '_') {
                    res = ch == arr[1][i] ? 100 : -100;
                    break;
                }
            }
        }

        if (count('_', arr) == 0 && Math.abs(res) != 100) {
            res = 50;
        }

        return res;
    }

    private static int gameResults(Character[][] arr, boolean gg) {
        String result = "Game not finished";
        int res = 0;

        if ((arr[0][0] == arr[1][1] && arr[1][1] == arr[2][2] ||
                arr[0][2] == arr[1][1] && arr[1][1] == arr[2][0]) && arr[1][1] != '_') {
            res = gg ? 100 : -100;
        } else {
            for (int i = 0; i < 3; i++) {
                if (arr[i][0] == arr[i][1] && arr[i][1] == arr[i][2] && arr[i][1] != '_') {
                    res = gg ? 100 : -100;
                    break;
                } else if (arr[0][i] == arr[1][i] && arr[1][i] == arr[2][i] && arr[1][i] != '_') {
                    res = gg ? 100 : -100;
                    break;
                }
            }
        }

        if (count('_', arr) == 0 && Math.abs(res) != 100) {
            res = 50;
        }

        return res;
    }

    private static void print() {
        System.out.println("---------");
        for (Character[] characters : arr) {
            for (int j = 0; j < characters.length; j++) {
                String mes = characters[j] + " ";
                if (j == 0) mes = "| " + mes;
                else if (j == 2) mes += "|";
                System.out.print(mes);
            }
            System.out.println();
        }
        System.out.println("---------");
    }

    private static void print(Character[][] arr) {
        System.out.println("---------");
        for (Character[] characters : arr) {
            for (int j = 0; j < characters.length; j++) {
                String mes = characters[j] + " ";
                if (j == 0) mes = "| " + mes;
                else if (j == 2) mes += "|";
                System.out.print(mes);
            }
            System.out.println();
        }
        System.out.println("---------");
    }
}
