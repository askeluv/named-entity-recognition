package com.company;

/**
 * Program which extracts person names from a HTML document given its URL.
 *
 * Command line usage:
 *      java NamedEntityRecognition <url>
 *      OR
 *      java NamedEntityRecognition --evaluate <testFilePath>
 *
 * Created by Alexander Svanevik on 16.05.15.
 */
public class NamedEntityRecognition {

    public static void main(String[] args) throws Exception {

        DocumentParser parser = new DocumentParser();
        PersonFinder finder = new PersonFinder();

        // evaluate mode
        if (args.length>1) {
            String testFilePath = args[1];
            Evaluator evaluator = new Evaluator(parser,finder,testFilePath);
        }
        // run mode
        else {
            // default URL in case none is given
            String url = "http://www.oracle.com/us/corporate/press/BoardofDirectors/index.html";

            if (args.length>0) {
                url = args[0];
            }

            String text = parser.extractTextFromUrl(url);
            for (String res : finder.extractPersons(text)){
                System.out.println(res);
            }
        }


    }
}