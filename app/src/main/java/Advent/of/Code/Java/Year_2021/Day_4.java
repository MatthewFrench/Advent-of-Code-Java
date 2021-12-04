package Advent.of.Code.Java.Year_2021;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;

import java.util.ArrayList;
import java.util.List;

public class Day_4 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2021/4/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> input = LoadUtilities.loadTextFileAsList(fileName);

        final List<String> calledNumbers = StringUtilities.splitStringIntoList(input.get(0), ",");

        final List<Board> boards = new ArrayList<>();
        for (int i = 2; i < input.size(); i+= 6) {
            final Board board = new Board();
            for (int y = 0; y < 5; y++) {
                final List<String> row = new ArrayList<>(StringUtilities.splitStringIntoList(input.get(i+y), " "));
                row.removeIf(String::isEmpty);
                for (int x = 0; x < 5; x++) {
                    int value = Integer.parseInt(row.get(x));
                    board.pieces[x][y] = new Piece();
                    board.pieces[x][y].value = value;
                }
            }
            boards.add(board);
        }

        callFor:
        for (int i = 0; i < calledNumbers.size(); i++) {
            int calledNumber = Integer.parseInt(calledNumbers.get(i));
            for (int boardIndex = 0; boardIndex < boards.size(); boardIndex++) {
                final Board board = boards.get(boardIndex);
                for (int x = 0; x < 5; x++) {
                    for (int y = 0; y < 5; y++) {
                        if (board.pieces[x][y].value == calledNumber) {
                            board.pieces[x][y].called = true;
                        }
                    }
                }
                // Check if the board won
                boolean won = false;
                nextRow:
                for (int x = 0; x < 5; x++) {
                    for (int y = 0; y < 5; y++) {
                        if (board.pieces[x][y].called == false) {
                            continue nextRow;
                        }
                    }
                    // Won row
                    won = true;
                    break;
                }
                if (!won) {
                    nextColumn:
                    for (int y = 0; y < 5; y++) {
                        for (int x = 0; x < 5; x++) {
                            if (board.pieces[x][y].called == false) {
                                continue nextColumn;
                            }
                        }
                        // Won row
                        won = true;
                        break;
                    }
                }
                if (won) {
                    long sum = 0;
                    for (int x = 0; x < 5; x++) {
                        for (int y = 0; y < 5; y++) {
                            if (board.pieces[x][y].called == false) {
                                sum += board.pieces[x][y].value;
                            }
                        }
                    }

                    LogUtilities.logGreen("Sum: " + sum);
                    LogUtilities.logGreen("Just called: " + calledNumber);
                    LogUtilities.logGreen("Solution: " + (sum * calledNumber));
                    boards.remove(boardIndex);
                    boardIndex -= 1;
                }
            }
        }
    }

    class Board {
        public Piece[][] pieces = new Piece[5][5];
    }

    class Piece {
        public boolean called = false;
        public int value = -1;
    }
}
