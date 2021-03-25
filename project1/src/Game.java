import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private String title1;
    private String title2;
    private String end;
    private int totalDiceRolls;
    private int player;
    private int goal;
    private String state;
    private String prompt;
    private int[][] snakes;
    private int[][] ladders;
    private Random rand;
    private boolean win; //check if the player win
    private boolean over; //check if the player want to quit before winning

    public Game() {
        title1 = "----------------------------- Total Dice rolls before this:";
        title2 = " ----------------------------------";
        end = "---------------------------------------------------------------";
        totalDiceRolls = 0;
        player = 0;
        goal = 100;
        prompt = "Enter 1 to roll a die and 2 to exit!!";
        state = "At Home";
        rand = new Random();
        win = false;
        over = false;
        createSnakesAndLadders();
    }

    private int roll() {
        return rand.nextInt(6) + 1;
    }

    private void createSnakesAndLadders() {
        //create empty array for snake and ladder
        snakes = new int[3][2];
        ladders = new int[3][2];
        //Use HashSet so all 12 number will be unique
        HashSet<Integer> set = new HashSet<>();
        //get the start of 3 ladders
        while (set.size() < 12) {
            set.add(rand.nextInt(99) + 1);
        }
        int start = 0;
        int[] temp = new int[12];
        for (Integer i : set) {
            temp[start] = i;
            start += 1;
        }

        for (int i = 0; i <= 4; i += 2) {
            //insert for ladder
            if (temp[i] < temp[i + 1]) {
                ladders[i / 2] = new int[]{temp[i], temp[i + 1]};
            } else {
                ladders[i / 2] = new int[]{temp[i + 1], temp[i]};
            }
            //insert for snake
            if (temp[i + 6] > temp[i + 1 + 6]) {
                snakes[i / 2] = new int[]{temp[i + 6], temp[i + 1 + 6]};
            } else {
                snakes[i / 2] = new int[]{temp[i + 1 + 6], temp[i + 6]};
            }
        }
        Arrays.sort(ladders, Comparator.comparingDouble(o -> o[0]));
        Arrays.sort(snakes, Comparator.comparingDouble(o -> o[0]));
    }

    public boolean check() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        String decision = scanner.nextLine();
        if (decision.equals("1")) {
            return true;
        } else if (decision.equals("2")) {
            return false;
        } else {
            return check();
        }
    }

    public void play() {
        //ouput basic information
        System.out.println(title1 + totalDiceRolls + title2);

        //print ladders and snakes
        System.out.println("Ladders: " + Arrays.deepToString(ladders));
        System.out.println("Sankes: " + Arrays.deepToString(snakes));

        //print player info
        System.out.println("CurrentPosition: " + player);
        System.out.println("CurrentState: " + state);

        //check function will check if the player want to exit or not
        if (check()) {
            System.out.println("Entered 1, rolling a die");
            int dice = roll();
            System.out.println("Dice value: " + dice);
            System.out.println();
            //check if the play can move
            if (state.equals("At Home")) {
                if (dice == 6) {
                    state = "Roam Free";
                }
            } else {
                if (player + dice == 100) {
                    win = true;
                    player += dice;
                } else if (player + dice < 100) {
                    player += dice;
                    for (int[] l : ladders) {
                        if (player == l[0]) {
                            player = l[1];
                            break;
                        }
                    }
                    for (int[] s : snakes) {
                        if (player == s[0]) {
                            player = s[1];
                            break;
                        }
                    }
                }
            }
            System.out.println("After the dice is rolled:");
        } else {
            over = true;
            return;
        }
        //end for 1 section
        System.out.println("CurrentPosition: " + player);
        System.out.println("CurrentState: " + state);
    }

    private void endGame() {
        if (win) {
            System.out.println("Hurray you won!! Bye bye :)");
        } else {
            System.out.println("You didn't win, hope to see you soon :)");
        }
    }

    public void start() {
        while (true) {
            play();
            if (over || win) {
                break;
            }
            System.out.println(end);
            System.out.println();
        }
        System.out.println();
        endGame();
        System.out.println(end);
    }

}
