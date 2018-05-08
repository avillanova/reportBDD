package model;

import java.io.Serializable;

/**
 * @author Alex Villanova
 */
public enum LogPriority implements Serializable {
    P1,
    P2,
    P3;

    @Override
    public String toString(){
        switch (this) {
            case P1: return "1";
            case P2: return "2";
            case P3: return "3";
            default: return "3";
        }
    }
}
