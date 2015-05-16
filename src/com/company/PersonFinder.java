package com.company;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for identifying persons in text.
 *
 * Created by Alexander Svanevik on 16.05.15.
 */
public class PersonFinder {

    /**
     * Identifies person names in a text.
     *
     * @param text - the text to parse
     * @return List of persons found
     * @throws Exception
     */
    public List<String> extractPersons(String text) throws Exception {
        List<String> persons = new ArrayList<String>();
        String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
        AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
        List<List<CoreLabel>> out = classifier.classify(text);
        String buffer = "";

        for (List<CoreLabel> sentence : out) {
            for (CoreLabel word : sentence) {
                if(word.get(CoreAnnotations.AnswerAnnotation.class).equals("PERSON")) {
                    buffer += " " + word.word(); // append the name to the buffer
                } else if (!buffer.equals("")) {
                    persons.add(buffer.trim());
                    buffer = ""; // reset the buffer
                }
            }
        }
        if (!buffer.equals("")){ // if we have something in our buffer at the end of the file, then add it
            persons.add(buffer.trim());
        }
        return persons;
    }

}
