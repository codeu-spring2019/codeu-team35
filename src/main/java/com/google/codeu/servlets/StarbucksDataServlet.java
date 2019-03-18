package com.google.codeu.servlets;

import java.io.IOException;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

/**
 * Returns UFO data as a JSON array, e.g. [{"lat": 38.4404675, "lng": -122.7144313}]
 */
@WebServlet("/ufo-data")
public class StarbucksDataServlet extends HttpServlet {

    JsonArray starbucksLocationsArray;

    @Override
    public void init() {
        starbucksLocationsArray = new JsonArray();
        Gson gson = new Gson();
        Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/starbucksDirectory.csv"));
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] cells = line.split(",");

            String state = cells[0];
            double lat = Double.parseDouble(cells[2]);
            double lng = Double.parseDouble(cells[1]);

            starbucksLocationsArray.add(gson.toJsonTree(new Starbucks(state, lat, lng)));
        }
        scanner.close();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.getOutputStream().println(starbucksLocationsArray.toString());
    }

    private static class Starbucks{
        String state;
        double lat;
        double lng;

        private Starbucks(String state, double lat, double lng) {
            this.state = state;
            this.lat = lat;
            this.lng = lng;
        }
    }
}
