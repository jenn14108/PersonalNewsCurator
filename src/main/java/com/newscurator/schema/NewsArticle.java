package com.newscurator.schema;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class NewsArticle {

    private String title;
    private String url;
}
