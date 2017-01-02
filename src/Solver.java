import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Stack<Board> solvedBoards;
    private boolean solvable;

    public Solver(Board initial) {
        SearchNode board = new SearchNode(initial, 0, null),
                twin = new SearchNode(initial.twin(), 0, null),
                del, tDel;

        solvable = false;

        // Priority queues for twin board and initial board
        MinPQ<SearchNode> q = new MinPQ<>();
        MinPQ<SearchNode>  tq = new MinPQ<>();

        // Add the initial board to the priority queues
        q.insert(board);
        tq.insert(twin);
        while (!board.board().isGoal() && !twin.board().isGoal()) {
            // Delete the minimum nodes
            del = q.delMin();
            tDel = tq.delMin();

            // Insert original board neighbors
            for (Board b : board.board().neighbors())
                if (!b.equals(board.previous() == null ? null : board.previous().board()))
                    q.insert(new SearchNode(b, board.moves() + 1, board));

            // Insert twin board neighbors
            for (Board b : twin.board().neighbors())
                if (!b.equals(twin.previous() == null ? null : twin.previous().board()))
                    tq.insert(new SearchNode(b, twin.moves() + 1, twin));

            // Find the minimum elements for twin and board
            board = del;
            twin = tDel;
        }

        if (board.board().isGoal()) {
            // Set solvable to true
            solvable = true;

            // Copy the solution by working backward along the pointers starting with the goal board
            solvedBoards = new Stack<>();
            solvedBoards.push(board.board());
            while ((board = board.previous()) != null)
                solvedBoards.push(board.board());
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return isSolvable() ? solvedBoards.size() - 1 : -1;
    }

    public Iterable<Board> solution() {
        return solvedBoards;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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

    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode previous;
        private final int moves;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        public Board board() {
            return board;
        }

        public SearchNode previous() {
            return previous;
        }

        public int moves() {
            return moves;
        }

        @Override
        public int compareTo(SearchNode o) {
            int m1 = this.board().manhattan() + this.moves();
            int m2 = o.board().manhattan() + o.moves();
            return Integer.compare(m1, m2);
        }
    }
}
