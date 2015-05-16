package com.company;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;

/**
 * Class for extracting text from a HTML document.
 *
 * Created by Alexander Svanevik on 16.05.15.
 */
public class DocumentParser {

    /**
     * Gets readable text from a URL.
     *
     * @param urlString - URL to read from
     * @return text
     * @throws Exception
     */
    public String extractTextFromUrl(String urlString) throws Exception {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        String plainText = "";

        url = new URL(urlString);
        is = url.openStream();
        // argument overrides memory limit:
        BodyContentHandler handler = new BodyContentHandler(10*1024*1024);
        Metadata metadata = new Metadata();
        new HtmlParser().parse(is, handler, metadata, new ParseContext());
        plainText = handler.toString();

        return plainText;
    }
}
