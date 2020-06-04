package com.Model.LogicTest;

import com.Model.Event;
import com.Model.Patient;
import com.Model.Place;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    Patient p;
    Event game, party;
    Calendar calendar;
    Date date;
    Place p1, p2;
    @BeforeEach
    void setUp() {
        p1 = new Place("TD", 10.02, 11.03);
        p2 = new Place("Fisher Ave", 120.09, 111.00);
        calendar = Calendar.getInstance();
        calendar.set(2020, 01, 29, 17, 33);
        date = new Date(calendar.getTimeInMillis());
        game = new Event("game", p1, date);
        party = new Event("Party", p2, date);
        p = new Patient();

    }

    @Test
    void getAllEvents() {
        assertEquals(0, p.getAllEventIds().size());
        p.addAttendEvents(game);
        assertTrue(p.getAllEventIds().contains((int) (game.getId())));
        p.addAttendEvents(game);
        assertEquals(1, p.getAllEventIds().size());
        p.addAttendEvents(party);
        assertEquals(2, p.getAllEventIds().size());
    }
}