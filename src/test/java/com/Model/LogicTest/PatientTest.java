package com.Model.LogicTest;

import com.Model.Event;
import com.Model.Patient;
import com.Model.PatientEventAssociation;
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
    PatientEventAssociation asso1, asso2;
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
        asso1 = new PatientEventAssociation(game, p, true);
        asso2 = new PatientEventAssociation(party, p, false);

    }

    @Test
    void getAllEvents() {
        assertEquals(0, p.getPatientEventAssication().size());
        p.addAssociation(asso1);
        assertTrue(p.getPatientEventAssication().contains(asso1));
        p.addAssociation(asso2);
        assertEquals(2, p.getPatientEventAssication().size());
    }
}