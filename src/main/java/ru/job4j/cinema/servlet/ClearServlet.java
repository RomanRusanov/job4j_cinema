package ru.job4j.cinema.servlet;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.job4j.cinema.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Roman Rusanov
 * @version 0.1
 * @since 17.12.2020
 * email roman9628@gmail.com
 * The class describe servlet action update and table hall.
 * Clear all occupied places.
 */
public class ClearServlet extends HttpServlet {
    /**
     * The postprocess.
     * @param req Request.
     * @param resp Response.
     * @throws javax.servlet.ServletException ServletException.
     * @throws java.io.IOException IOException.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PsqlStore.instOf().clearAllTakenPlace(1);
    }
}