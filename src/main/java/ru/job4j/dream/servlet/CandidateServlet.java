package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlCandidate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("candidates", PsqlCandidate.instOf().findAllCandidates());
        req.setAttribute("cities", PsqlCandidate.instOf().findAllCityOfCandidates());
        req.setAttribute("photo", PsqlCandidate.instOf().findAllPhotoOfTheCandidates());
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String city = req.getParameter("city");
        PsqlCandidate.instOf().saveCandidates(
                new Candidate(Integer.parseInt(id), name, 0, Integer.parseInt(id)));
        PsqlCandidate.instOf().saveCityOfCandidate(id, city);
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
