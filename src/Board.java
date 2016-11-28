public class Board {
    private final int size;
    private int turns;
    private final int[][] blocks;
    private final int[][] SOL_BOARD;

    public Board(int[][] blocks) {
        this.size = blocks.length;
        this.blocks = blocks;
        this.turns = 0;

        // Initialize the solved board
        SOL_BOARD = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                SOL_BOARD[i][j] = i == size && j == size ? 0 : size * i + j + 1;
    }

    public int dimension() {
        return size;
    }

    public int hamming() {
        int h = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                h += blocks[i][j] == 0 || blocks[i][j] == SOL_BOARD[i][j] ? 0 : 1;
        return h + turns;
    }

    public int manhattan() {
        return 0;
    }

    public boolean isGoal() {
        return false;
    }

    public Board twin() {
        return null;
    }

    public boolean equals(Object y) {
        return false;
    }

    public Iterable<Board> neighbors() {
        return null;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(turns + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                b.append(" " + blocks[i][j]);
            }
            b.append("\n");
        }
        return b.toString();
    }

    public static void main(String[] args) {
        int[][] board = new int[][] {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 }
        };
        Board b = new Board(board);
        System.out.print(b.hamming());
    }
}
