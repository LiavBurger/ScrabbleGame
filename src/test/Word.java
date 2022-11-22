package test;


import java.util.Arrays;
import java.util.Objects;

public class Word {
    private Tile[] tiles;
    private int row;
    private int col;
    private boolean vertical;

    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        Tile[] temp = new Tile[tiles.length];
        System.arraycopy(tiles, 0, temp, 0, tiles.length);
        this.tiles = temp;
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }

    public void setTiles(Tile[] tiles) {
        Tile[] temp = new Tile[tiles.length];
        System.arraycopy(tiles, 0, temp, 0, tiles.length);
        this.tiles = temp;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isVertical() {
        return vertical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return row == word.row && col == word.col && vertical == word.vertical && Arrays.equals(tiles, word.tiles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(row, col, vertical);
        result = 31 * result + Arrays.hashCode(tiles);
        return result;
    }

    public boolean hasNullTiles() {
        for (Tile tile : tiles) {
            if (tile == null) {
                return true;
            }
        }
        return false;
    }
}
