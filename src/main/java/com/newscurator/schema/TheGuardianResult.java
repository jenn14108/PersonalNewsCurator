package com.newscurator.schema;

import lombok.Getter;

@Getter
public class TheGuardianResult {

    String id;
    String type;
    String sectionId;
    String sectionName;
    String webPublicationDate;
    String webTitle;
    String webUrl;
    String apiUrl;
    boolean isHosted;
    String pillarId;
    String pillarName;

    public String toString(){
        return "webTitle: " + this.webTitle;
    }
}
