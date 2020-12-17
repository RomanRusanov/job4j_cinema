package ru.job4j.cinema.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.job4j.cinema.model.Place;
import ru.job4j.cinema.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Roman Rusanov
 * @version 0.1
 * @since 16.12.2020
 * email roman9628@gmail.com
 * The class serve request from index.html and payment.html.
 */
public class HallServlet extends HttpServlet {
    /**
     * The preprocess.
     * Update all occupied places.
     * @param req Request.
     * @param resp Response.
     * @throws javax.servlet.ServletException ServletException.
     * @throws java.io.IOException IOException.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PsqlStore store = PsqlStore.instOf();
        List<Place> allOccupied = store.getAllOccupiedPlaces();
        Gson jsonObject = new Gson().newBuilder()
                .setPrettyPrinting()
                .create();
        String json = jsonObject.toJson(allOccupied);
        PrintWriter writer = new PrintWriter(resp.getOutputStream(),
                false, StandardCharsets.UTF_8);
        writer.print(json);
        writer.flush();
    }

    /**
     * The postprocess.
     * If user pay ticket.
     * @param req Request.
     * @param resp Response.
     * @throws ServletException ServletException.
     * @throws IOException IOException.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fio = req.getParameter("fio");
        String phone = req.getParameter("phone");
        int row = Integer.parseInt(req.getParameter("rowVal"));
        int number = Integer.parseInt(req.getParameter("numberVal"));
        PsqlStore store = PsqlStore.instOf();
        long userId = store.addUserToAccount(fio, phone);
        if (store.placeIsTaken(userId, 1, row, number)) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            String json = "{userId : " + userId + "}";
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            PrintWriter writer = new PrintWriter(resp.getOutputStream(),
                    false, StandardCharsets.UTF_8);
            writer.print(jsonObject);
            writer.flush();
        }
    }

}