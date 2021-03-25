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
    private int player; //player's position
    private String state;
    private String prompt;
    private int[][] snakes; //store snakes value
    private int[][] ladders; //store ladders value
    private Random rand;
    private boolean win; //check if the player win
    private boolean over; //check if the player want to quit before winning

    public Game() {
        title1 = "----------------------------- Total Dice rolls before this:";
        title2 = " ----------------------------------";
        end = "---------------------------------------------------------------";
        totalDiceRolls = 0;
        player = 0;
        prompt = "Enter 1 to roll a die and 2 to exit!!";
        state = "At Home";
        rand = new Random();
        win = false;
        over = false;
        createSnakesAndLadders();
    }

    private int roll() {
        //roll the dice
        return rand.nextInt(6) + 1;
    }

    private void createSnakesAndLadders() {
        //create empty array for snake and ladder
        snakes = new int[3][2];
        ladders = new int[3][2];
        //Use HashSet so all 12 number will be unique
        HashSet<Integer> set = new HashSet<>();

        while (set.size() < 12) {
            set.add(rand.nextInt(99) + 1);
        }

        //put the numbers in array, so it divide to two part easily
        int start = 0;
        int[] temp = new int[12];
        for (Integer i : set) {
            temp[start] = i;
            start += 1;
        }

        //create the ladder and snake 2d array from the temp array
        //first half would be in ladder, second half would be in snake
        for (int i = 0; i <= 4; i += 2) {
            //i will be 0, 2, 4
            //when insert into ladder or snake i need to be divide by 2
            if (temp[i] < temp[i + 1]) {
                ladders[i / 2] = new int[]{temp[i], temp[i + 1]};
            } else {
                ladders[i / 2] = new int[]{temp[i + 1], temp[i]};
            }
            //since i will be 0, 2, 4, index for snake should be 6, 8, 10
            if (temp[i + 6] > temp[i + 1 + 6]) {
                snakes[i / 2] = new int[]{temp[i + 6], temp[i + 1 + 6]};
            } else {
                snakes[i / 2] = new int[]{temp[i + 1 + 6], temp[i + 6]};
            }
        }
        //sort the 2d array by a[0]
        Arrays.sort(ladders, Comparator.comparingDouble(o -> o[0]));
        Arrays.sort(snakes, Comparator.comparingDouble(o -> o[0]));
    }

    public boolean check() {
        //check if the user enter 1 or 2
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        String decision = scanner.nextLine();
        if (decision.equals("1")) {
            return true;
        } else if (decision.equals("2")) {
            return false;
        } else {
            //if the user enter neither 1 or 2
            //the application will keep asking
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
            //check if the player's state is At Home or Roam Free
            if (state.equals("At Home")) {
                if (dice == 6) {
                    state = "Roam Free";
                }
            } else {
                //check if the player win
                //if the player + dice > 100, nothing will happen
                if (player + dice == 100) {
                    win = true;
                    player += dice;
                } else if (player + dice < 100) {
                    player += dice;
                    //since all number in snake and ladder are unique
                    //it doesn't matter if we scan ladder first or snake first
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
            //if the player enter 2 to exit
            over = true;
            return; //exit play(), won't print the info after dice is rolled
        }
        //info after the dice is rolled
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
        while (true) { // the application will run until the player win or exit
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
