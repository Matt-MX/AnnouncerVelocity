package com.mattmx.announcer.logic;

import org.apache.commons.lang3.ArrayUtils;

public class Delay {
    private DelayType type;
    private int time;
    private int maxTime;

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
        if (maxTime == -1) {
            this.type = DelayType.STATIC;
        } else {
            this.maxTime = maxTime;
            this.type = DelayType.RANGE;
        }
        this.time = time;
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
