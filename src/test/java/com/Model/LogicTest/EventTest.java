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

class EventTest {

    Place td;
    Date date;
    Event footballGame, shopEvent;
    Patient p1, p2;
    Calendar calendar;
    PatientEventAssociation asso1, asso2;

    @BeforeEach
    void setUp() {
        calendar = Calendar.getInstance();
        calendar.set(2020, 0, 29);
        date = new Date(calendar.getTimeInMillis());
        td = new Place("TD place", 11.12, 12.22);
        footballGame = new Event("Game Watch", td, date);
        shopEvent = new Event("Shopping", td, date);
        p1 = new Patient();
        p2 = new Patient();
        asso1 = new PatientEventAssociation(footballGame, p1, true);
        asso2 = new PatientEventAssociation(shopEvent, p2, true);
    }

   @Test
    void getAttendedPatientNum() {
       assertEquals(0, footballGame.getPatientEventAssociations().size());
       footballGame.addAssociation(asso1);
       assertEquals(1, footballGame.getPatientEventAssociations().size());
       //add a redundant patient
       shopEvent.addAssociation(asso2);
       assertEquals(1, shopEvent.getPatientEventAssociations().size());
    }

}