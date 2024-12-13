package org.logesh.jobportal.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check session for user attributes
        String uri = request.getRequestURI();

        // Allow access to public pages
        if (uri.equals("/") || uri.equals("/signup") || uri.startsWith("/css/") || uri.startsWith("/js/")) {
            return true;
        }

        // Check if user is logged in
        if (request.getSession().getAttribute("studentEmail") != null ||
                request.getSession().getAttribute("recruiterEmail") != null ||
                request.getSession().getAttribute("adminEmail") != null) {
            return true; // User is authenticated
        }

        // Redirect to login page if not authenticated
        response.sendRedirect("/");
        return false;
    }
}
