import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import java.util.HashMap;
import java.util.LinkedList;

public class BaseballElimination {
    private HashMap<String, Integer> teams;
    private String[] teamArray;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;
    public BaseballElimination(String filename) {
    // create a baseball division from given filename in format specified below
        In data = new In(filename);
        int numTeams = data.readInt();
        teams = new HashMap<String, Integer>();
        teamArray = new String[numTeams];
        w = new int[numTeams];
        l = new int[numTeams];
        r = new int[numTeams];
        g = new int[numTeams][numTeams];
        int teamId = 0;
        while (!data.isEmpty()) {
            String name = data.readString();
            teams.put(name, teamId);
            teamArray[teamId] = name;
            // read w, l, r
            w[teamId] = data.readInt();
            l[teamId] = data.readInt();
            r[teamId] = data.readInt();
            // real g
            for (int i = 0; i < numTeams; i++) {
                g[teamId][i] = data.readInt();
            }
            teamId += 1;
        }        
    }
    public int numberOfTeams() {
        // number of teams
        return teams.size();
    }
    public Iterable<String> teams() {
        // all teams
        return teams.keySet();
    }
    public int wins(String team) {
        // number of wins for given team
        return w[teams.get(team)];
    }
    public int losses(String team) {
        // number of losses for given team
        return l[teams.get(team)];
    }
    public int remaining(String team) {
        // number of remaining games for given team
        return r[teams.get(team)];
    }
    public int against(String team1, String team2) {
        // number of remaining games between team1 and team2
        return g[teams.get(team1)][teams.get(team2)];
    }
    public boolean isEliminated(String team) {
        // is given team eliminated?
        return certificateOfElimination(team) != null;
    }
    public Iterable<String> certificateOfElimination(String team) {
        // subset R of teams that eliminates given team; null if not eliminated
        int x = teams.get(team);
        int n = numberOfTeams() - 1;
        // trivial
        for (int i = 0; i <= n; i++) {
            if (w[x] + r[x] - w[i] < 0) {
                LinkedList<String> list = new LinkedList<String>();
                list.add(teamArray[i]);
                return list;
            }
        }
        // maxflow
        int total = n + (n + 1) * (n + 1) + 2;
        int sourceId = total - 2;
        int targetId = total - 1;
        // creating a graph
        FlowNetwork graph = new FlowNetwork(total);
        for (int i = 0; i <= n; i++) {
            if (i == x) continue;
            for (int j = i; j <= n; j++) {
                if (j == x) continue;
                if (g[i][j] > 0) {
                    int gameId = n + (i + 1) * (j + 1) - 1;
                    // source to game
                    graph.addEdge(new FlowEdge(sourceId, gameId, g[i][j]));
                    // game to team
                    graph.addEdge(new FlowEdge(gameId, i, 
                                               Double.POSITIVE_INFINITY));
                    graph.addEdge(new FlowEdge(gameId, j, 
                                               Double.POSITIVE_INFINITY));
                }
            }
            graph.addEdge(new FlowEdge(i, targetId, w[x] + r[x] - w[i]));
        }
        FordFulkerson maxFlow = new FordFulkerson(graph, sourceId, targetId);
        LinkedList<String> list  = null;
        for (FlowEdge edge : graph.adj(targetId)) {
            if (maxFlow.inCut(edge.from())) {
                if (list == null) {
                    list = new LinkedList<String>();
                }
                list.add(teamArray[edge.from()]);
            }
        }
        return list;
    }
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}