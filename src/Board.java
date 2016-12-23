import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int size;
    private final int[][] blocks;
    private final int[][] SOL_BOARD;
    private final int[][] SOL_COORDS;

    public Board(int[][] blocks) {
        this.size = blocks.length;
        this.blocks = blocks;

        // Initialize the solved board
        SOL_BOARD = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                SOL_BOARD[i][j] = i == size && j == size ? 0 : size * i + j + 1;

        // Map values to coordinates for manhattan calculation
        SOL_COORDS = new int[size * size][2];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (size * i + j + 1 < 9) {
                    SOL_COORDS[size * i + j + 1] = new int[]{i, j};
                }
        SOL_COORDS[0] = new int[] { size, size };
    }

    public static void main(String[] args) {
        int[][] board = new int[][] {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 }
        };
        Board b = new Board(board);

        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        System.out.println(b.isGoal());
        System.out.println(b.equals(new int[][] {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 }
        }));

        System.out.println("Board");
        System.out.println(b);
        System.out.println("Neighbors");
        for (Board bs : b.neighbors()) {
            System.out.println("manhattan: " + bs.manhattan());
            System.out.println(bs);
        }

        System.out.println("Twin");
        System.out.println(b.twin());
    }

    public int dimension() {
        return size;
    }

    public int hamming() {
        int h = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                h += blocks[i][j] == 0 || blocks[i][j] == SOL_BOARD[i][j] ? 0 : 1;
        return h;
    }

    public int manhattan() {
        int m = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                int s = blocks[i][j];
                int[] c = SOL_COORDS[s];
                if (s != 0)
                    m += distance(i, j, c[0], c[1]);
            }
        return m;
    }

    public boolean isGoal() {
        return this.equals(SOL_BOARD);
    }

    public Board twin() {
        // Find a non-zero coordinate
        int i, j;
        do {
            i = StdRandom.uniform(0, size);
            j = StdRandom.uniform(0, size);
        } while (blocks[i][j] == 0);

        // Find another set
        int k, l;
        do {
            k = StdRandom.uniform(0, size);
            l = StdRandom.uniform(0, size);
        } while (blocks[k][l] == 0 || (k == i && l == j));

        // Swap the blocks
        swap(i, j, k, l);

        return this;
    }

    public boolean equals(Object y) {
        if (!(y instanceof int[][]))
            return false;

        int[][] by = (int[][]) y;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (blocks[i][j] != by[i][j])
                    return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        int[] empty = findEmpty(blocks);
        int i = empty[0], j = empty[1];

        int[][] dirs = new int[][] {
                { i - 1, j },
                { i + 1, j },
                { i, j - 1 },
                { i, j + 1 }
        };

        List<Board> neighbors = new ArrayList<>();
        for (int f = 0; f < dirs.length; f++)
            if (testBounds(dirs[f]))
                neighbors.add(createSwapBoard(empty, dirs[f]));

        return neighbors;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(size + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                b.append(" " + blocks[i][j]);
            b.append("\n");
        }
        return b.toString();
    }

    private Board createSwapBoard(int[] empty, int[] coord) {
        // Copy blocks
        int[][] nblocks = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                nblocks[i][j] = blocks[i][j];

        int block = nblocks[coord[0]][coord[1]];
        nblocks[coord[0]][coord[1]] = 0;
        nblocks[empty[0]][empty[1]] = block;

        return new Board(nblocks);
    }

    private boolean testBounds(int[] inds) {
        return inds[0] >= 0 && inds[0] < size && inds[1] >=0 && inds[1] < size;
    }

    private int[] findEmpty(int[][] blocks) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (blocks[i][j] == 0)
                    return new int[] { i, j };
        return null;
    }

    private int distance(int i, int j, int k, int l) {
        return Math.abs(i - k) + Math.abs(j - l);
    }

    private void swap(int i, int j, int k, int l) {
        int temp = blocks[i][j];
        blocks[i][j] = blocks[k][l];
        blocks[k][l] = temp;
    }
}
