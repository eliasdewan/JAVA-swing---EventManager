/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ci5105202122k2056101.eventmanager.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author k2056101
 */
public class Event {

    private String title;
    private Organiser organiser;
    private LocalTime time;
    private LocalDate date;
    private String location;
    private ArrayList<Item> agendaItem;

    /**
     * Constructor 
     * Initializes agendaItem List
     */
    public Event() { 
        agendaItem = new ArrayList();
    }
    
    public ArrayList<Item> getAgendaItem() {
        return agendaItem;
    }

    public void setAgendaItem(ArrayList<Item> agendaItem) {
        this.agendaItem = agendaItem;
    }

    // General getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Organiser getOrganiser() {
        return organiser;
    }

    public void setOrganiser(Organiser organiser) {
        this.organiser = organiser;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    //String set time and date
    public void setTime(String time) {
        this.time = LocalTime.parse(time);
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<Item> getItems() {
        return agendaItem;
    }

    public void setItems(ArrayList<Item> items) {
        this.agendaItem = items;
    }

}
