package com.example.test1;

public class DBHAgroupData {
    private String hagroupname;
    private String hagroupId;
    private String slavecount;
    private String zone;
    private String masterName;
    private String masterStatus;
    private String masterFebricStatus;
    private String masterId;
    private String slaveName;
    private String slaveStatus;
    private String slaveFebricStatus;
    private String slaveId;
    private int resId;

    public String getHagroupId() { return hagroupId; }

    public void setHagroupId(String hagroupId) {
        this.hagroupId = hagroupId;
    }

    public String getSlaveId() { return slaveId; }

    public void setSlaveId(String slaveId) {
        this.slaveId = slaveId;
    }

    public String getMasterId() { return masterId; }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }


    public String getHagroupname() { return hagroupname; }

    public void setHagroupname(String hagroupname) {
        this.hagroupname = hagroupname;
    }

    public String getSlavecount() {
        return slavecount;
    }

    public void setSlavecount(String slavecount) {
        this.slavecount = slavecount;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getMasterStatus() {
        return masterStatus;
    }

    public void setMasterStatus(String masterStatus) { this.masterStatus = masterStatus; }

    public String getMasterFebricStatus() {
        return masterFebricStatus;
    }

    public void setMasterFebricStatus(String masterFebricStatus) { this.masterFebricStatus = masterFebricStatus; }

    public void setSlaveStatus(String slaveStatus) { this.slaveStatus = slaveStatus; }

    public String getSlaveStatus() {return slaveStatus; }

    public void setSlaveName(String slaveName) { this.slaveName = slaveName; }

    public String getSlaveName() {return slaveName; }

    public void setSlaveFebricStatus(String slaveFebricStatus) { this.slaveFebricStatus = slaveFebricStatus; }

    public String getSlaveFebricStatus() { return slaveFebricStatus; }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }


}
