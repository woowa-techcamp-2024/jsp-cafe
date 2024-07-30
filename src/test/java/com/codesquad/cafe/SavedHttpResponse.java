package com.codesquad.cafe;

import java.util.Arrays;
import org.apache.http.Header;
import org.apache.http.StatusLine;

public class SavedHttpResponse {
    private StatusLine statusLine;
    private Header[] headers;
    private String body;

    public SavedHttpResponse(StatusLine statusLine, Header[] headers, String body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public Header[] getHeaders() {
        return headers;

    }

    public String getFirstHeader(String key) {
        return Arrays.stream(headers)
                .filter(header -> header.getName().equals(key))
                .findFirst()
                .map(Header::getValue)
                .orElse("");
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return Arrays.stream(headers)
                .filter(header -> header.getName().equals("Content-Type"))
                .findFirst()
                .map(Header::getValue)
                .orElse("");
    }
}
