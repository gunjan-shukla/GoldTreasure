public class Coordinate {
    private int row;
    private int column;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof Coordinate)) {
            return false;
        }

        Coordinate current = (Coordinate) o;

        return (this.row == current.row) && (this.column == current.column);
    }
}

