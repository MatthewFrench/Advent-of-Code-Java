package Advent.of.Code.Java.Year_2020;

import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.Day;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day_22 implements Day {
    public void run() throws Exception {
        {
            final String input = LoadUtilities.loadTextFileAsString("input/2020/22/input.txt");
            var rawPlayer1 = StringUtilities.splitStringIntoList(input, "\n\n").get(0);
            var rawPlayer2 = StringUtilities.splitStringIntoList(input, "\n\n").get(1);
            List<String> player1Cards = new ArrayList<>(StringUtilities.splitStringIntoList(StringUtilities.removeStartChunk(rawPlayer1, "Player 1:\n"), "\n"));
            List<String> player2Cards = new ArrayList<>(StringUtilities.splitStringIntoList(StringUtilities.removeStartChunk(rawPlayer2, "Player 1:\n"), "\n"));


            long round = 0;
            while (player1Cards.size() > 0 && player2Cards.size() > 0) {
                round += 1;
                var card1 = player1Cards.remove(0);
                var card2 = player2Cards.remove(0);
                int card1Number = Integer.parseInt(card1);
                int card2Number = Integer.parseInt(card2);
                if (card1Number > card2Number) {
                    player1Cards.add(card1);
                    player1Cards.add(card2);
                } else if (card2Number > card1Number) {
                    player2Cards.add(card2);
                    player2Cards.add(card1);
                } else {
                    throw new Exception("Unhandled!");
                }
            }
            var cardsToCount = player1Cards.size() > 0 ? player1Cards : player2Cards;
            long total = 0;
            for (var i = 0; i < cardsToCount.size(); i++) {
                var card= cardsToCount.get(i);
                long cardNumber = Long.parseLong(card);
                total += cardNumber * (cardsToCount.size() - i);
            }
            LogUtilities.log("Part 1: " + total);
        }
        {
            final String input = LoadUtilities.loadTextFileAsString("input/2020/22/input.txt");
            var rawPlayer1 = StringUtilities.splitStringIntoList(input, "\n\n").get(0);
            var rawPlayer2 = StringUtilities.splitStringIntoList(input, "\n\n").get(1);
            List<String> player1Cards = new ArrayList<>(StringUtilities.splitStringIntoList(StringUtilities.removeStartChunk(rawPlayer1, "Player 1:\n"), "\n"));
            List<String> player2Cards = new ArrayList<>(StringUtilities.splitStringIntoList(StringUtilities.removeStartChunk(rawPlayer2, "Player 1:\n"), "\n"));
            ArrayList<Integer> player1CardNumbers = player1Cards.stream().map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Integer> player2CardNumbers = player2Cards.stream().map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));

            playGame(player1CardNumbers, player2CardNumbers);

            var cardsToCount = player1CardNumbers.size() > 0 ? player1CardNumbers : player2CardNumbers;
            long total = 0;
            for (var i = 0; i < cardsToCount.size(); i++) {
                long cardNumber = cardsToCount.get(i);
                total += cardNumber * (cardsToCount.size() - i);
            }
            LogUtilities.log("Part 2: " + total);
        }
    }

    static boolean playGame(ArrayList<Integer> player1CardNumbers, ArrayList<Integer> player2CardNumbers) throws Exception {
        // Track rounds in this game
        //Set<Pair<List<Integer>, List<Integer>>> previousRounds = new HashSet<>();
        var player1Won = false;
        Set<Pair<ArrayList<Integer>, ArrayList<Integer>>> previousRounds = new HashSet<>();
        while (player1CardNumbers.size() > 0 && player2CardNumbers.size() > 0) {
            // If there is a previous round in this game with the same order of player decks, player 1 wins
            if (previousRounds.contains(Pair.with(player1CardNumbers, player2CardNumbers))) {
                // Player 1 wins
                player1Won = true;
                break;
            }
            previousRounds.add(Pair.with(new ArrayList<>(player1CardNumbers), new ArrayList<>(player2CardNumbers)));

            int card1Number = player1CardNumbers.remove(0);
            int card2Number = player2CardNumbers.remove(0);

            // Determine if the players have as many cards remaining in their deck as the value they just drew
            if (card1Number <= player1CardNumbers.size() && card2Number <= player2CardNumbers.size()) {
                // New game of recursive combat
                ArrayList<Integer> newPlayer1CardNumbers = new ArrayList<>();
                for (var i = 0; i < card1Number; i++) {
                    newPlayer1CardNumbers.add(player1CardNumbers.get(i));
                }
                ArrayList<Integer> newPlayer2CardNumbers = new ArrayList<>();
                for (var i = 0; i < card2Number; i++) {
                    newPlayer2CardNumbers.add(player2CardNumbers.get(i));
                }
                var winner = playGame(newPlayer1CardNumbers, newPlayer2CardNumbers);
                if (winner) {
                    player1CardNumbers.add(card1Number);
                    player1CardNumbers.add(card2Number);
                } else {
                    player2CardNumbers.add(card2Number);
                    player2CardNumbers.add(card1Number);
                }
            } else {
                if (card1Number > card2Number) {
                    player1CardNumbers.add(card1Number);
                    player1CardNumbers.add(card2Number);
                } else if (card2Number > card1Number) {
                    player2CardNumbers.add(card2Number);
                    player2CardNumbers.add(card1Number);
                } else {
                    throw new Exception("Unhandled!");
                }
            }
            if (player2CardNumbers.size() == 0) {
                player1Won = true;
            } else if (player1CardNumbers.size() == 0) {
                player1Won = false;
            }
        }
        return player1Won;
    }
}