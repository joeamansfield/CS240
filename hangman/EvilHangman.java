package hangman;

import java.io.*;
import java.util.*;

public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException {
        File dictionary = new File(args[0]);
        int numChars = Integer.parseInt(args[1]);
        int numGuesses = Integer.parseInt(args[2]);
        Scanner console = new Scanner(System.in);
        EvilHangmanGame theGame = new EvilHangmanGame();
        theGame.startGame(dictionary, numChars);
        while (numGuesses > 0) {
            System.out.println("You have " + numGuesses + " guesses left");
            System.out.println("Used letters: " + theGame.getGuessedLetters().toString());
            System.out.println("Word: " + theGame.getRevealedWord());
            boolean validInput = false;
            char inputChar = '0';
            String currentString = theGame.getRevealedWord();
            while (!validInput) {
                System.out.print("Enter guess: ");
                String input = console.nextLine();
                if (input.length() == 1) {
                    inputChar = input.charAt(0);
                }
                if (Character.isLetter(inputChar)) {
                    validInput = true;
                    try {
                        theGame.makeGuess(inputChar);
                    }
                    catch (hangman.GuessAlreadyMadeException e) {
                        System.out.println("Letter already guessed");
                        validInput = false;
                        continue;
                    }
                    int difference = 0;
                    for (int i = 0; i < currentString.length(); i++) {
                        if (currentString.charAt(i) != theGame.getRevealedWord().charAt(i)) {
                            difference++;
                        }
                    }
                    if (difference == 0) {
                        System.out.println("Sorry, there are no " + inputChar + "'s");
                        numGuesses--;
                    }
                    else if (difference == 1){
                        System.out.println("Yes, there is 1 " + inputChar);
                    }
                    else {
                        System.out.println("Yes, there are " + difference + " "+ inputChar + "'s");
                    }
                }
                else {
                    System.out.println("Invalid guess, guesses must be a single letter");
                }
            }
            int charsFilled = 0;
            for (int i = 0; i < currentString.length(); i++) {
                if ('_' != theGame.getRevealedWord().charAt(i)) {
                    charsFilled++;
                }
            }
            if (charsFilled == currentString.length()) {
                System.out.println("You somehow won, congrats!\nThe word was " + theGame.getRevealedWord());
                return;
            }
            //numGuesses--;
            if (numGuesses == 0) {
                System.out.println("You lose!\nThe word was " + theGame.wordSet.iterator().next());
                return;
            }
            System.out.println("");
        }
    }
}
