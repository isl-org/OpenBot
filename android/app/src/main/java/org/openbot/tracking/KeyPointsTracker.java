package org.openbot.tracking;

import org.tensorflow.lite.examples.posenet.lib.KeyPoint;
import org.tensorflow.lite.examples.posenet.lib.Person;

public class KeyPointsTracker {
    Person person ;

    public void trackPerson(Person person) {
        this.person = person;
    }




}
