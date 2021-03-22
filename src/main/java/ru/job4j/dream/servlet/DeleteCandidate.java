package ru.job4j.dream.servlet;

import ru.job4j.dream.store.PsqlCandidate;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class DeleteCandidate extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String photo = req.getParameter("photo");
        PsqlCandidate.instOf().deletePhotoAndCityOfCandidate(id);
        PsqlCandidate.instOf().deleteCandidate(id);
        if (photo != null) {
            File file = new File("images" + File.separator + photo);
            file.delete();
        }
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
