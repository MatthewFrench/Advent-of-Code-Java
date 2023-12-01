package Advent.of.Code.Java.Year_2022;

import Advent.of.Code.Java.Utility.DataUtilities;
import Advent.of.Code.Java.Utility.DayUtilities;
import Advent.of.Code.Java.Utility.LoadUtilities;
import Advent.of.Code.Java.Utility.LogUtilities;
import Advent.of.Code.Java.Utility.StringUtilities;
import Advent.of.Code.Java.Utility.Structures.DayWithExecute;
import Advent.of.Code.Java.Utility.Structures.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

public class Day_16 implements DayWithExecute {
    public void run() throws Exception {
        DayUtilities.run("input/2022/16/", this);
    }

    /**
     * @param stepsToRoom All the rooms that this room can lead to and the steps to get there from this room
     *                    Suppose this room is AA and it connects to BB and DD, but both of them connect to CC
     *                    Steps to CC would look like:
     *                    CC ->
     *                    BB -> CC
     *                    DD -> CC
     */
    record ValveRoom(String name, long rate,
                     List<ValveRoom> connectedRooms,
                     Map<ValveRoom, List<List<ValveRoom>>> stepsToRoom) {
        @Override
        public int hashCode() {
            return Objects.hashCode(this.name);
        }
    }

    record Actor(ValveRoom currentRoom, Integer minutesLeft) {
        @Override
        public int hashCode() {
            return Objects.hashCode(this.currentRoom);
        }
    }

    record PressurePath(List<Actor> actors, long totalPressureReleased, Set<ValveRoom> roomPressuresReleased,
                        List<Pair<ValveRoom, Integer>> releasedRoomAtMinute) {
        PressurePath(
                final List<Actor> actors,
                final long totalPressureReleased,
                final Set<ValveRoom> roomPressuresReleased,
                final List<Pair<ValveRoom, Integer>> releasedRoomAtMinute
        ) {
            this.actors = actors;
            // Sort so the actors with least minutes is first. That way when we simulate them
            // We will fully simulate the likely longest path first.
            this.actors.sort(Comparator.comparingLong(Actor::minutesLeft));
            this.totalPressureReleased = totalPressureReleased;
            this.roomPressuresReleased = roomPressuresReleased;
            this.releasedRoomAtMinute = releasedRoomAtMinute;
        }
    }

    record PathStep(int minutesLeft, ValveRoom fromRoom, ValveRoom toRoom) {
    }

    public void executeWithInput(final String fileName) throws Exception {
        //Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        final List<String> inputRows = StringUtilities.splitStringIntoList(LoadUtilities.loadTextFileAsString(fileName), "\n");
        final Map<String, ValveRoom> roomMap = new HashMap<>();
        final List<ValveRoom> roomsWithPressure = new ArrayList<>();
        for (final String row : inputRows) {
            List<String> rowSplit = StringUtilities.splitStringIntoList(row, "; tunnels lead to valves ");
            if (rowSplit.size() == 1) {
                rowSplit = StringUtilities.splitStringIntoList(row, "; tunnel leads to valve ");
            }
            List<String> firstSplit = StringUtilities.splitStringIntoList(rowSplit.get(0), " has flow rate=");
            long flowRate = Long.parseLong(firstSplit.get(1));
            final String currentRoom = StringUtilities.removeStartChunk(firstSplit.get(0), "Valve ");
            final ValveRoom newRoom = new ValveRoom(currentRoom, flowRate, new ArrayList<>(), new HashMap<>());
            roomMap.put(currentRoom, newRoom);
            if (newRoom.rate > 0) {
                roomsWithPressure.add(newRoom);
            }
        }
        for (final String row : inputRows) {
            List<String> rowSplit = StringUtilities.splitStringIntoList(row, "; tunnels lead to valves ");
            if (rowSplit.size() == 1) {
                rowSplit = StringUtilities.splitStringIntoList(row, "; tunnel leads to valve ");
            }
            List<String> firstSplit = StringUtilities.splitStringIntoList(rowSplit.get(0), " has flow rate=");
            final String currentRoom = StringUtilities.removeStartChunk(firstSplit.get(0), "Valve ");
            final ValveRoom room = roomMap.get(currentRoom);
            List<String> leadToRooms = StringUtilities.splitStringIntoList(rowSplit.get(1), ", ");
            for (final String leadToRoom : leadToRooms) {
                room.connectedRooms.add(roomMap.get(leadToRoom));
            }
        }
        roomsWithPressure.sort(Comparator.comparingLong(ValveRoom::rate));
        // Map out the minimum steps to each room from each room
        for (final ValveRoom room : roomMap.values()) {
            final ArrayList<Pair<ValveRoom, List<ValveRoom>>> processRooms = new ArrayList<>();
            for (final ValveRoom connectedRoom : room.connectedRooms) {
                processRooms.add(new Pair<>(connectedRoom, new ArrayList<>()));
            }
            while (processRooms.size() > 0) {
                final Pair<ValveRoom, List<ValveRoom>> processRoomPair = processRooms.remove(0);
                final List<ValveRoom> stepRoomList = new ArrayList<>(processRoomPair.getValue());
                stepRoomList.add(processRoomPair.getKey());
                room.stepsToRoom.computeIfAbsent(processRoomPair.getKey(), (key) -> new ArrayList<>()).add(stepRoomList);
                for (final ValveRoom nextRoom : processRoomPair.getKey().connectedRooms) {
                    if (nextRoom != room &&
                            (!room.stepsToRoom.containsKey(nextRoom) || room.stepsToRoom.get(nextRoom).get(0).size() >= processRoomPair.getValue().size() + 1)) {
                        final List<ValveRoom> stepList = new ArrayList<>(processRoomPair.getValue());
                        stepList.add(processRoomPair.getKey());
                        processRooms.add(new Pair<>(nextRoom, stepList));
                    }
                }
            }
        }

        solution(DataUtilities.List(new Actor(roomMap.get("AA"), 26)), roomsWithPressure);
        solution(DataUtilities.List(new Actor(roomMap.get("AA"), 26), new Actor(roomMap.get("AA"), 26)), roomsWithPressure);
    }

    void solution(final List<Actor> startWithActors, final List<ValveRoom> roomsWithPressure) {
        // Release the most pressure in 30 minutes
        long mostPressureReleased = 0;
        final List<PressurePath> inProgressPaths = new ArrayList<>();
        inProgressPaths.add(new PressurePath(startWithActors, 0, new HashSet<>(), new ArrayList<>()));
        while (inProgressPaths.size() > 0) {
            final PressurePath currentPath = inProgressPaths.remove(inProgressPaths.size() - 1);

            // Don't go down every path, too many paths
            // Only move towards valves that can be released and go the shortest path
            final List<ValveRoom> pathsLeft = pathsLeftToRelease(currentPath, roomsWithPressure);
            boolean movedPath = false;
            boolean continueSimulating = true;
            if (pathsLeft.size() > 0) {

                // Check if the paths left in max state can get use more pressure released than continued simulation, if they can't, skip this rest of simulating
                {
                    long totalSimulatedRelease = currentPath.totalPressureReleased;
                    // The paths left are in order from highest to smallest, we can use that ordering
                    final List<ValveRoom> simulatePathsLeft = new ArrayList<>(pathsLeft);
                    final PriorityQueue<Integer> minutesLeftQueue = new PriorityQueue<>(Collections.reverseOrder());
                    for (final Actor actor : currentPath.actors) {
                        minutesLeftQueue.add(actor.minutesLeft);
                    }
                    for (int pathIndex = simulatePathsLeft.size() - 1; pathIndex >= 0; pathIndex--) {
                        final ValveRoom path = simulatePathsLeft.get(pathIndex);
                        final long rate = path.rate;
                        final int minutesLeft = minutesLeftQueue.remove() - 1;
                        totalSimulatedRelease += rate * minutesLeft;
                        minutesLeftQueue.add(minutesLeft);
                    }
                    if (totalSimulatedRelease < mostPressureReleased) {
                        continueSimulating = false;
                    }
                }

                if (continueSimulating) {
                    // Don't duplicate actors doing the same path from the same point with the same minutes left
                    final Set<PathStep> pathSteps = new HashSet<>();
                    for (final Actor actor : currentPath.actors) {
                        for (final ValveRoom goToRoom : pathsLeft) {
                            final PathStep newPathStep = new PathStep(actor.minutesLeft, actor.currentRoom, goToRoom);
                            if (!pathSteps.contains(newPathStep)) {
                                pathSteps.add(newPathStep);
                                if (goToRoom.equals(actor.currentRoom) && actor.minutesLeft > 0) {
                                    final Set<ValveRoom> roomsReleased = new HashSet<>(currentPath.roomPressuresReleased);
                                    roomsReleased.add(actor.currentRoom);
                                    final long releasePressure = (actor.minutesLeft - 1) * actor.currentRoom.rate;
                                    final List<Pair<ValveRoom, Integer>> releasedRoomAtMinute = new ArrayList<>(currentPath.releasedRoomAtMinute);
                                    releasedRoomAtMinute.add(new Pair<>(actor.currentRoom, actor.minutesLeft));
                                    // Re-make list of actors
                                    final List<Actor> actors = new ArrayList<>();
                                    for (final Actor copyActor : currentPath.actors) {
                                        if (actor != copyActor) {
                                            actors.add(new Actor(copyActor.currentRoom, copyActor.minutesLeft));
                                        }
                                    }
                                    actors.add(new Actor(actor.currentRoom, actor.minutesLeft - 1));
                                    inProgressPaths.add(new PressurePath(
                                            actors,
                                            currentPath.totalPressureReleased + releasePressure,
                                            roomsReleased,
                                            releasedRoomAtMinute
                                    ));
                                    movedPath = true;
                                } else {
                                    // Complete the entire path down if they have enough minutes to get there
                                    final ValveRoom currentValveRoom = actor.currentRoom;
                                    if (currentValveRoom.stepsToRoom.containsKey(goToRoom)) {
                                        final List<ValveRoom> stepsToRoom = currentValveRoom.stepsToRoom.get(goToRoom).get(0); // Get the first because it doesn't matter how we get there
                                        if (actor.minutesLeft >= stepsToRoom.size() + 1) { // We're going to the room and opening it
                                            final Set<ValveRoom> roomsReleased = new HashSet<>(currentPath.roomPressuresReleased);
                                            roomsReleased.add(goToRoom);
                                            final long releasePressure = (actor.minutesLeft - stepsToRoom.size() - 1) * goToRoom.rate;
                                            final List<Pair<ValveRoom, Integer>> releasedRoomAtMinute = new ArrayList<>(currentPath.releasedRoomAtMinute);
                                            releasedRoomAtMinute.add(new Pair<>(goToRoom, actor.minutesLeft - stepsToRoom.size()));
                                            // Re-make list of actors
                                            final List<Actor> actors = new ArrayList<>();
                                            for (final Actor copyActor : currentPath.actors) {
                                                if (actor != copyActor) {
                                                    actors.add(new Actor(copyActor.currentRoom, copyActor.minutesLeft));
                                                }
                                            }
                                            actors.add(new Actor(goToRoom, actor.minutesLeft - stepsToRoom.size() - 1));
                                            inProgressPaths.add(new PressurePath(
                                                    actors,
                                                    currentPath.totalPressureReleased + releasePressure,
                                                    roomsReleased,
                                                    releasedRoomAtMinute
                                            ));
                                            movedPath = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Regardless of if we made new paths, we can still set the max pressure released
            if (mostPressureReleased < currentPath.totalPressureReleased) {
                mostPressureReleased = currentPath.totalPressureReleased;
                if (!movedPath && continueSimulating) {
                    LogUtilities.logPurple("Most pressure released up to " + mostPressureReleased);
                }
            }
        }

        LogUtilities.logGreen("Solution " + startWithActors.size() + ": " + mostPressureReleased);
    }

    List<ValveRoom> pathsLeftToRelease(final PressurePath pressurePath, final List<ValveRoom> roomsWithPressure) {
        final List<ValveRoom> pathsLeft = new ArrayList<>();
        for (final ValveRoom valveRoom : roomsWithPressure) {
            if (!pressurePath.roomPressuresReleased.contains(valveRoom)) {
                pathsLeft.add(valveRoom);
            }
        }
        return pathsLeft;
    }
}
