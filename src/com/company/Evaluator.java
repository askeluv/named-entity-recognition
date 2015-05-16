package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.*;

/**
 * Class for evaluating the performance of a Named Entity Recognition program for identifying persons.
 * Computes precision and recall based on a ground-truth set.
 *
 * Created by Alexander Svanevik on 16.05.15.
 */
public class Evaluator {

    public Evaluator(DocumentParser parser, PersonFinder finder, String testFilePath) throws Exception {

        TestCase testCase = testCaseFromFile(testFilePath);
        String text = parser.extractTextFromUrl(testCase.getUrl());
        List<String> extractedPersons = finder.extractPersons(text);

        System.out.printf("Precision: %.2f\n", testCase.precision(extractedPersons));
        System.out.printf("Recall: %.2f\n", testCase.recall(extractedPersons));
    }

    /**
     * Reads a test case from a JSON object
     *
     * @param filePath - file path containing JSON object
     * @return TestCase object containing URL and ground-truth names
     * @throws Exception
     */
    public TestCase testCaseFromFile(String filePath) throws Exception {
        // read the json file
        FileReader reader = new FileReader(filePath);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

        // get a String from the JSON object
        String url = (String) jsonObject.get("url");

        // get an array from the JSON object
        JSONArray jsonNames = (JSONArray) jsonObject.get("names");

        List<String> names = new ArrayList<String>();
        Iterator i = jsonNames.iterator();
        // take each value from the json array separately
        while (i.hasNext()) {
            JSONObject innerObj = (JSONObject) i.next();
            names.add((String) innerObj.get("name"));
        }
        return new TestCase(url,names);
    }

    /**
     * TestCase class containing ground-truth person names for a given URL.
     */
    private class TestCase{
        private String url;
        private List<String> names;

        public TestCase(String url, List<String> names) {
            this.url = url;
            this.names = names;
        }

        public String getUrl() {
            return this.url;
        }

        public List<String> getNames() {
            return this.names;
        }

        /**
         * Computes precision on the test case given the candidates.
         *
         * @param candidates - the candidate person names
         * @return precision (proportion of candidates which were persons)
         */
        public double precision(List<String> candidates) {
            Set<String> tmpSet = new HashSet<String>(candidates);
            Set<String> actualSet = new HashSet<String>(this.names);

            int numPredictedPositives = tmpSet.size();
            tmpSet.retainAll(actualSet); // get intersection

            return (double) tmpSet.size() / numPredictedPositives;
        }

        /**
         * Computes recall on the test case given the candidates.
         *
         * @param candidates - the candidate person names
         * @return recall (proportion of ground-truth person names that were identified)
         */
        public double recall(List<String> candidates) {
            Set<String> tmpSet = new HashSet<String>(candidates);
            Set<String> actualSet = new HashSet<String>(this.names);

            tmpSet.retainAll(actualSet); // get intersection

            return (double) tmpSet.size() / actualSet.size();
        }
    }

}
