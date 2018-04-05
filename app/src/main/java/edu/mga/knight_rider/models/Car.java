package edu.mga.knight_rider.models;

public class Car {
    private int id;
    private String maker;
    private String type;
    private int capacity;
    private String color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Car(int id, String maker, String type, int capacity, String color) {

        this.id = id;
        this.maker = maker;
        this.type = type;
        this.capacity = capacity;
        this.color = color;
    }
}
