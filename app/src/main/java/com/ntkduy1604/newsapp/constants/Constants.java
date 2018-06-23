package com.ntkduy1604.newsapp.constants;

/**
 * Created by NTKDUY on 6/22/2018
 * for PIGGY HOUSE
 * you can contact me at: ntkduy1604@gmail.com
 */
public class Constants {
    public static final String REQUEST_URL = "https://content.guardianapis.com/search";

    // Getting SECTION
    public static final String API_SECTION = "section";
    public static final String NEWS_SECTION = "news";
    public static final String OPINION_SECTION = "commentisfree";
    public static final String SPORT_SECTION = "sport";
    public static final String LIFESTYLE_SECTION = "lifeandstyle";

    // Getting FIELDS
    public static final String API_FIELD = "show-fields";
    public static final String AUTHOR_FIELD = "byline";
    public static final String TITLE_FIELD = "webTitle";
    public static final String URL_FIELD = "webUrl";
    public static final String PUBLISH_DATE_FIELD = "webPublicationDate";

    // API KEY
    public static final String API_KEY = "api-key";
    public static final String TEST_API_KEY = "test";
    public static final String PERSONAL_API_KEY = "e8f6e94b-567d-4fdb-a6a9-cd5eb8539536";

    // SETTING PARAMETERS
    public static final String ORDER_BY = "order-by";
    public static final String EDITION = "production-office";

    public static final int NEWS_LOADER_ID = 1;
    public static final int OPINION_LOADER_ID = 2;
    public static final int SPORT_LOADER_ID = 3;
    public static final int LIFESTYLE_LOADER_ID = 4;
}
