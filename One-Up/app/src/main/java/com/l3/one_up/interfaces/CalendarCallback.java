package com.l3.one_up.interfaces;

public interface CalendarCallback {
    void onDateClicked(int year, int month, int day);
    void onDateCancelled();
}
