package model;

import java.io.Serializable;

/**
 * @author Alex Villanova
 */
public enum LogPriority implements Serializable {
    HIGHEST,
    HIGH,
    MEDIUM,
    LOW,
    LOWEST;

    @Override
    public String toString(){
        switch (this) {
            case HIGHEST: return "Highest";
            case HIGH: return "High";
            case LOW: return "Low";
            case LOWEST: return "Lowest";
            default: return "Medium";
        }
    }
}
