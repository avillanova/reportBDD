package model;

import java.io.Serializable;

/**
 * @author Alex Villanova
 */
public enum LogStatus implements Serializable {
    PASS,
    FAIL,
    SKIP,
    INFO,
    UNKNOW;

    @Override
    public String toString(){
        switch (this) {
            case PASS: return "pass";
            case FAIL: return "fail";
            case INFO: return "info";
            case SKIP: return "skip";
            default: return "unknown";
        }
    }
}
