package com.newscurator.schema;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class News {

    private String newsTitle;
    private String newsUrl;
}
