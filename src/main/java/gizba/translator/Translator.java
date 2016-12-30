package main.java.gizba.translator;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Translator {

    public String translate(String text, String languageBefore, String languageAfter) {
        Assert.assertTrue("String variable text cannot be blank", StringUtils.isNotBlank(text));

        String translation = null;
        URIBuilder builder = newURIBuilder();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String preparedText = prepareTextToTranslate(text);
            translation = translate(preparedText, builder, client, languageBefore, languageAfter);
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }

        return translation;
    }

    private String prepareTextToTranslate(String text) {
        String result = text;

        if (result.contains("_")) {
            result = result.replaceAll("_", " ");
        }
        if (!isAllUpercase(result)) {
            result = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, result);
        }
        if (result.contains("_")) {
            result = result.replaceAll("_", " ");
        }

        return result;
    }

    private URIBuilder newURIBuilder() {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http");
        builder.setHost("www.google.com");
        builder.setPath("/translate_t");
        return builder;
    }

    private String translate(String text, URIBuilder builder, CloseableHttpClient client, String languageBefore, String languageAfter) throws IOException, URISyntaxException {
        builder.clearParameters();
        builder.setParameter("langpair", languageBefore + "|" + languageAfter);
        builder.setParameter("text", text);

        URI uri = builder.build();
        HttpGet get = new HttpGet(uri);

        try {
            CloseableHttpResponse response = client.execute(get);
            StatusLine statusLine = response.getStatusLine();
            if (!statusLine.getReasonPhrase().equals("OK")) {
                throw new RuntimeException(statusLine.toString());
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return translate(entity);
            }
        } catch (ClientProtocolException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private String translate(HttpEntity entity) throws IOException {
        String string = EntityUtils.toString(entity);
        Span resultBox = SpanBuilder.build(string, " id=result_box ");
        if (resultBox == null) {
            return null;
        }
        String result = resultBox.getString();

        String translation = "";

        int index = 0;
        Span span;
        do {
            span = SpanBuilder.build(result, " title=", index);
            if (span == null) {
                break;
            }
            translation += span.getValue();
            index = span.getEndIndex();
        } while (index > 0);

        return translation;
    }

    private boolean isAllUpercase(String string) {
        return CharMatcher.JAVA_UPPER_CASE.or(CharMatcher.WHITESPACE).matchesAllOf(string);
    }

}

