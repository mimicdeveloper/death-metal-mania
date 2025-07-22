
package com.deathmetalmania.model.api.ticketmaster;




public class Access {

    private String startDateTime;
    private Boolean startApproximate;
    private Boolean endApproximate;

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Boolean getStartApproximate() {
        return startApproximate;
    }

    public void setStartApproximate(Boolean startApproximate) {
        this.startApproximate = startApproximate;
    }

    public Boolean getEndApproximate() {
        return endApproximate;
    }

    public void setEndApproximate(Boolean endApproximate) {
        this.endApproximate = endApproximate;
    }

}
