package com.github.game;

public class Location {
    private double x, y, z;
    public Location(double x, double y, double z) {
        this.x = x;
        this.y = 0f;
        this.z = z;
    }

    public double getX(){return x;}

    public double getZ(){return z;}
    public double Distance(Location other){
        double tempX = other.getX();
        double tempZ = other.getZ();
        return Math.sqrt((tempX*tempX) + (tempZ*tempZ));
    }
}
