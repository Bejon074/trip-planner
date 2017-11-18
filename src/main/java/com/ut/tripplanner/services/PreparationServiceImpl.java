package com.ut.tripplanner.services;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class PreparationServiceImpl implements PreparationService {

    @Override
    public void populateDatabase() throws IOException {
        CSVReader reader = new CSVReader(new FileReader("./gfts/stop_times.txt"), ',', '"', 1);
        List<String[]> allStopTimeRows = reader.readAll();
        reader = new CSVReader(new FileReader("./gfts/stops.txt"), ',', '"', 1);
        List<String[]> allStopRows = reader.readAll();
        reader = new CSVReader(new FileReader("./gfts/trips.txt"), ',', '"', 1);
        List<String[]> allTripRows = reader.readAll();
        reader = new CSVReader(new FileReader("./gfts/routes.txt"), ',', '"', 1);
        List<String[]> allRouteRows = reader.readAll();
        reader = new CSVReader(new FileReader("./gfts/calendar.txt"), ',', '"', 1);
        List<String[]> allCalendarRows = reader.readAll();

        //Read CSV line by line and use the string array as you want
        for(String[] row : allCalendarRows){
            System.out.println(Arrays.toString(row));
        }
    }
}
