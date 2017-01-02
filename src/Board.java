import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int size;
    private final char[][] blocks;

    // Distance cache values
    private int man;

    public Board(int[][] blks) {
        this.size = blks.length;

        // Copy the argument array into a new array
        this.blocks = new char[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                this.blocks[i][j] = (char) blks[i][j];

        // Calculate the manhattan distance
        this.man = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                int s = blocks[i][j];
                if (s != 0)
                    this.man += distance(i, j, (s - 1) / size, (s - 1) % size);
            }
    }

    public int dimension() {
        return size;
    }

    public int hamming() {
        int h = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                h += blocks[i][j] == 0 || blocks[i][j] == sol(i, j) ? 0 : 1;
        return h;
    }

    public int manhattan() {
        return man;
    }

    // The solution value for coordinate (i, j)
    private int sol(int i, int j) {
        return i == size - 1 && j == size - 1 ? 0 : size * i + j + 1;
    }

    // Taxicab distance between two points
    private int distance(int i, int j, int k, int l) {
        return Math.abs(i - k) + Math.abs(j - l);
    }

    public boolean isGoal() {
        return man == 0;
    }

    public Board twin() {
        int[][] nblocks = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                nblocks[i][j] = blocks[i][j];

        // Find a non-zero coordinate
        int i, j;
        do {
            i = StdRandom.uniform(0, size);
            j = StdRandom.uniform(0, size);
        } while (nblocks[i][j] == 0);

        // Find another set
        int k, l;
        do {
            k = StdRandom.uniform(0, size);
            l = StdRandom.uniform(0, size);
        } while (nblocks[k][l] == 0 || (k == i && l == j));

        // Swap the blocks
        int temp = nblocks[i][j];
        nblocks[i][j] = nblocks[k][l];
        nblocks[k][l] = temp;

        return new Board(nblocks);
    }

    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null || y.getClass() != this.getClass())
            return false;

        Board by = (Board) y;
        // Board must be the same dimension
        if (by.dimension() != this.dimension())
            return false;

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (this.blocks[i][j] != by.blocks[i][j])
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
        return inds[0] >= 0 && inds[0] < size && inds[1] >= 0 && inds[1] < size;
    }

    private int[] findEmpty(char[][] blks) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (blks[i][j] == 0)
                    return new int[] { i, j };
        throw new IllegalArgumentException("No empty space found!");
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(size + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                b.append(" " + (int) blocks[i][j]);
            b.append("\n");
        }
        return b.toString();
    }

    public static void main(String[] args) {
        int[][] board = new int[][] {
                { 5, 13, 14,  0 },
                { 3, 10,  2, 11 },
                { 9, 15, 12,  1 },
                { 7,  8,  4,  6 }
        };
        Board b = new Board(board);

        System.out.println("hamming: " + b.hamming());
        System.out.println("manhattan: " + b.manhattan());
        System.out.println(b.isGoal());
        System.out.println(b.equals(new Board(new int[][] {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 }
        })));

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
}
