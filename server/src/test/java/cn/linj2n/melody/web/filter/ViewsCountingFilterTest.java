package cn.linj2n.melody.web.filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletException;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ViewsCountingFilterTest {

    private static final String[] MOCK_INCLUDED_URI = new String[] {"/posts/1", "/archives", "/categories", "/about", "/wiki", "/index"};

    private static final String[] MOCK_EXCLUDED_URI = new String[] {"/api/v1/posts/1", "/admin/post/1"};

    @InjectMocks
    private ViewsCountingFilter viewsCountingFilter;

    @Test
    public void test_doFilterInternal() {
    }

    @Test
    public void test_shouldNotFilter() throws ServletException, IOException {

        Arrays.stream(MOCK_INCLUDED_URI).peek(uri -> {
            MockHttpServletRequest request = new MockHttpServletRequest("get", uri);
            try {
                assertEquals(false, viewsCountingFilter.shouldNotFilter(request));
            } catch (ServletException e) {
                e.printStackTrace();
            }
        });

        Arrays.stream(MOCK_EXCLUDED_URI).peek(uri -> {
            MockHttpServletRequest request = new MockHttpServletRequest("get", uri);
            try {
                assertEquals(true, viewsCountingFilter.shouldNotFilter(request));
            } catch (ServletException e) {
                e.printStackTrace();
            }
        });


    }
}