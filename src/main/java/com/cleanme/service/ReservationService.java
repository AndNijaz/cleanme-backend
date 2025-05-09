package com.cleanme.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    List<String> people = new ArrayList<>();

    public List<String> getReservations(){
        people.add("NIJAZ");
        return people;
    }
}
