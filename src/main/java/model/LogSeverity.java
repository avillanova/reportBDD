package model;

import java.io.Serializable;

/**
 * @author Alex Villanova
 */
public enum LogSeverity implements Serializable {
    HIGHEST,
    HIGH,
    MEDIUM,
    LOW;

    @Override
    public String toString(){
        switch (this) {
            case HIGHEST: return "Highest";
            case HIGH: return "High";
            case MEDIUM: return "Medium";
            case LOW: return "Low";
            default: return "Low";
        }
    }
}