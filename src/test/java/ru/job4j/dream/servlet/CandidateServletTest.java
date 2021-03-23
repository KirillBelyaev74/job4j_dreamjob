package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.store.PsqlCandidate;
import ru.job4j.dream.store.StoreCandidate;
import ru.job4j.dream.store.ValidateStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlCandidate.class)
public class CandidateServletTest {

    @Test
    public void whenCreateCandidate() throws IOException, ServletException {
        StoreCandidate store = new ValidateStore();
        PowerMockito.mockStatic(PsqlCandidate.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(PsqlCandidate.instOf()).thenReturn(store);
        when(request.getParameter("id")).thenReturn("0");
        when(request.getParameter("name")).thenReturn("Kirill");
        new CandidateServlet().doPost(request, response);
        assertThat(store.findAllCandidates().iterator().next().getName(), is("Kirill"));
    }
}