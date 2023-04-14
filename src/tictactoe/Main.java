package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // write your code here
        var scanner = new Scanner(System.in);
        System.out.print("Enter the cells: ");
        var game = new Game(scanner.nextLine());
        game.start();
    }
}
