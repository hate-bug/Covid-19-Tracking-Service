package com.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    Patient p;
    Event game, party;
    Date date;
    Calendar calendar;
    Place p1, p2;
    @BeforeEach
    void setUp() {
        p1 = new Place("TD", 10.02, 11.03);
        p2 = new Place("Fisher Ave", 120.09, 111.00);
        calendar = Calendar.getInstance();
        calendar.set(2020, 01, 29, 17, 33);
        date = new Date();
        date.setTime(calendar.getTimeInMillis());
        game = new Event("game", p1, date);
        party = new Event("Party", p2, date);
        party.setId(1);
        p = new Patient();

    }

    @Test
    void getAllEvents() {
        assertEquals(0, p.getAllEvents().size());
        p.addAttendEvents(game);
        assertEquals("game", p.getAllEvents().get(0).getName());
        p.addAttendEvents(game);
        assertEquals(1, p.getAllEvents().size());
        p.addAttendEvents(party);
        assertEquals(2, p.getAllEvents().size());
    }
}