import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solver {
    private List<Board> solved;
    private int moves;

    public Solver(Board initial) {
        Board board = initial;
        MinPQ<Board> q;

        // List of steps to solve board
        solved = new ArrayList<>();
        solved.add(board);

        moves = 0;

        int i = 0;
        //while (!board.isGoal() || i++ < 10) {
            q = new MinPQ<>((o1, o2) -> Integer.compare(o1.manhattan() + moves, o2.manhattan() + moves));
            for (Board b : board.neighbors())
                q.insert(b);

            // Find the minimum element
            board = q.min();

            // Add to the sequence of solution steps
            solved.add(board);

            // Increment moves
            moves++;
        //}
    }

    public boolean isSolvable() {
        return true;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        // Defensive copy
        return new ArrayList<>(solved);
    }

    public static void main(String[] args) {
        // create initial board from file
/*        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);*/
        Board initial = new Board(new int[][] {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 }
        });

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
