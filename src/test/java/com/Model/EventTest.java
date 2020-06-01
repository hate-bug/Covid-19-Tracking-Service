package com.Model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Calendar;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    Place td;
    Event footballGame, shopEvent;
    Patient p1, p2;
    Date date;
    Calendar calendar;

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
        p2.setId(1);
    }

   @Test
    void getAttendedPatientNum() {
       assertEquals(0, footballGame.getAttendedPatientNum());
       footballGame.addPatient(p1);
       assertEquals(1, footballGame.getAttendedPatientNum());
       //add a redundant patient
       footballGame.addPatient(p1);
       assertEquals(1, footballGame.getAttendedPatientNum());
       //add a different patient
       footballGame.addPatient(p2);
       assertEquals(2, footballGame.getAttendedPatientNum());
    }

}