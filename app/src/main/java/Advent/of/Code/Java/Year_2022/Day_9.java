package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day_9 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/9/", this);
    }

    public void executeWithInput(final String fileName) throws Exception {
        final List<String> inputRows = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");

        {
            Set<Pair<Long, Long>> previousSpots = new HashSet<>();

            long headX = 0;
            long headY = 0;
            long tailX = 0;
            long tailY = 0;
            previousSpots.add(new Pair<>(tailX, tailY));

            for (final String input : inputRows) {
                final List<String> inputSplit = StringUtilities.splitStringIntoList(input, " ");
                final String direction = inputSplit.get(0);
                final long numberOfSteps = Long.parseLong(inputSplit.get(1));
                for (int i = 0; i < numberOfSteps; i++) {
                    if (direction.equals("R")) {
                        headX += 1;
                    } else if (direction.equals("L")) {
                        headX -= 1;
                    } else if (direction.equals("D")) {
                        headY -= 1;
                    } else if (direction.equals("U")) {
                        headY += 1;
                    }
                    if (headX - tailX > 1) {
                        if (headY > tailY) {
                            tailY += 1;
                        } else if (headY < tailY) {
                            tailY -= 1;
                        }
                        tailX += 1;
                    }
                    if (tailX - headX > 1) {
                        if (headY > tailY) {
                            tailY += 1;
                        } else if (headY < tailY) {
                            tailY -= 1;
                        }
                        tailX -= 1;
                    }
                    if (headY - tailY > 1) {
                        if (headX > tailX) {
                            tailX += 1;
                        } else if (headX < tailX) {
                            tailX -= 1;
                        }
                        tailY += 1;
                    }
                    if (tailY - headY > 1) {
                        if (headX > tailX) {
                            tailX += 1;
                        } else if (headX < tailX) {
                            tailX -= 1;
                        }
                        tailY -= 1;
                    }
                    previousSpots.add(new Pair<>(tailX, tailY));
                }
            }

            LogUtilities.logGreen("Solution : " + previousSpots.size());
        }

        {
            final List<RopeNode> rope = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                rope.add(
                        new RopeNode(0, 0, i == 0 ? null : rope.get(rope.size()-1))
                );
            }
            final RopeNode head = rope.remove(0);
            final RopeNode tail = rope.get(rope.size() - 1);
            Set<Pair<Long, Long>> previousSpots = new HashSet<>();
            previousSpots.add(new Pair<>(tail.getX(), tail.getY()));

            for (final String input : inputRows) {
                final List<String> inputSplit = StringUtilities.splitStringIntoList(input, " ");
                final String direction = inputSplit.get(0);
                final long numberOfSteps = Long.parseLong(inputSplit.get(1));
                for (int i = 0; i < numberOfSteps; i++) {
                    if (direction.equals("R")) {
                        head.x += 1;
                    } else if (direction.equals("L")) {
                        head.x -= 1;
                    } else if (direction.equals("D")) {
                        head.y -= 1;
                    } else if (direction.equals("U")) {
                        head.y += 1;
                    }
                    for (final RopeNode node : rope) {
                        if (node.parent.x - node.x > 1) {
                            if (node.parent.y > node.y) {
                                node.y += 1;
                            } else if (node.parent.y < node.y) {
                                node.y -= 1;
                            }
                            node.x += 1;
                        }
                        if (node.x - node.parent.x > 1) {
                            if (node.parent.y > node.y) {
                                node.y += 1;
                            } else if (node.parent.y < node.y) {
                                node.y -= 1;
                            }
                            node.x -= 1;
                        }
                        if (node.parent.y - node.y > 1) {
                            if (node.parent.x > node.x) {
                                node.x += 1;
                            } else if (node.parent.x < node.x) {
                                node.x -= 1;
                            }
                            node.y += 1;
                        }
                        if (node.y - node.parent.y > 1) {
                            if (node.parent.x > node.x) {
                                node.x += 1;
                            } else if (node.parent.x < node.x) {
                                node.x -= 1;
                            }
                            node.y -= 1;
                        }
                    }
                    previousSpots.add(new Pair<>(tail.x, tail.y));
                }
            }

            LogUtilities.logGreen("Solution 2: " + previousSpots.size());
        }
    }
    @Data
    static class RopeNode {
        long x;
        long y;
        RopeNode parent;
        public RopeNode(long x, long y, RopeNode parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }
    }
}
