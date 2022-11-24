package org.example;

public class Route implements Comparable<Route> {
    private GridCell from;
    private GridCell to;

    public Route(GridCell from, GridCell to) {
        this.from = from;
        this.to = to;
    }

    public Route(double longitudeFrom, double latitudeFrom, double longitudeTo, double latitudeTo) {
        this.from = new GridCell(longitudeFrom, latitudeFrom);
        this.to = new GridCell(longitudeTo, latitudeTo);
    }

    public GridCell getFrom() {
        return from;
    }

    public GridCell getTo() {
        return to;
    }

    public boolean isInsideGrid() {
        if (from.getLatitudeIndex() < 1 || from.getLatitudeIndex() > 300 ||
                from.getLongitudeIndex() < 1 || from.getLongitudeIndex() > 300 ||
                to.getLatitudeIndex() < 1 || to.getLatitudeIndex() > 300 ||
                to.getLongitudeIndex() < 1 || to.getLongitudeIndex() > 300) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Route route) {
        if (this.from.compareTo(route.getFrom()) != 0) {
            return this.from.compareTo(route.getFrom());
        } else {
            return this.to.compareTo(route.getTo());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Route route)) {
            return false;

        }
        return this.from.equals(route.getFrom()) && this.to.equals(route.getTo());
    }

    @Override
    public String toString() {
        return "Route{" + "\n" +
                "from = " + from + "\n" +
                "to = " + to + "\n" +
                '}';
    }
}
