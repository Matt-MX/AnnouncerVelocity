package com.mattmx.announcer.logic;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

public class Delay {
    private DelayType type;
    private int time;
    private int maxTime;
    private long next;

    public Delay(String line) {
        String[] args = line.split(" ");
        if (args.length > 1) {
            constr(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } else {
            constr(Integer.parseInt(line), -1);
        }
    }

    public static boolean isValid(String line) {
        return line.matches("\\d+(\\s\\d+)?");
    }

    public Delay(int time) {
        this(time, -1);
    }

    public Delay(int time, int maxTime) {
        constr(time, maxTime);
    }

    private void constr(int time, int maxTime) {
        this.time = time;
        if (maxTime == -1) {
            this.type = DelayType.STATIC;
        } else {
            this.maxTime = maxTime;
            this.type = DelayType.RANGE;
            this.next = AnnouncerManager.getTimer() + new Random().nextInt(maxTime - time) + time;
        }
    }

    public void setNext(long i) {
        this.next = i;
    }

    public long getNext() {
        return next;
    }

    @Override
    public String toString() {
        if (type == DelayType.RANGE) {
            return time + " " + maxTime;
        } else {
            return Integer.toString(time);
        }
    }

    public enum DelayType {
        RANGE,
        STATIC;
    }

    public DelayType getType() {
        return type;
    }

    public Delay setType(DelayType type) {
        this.type = type;
        return this;
    }

    public int getTime() {
        return time;
    }

    public Delay setTime(int time) {
        this.time = time;
        return this;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public Delay setMaxTime(int maxTime) {
        this.maxTime = maxTime;
        return this;
    }
}
