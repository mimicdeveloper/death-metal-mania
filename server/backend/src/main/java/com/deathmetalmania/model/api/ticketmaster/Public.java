
package com.deathmetalmania.model.api.ticketmaster;




public class Public {

    private String startDateTime;
    private Boolean startTBD;
    private Boolean startTBA;
    private String endDateTime;

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Boolean getStartTBD() {
        return startTBD;
    }

    public void setStartTBD(Boolean startTBD) {
        this.startTBD = startTBD;
    }

    public Boolean getStartTBA() {
        return startTBA;
    }

    public void setStartTBA(Boolean startTBA) {
        this.startTBA = startTBA;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

}
