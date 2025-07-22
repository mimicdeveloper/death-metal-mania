
package com.deathmetalmania.model.api.ticketmaster;


import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketmasterApi {

    @JsonProperty("_embedded")
    private Embedded embedded;

    @JsonProperty("_links")
    private Links__3 links;
    private Page page;

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    public Links__3 getLinks() {
        return links;
    }

    public void setLinks(Links__3 links) {
        this.links = links;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

}
