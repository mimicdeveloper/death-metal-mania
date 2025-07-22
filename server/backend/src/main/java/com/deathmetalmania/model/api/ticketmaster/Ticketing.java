
package com.deathmetalmania.model.api.ticketmaster;




public class Ticketing {

    private SafeTix safeTix;
    private AllInclusivePricing allInclusivePricing;
    private String id;

    public SafeTix getSafeTix() {
        return safeTix;
    }

    public void setSafeTix(SafeTix safeTix) {
        this.safeTix = safeTix;
    }

    public AllInclusivePricing getAllInclusivePricing() {
        return allInclusivePricing;
    }

    public void setAllInclusivePricing(AllInclusivePricing allInclusivePricing) {
        this.allInclusivePricing = allInclusivePricing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
