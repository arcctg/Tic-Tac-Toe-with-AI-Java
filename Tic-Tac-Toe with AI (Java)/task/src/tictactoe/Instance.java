package tictactoe;

import java.util.*;

public class Instance {
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        boolean go = true;

        do {
            String[] param = inpParam();

            switch (param[0]) {
                case "start" -> Player.process(param[1], param[2]);
                case "exit" -> go = false;
            }
        } while (go);
    }

    private String[] inpParam() {
        System.out.print("Input command: ");
        String comm = scanner.nextLine().toLowerCase().trim();

        while (!comm.matches("^start\\s+(easy|user|medium|hard)\\s+(easy|user|medium|hard)")
        && !"exit".equals(comm)) {
            System.out.print("Bad parameters!\nInput command: ");
            comm = scanner.nextLine().toLowerCase().trim();
        }

        return comm.split(" ");
    }
}
