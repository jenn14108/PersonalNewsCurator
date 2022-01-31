package com.newscurator.schema;

import lombok.Getter;

@Getter
public class MarketauxResult {

    String uuid;
    String title;
    String description;
    String keywords;
    String snippet;
    String url;
    String umage_url;
    String language;
    String published_at;
    String source;
    String relevance_score;
    Object[] entities;
    Object[] similar;

    public String toString(){
        return "title: " + this.title;
    }

}
