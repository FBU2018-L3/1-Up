package com.l3.one_up.interfaces;

import com.l3.one_up.model.Event;
import com.l3.one_up.model.FacebookQuery;

import java.util.ArrayList;

/**
 * Created by luzcamacho on 7/24/18.
 *
 * Interface to deal with the fact that most of our calls are async. 
 */

public interface FacebookCallComplete {
    void notifyCompleteList(ArrayList<FacebookQuery.FacebookUser> list, ArrayList<String> friendIds);
    void notifyParseCallComplete(ArrayList<Event> eventsByUser);
}
