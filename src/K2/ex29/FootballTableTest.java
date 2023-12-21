package K2.ex29;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

class Team {
    private String name;
    private int gamesPlayed, wins, draws, losses, goalsScored, goalsConceded;

    public Team(String name) {
        this.name = name;
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
        this.goalsScored = 0;
        this.goalsConceded = 0;
    }

    public void addGame(int scored, int conceded) {
        this.goalsScored += scored;
        this.goalsConceded += conceded;
        gamesPlayed++;
        if (scored > conceded) {
            wins++;
        } else if (scored < conceded) {
            losses++;
        } else {
            draws++;
        }
    }

    public String getName() {
        return name;
    }

    public int getGoalDiff() {
        return goalsScored - goalsConceded;
    }

    public int getPoints() {
        return 3 * wins + draws;
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d",
                name,
                gamesPlayed,
                wins,
                draws,
                losses,
                getPoints());
    }
}

class FootballTable {

    TreeMap<String, Team> teams;

    public FootballTable() {
        this.teams = new TreeMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        teams.putIfAbsent(homeTeam, new Team(homeTeam));
        teams.putIfAbsent(awayTeam, new Team(awayTeam));
        teams.get(homeTeam).addGame(homeGoals, awayGoals);
        teams.get(awayTeam).addGame(awayGoals, homeGoals);
    }

    public void printTable() {
        List<Team> teamList = new ArrayList<>(teams.values());
        teamList = teamList.stream()
                .sorted(
                        Comparator.comparing(Team::getPoints)
                                .thenComparing(Team::getGoalDiff)
                                .reversed()
                                .thenComparing(Team::getName)
                )
                .collect(Collectors.toList());
        for (int i = 0; i < teamList.size(); i++) {
            System.out.println(String.format("%2d. %s", i + 1, teamList.get(i)));
        }
    }

}