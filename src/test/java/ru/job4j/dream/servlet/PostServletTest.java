package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.store.PsqlPost;
import ru.job4j.dream.store.ValidateStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlPost.class)
public class PostServletTest {

    @Test
    public void whenCreatePost() throws Exception {
        ValidateStore store = new ValidateStore();
        PowerMockito.mockStatic(PsqlPost.class);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(PsqlPost.instOf()).thenReturn(store);
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("Java Junior");
        when(req.getParameter("description")).thenReturn("Desc");
        new PostServlet().doPost(req, resp);
        assertThat(store.findAllPosts().iterator().next().getName(), is("Java Junior"));
    }
}