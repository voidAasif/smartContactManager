package com.example.smartContactManager.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;

// import jakarta.servlet.http.HttpServletRequest;

@Component
public class SessionHelper { // custom class for removing session message;

    @SuppressWarnings("all")
    public void sessionMessageRemove() {
        System.out.println("sessionMessageRemove method execute"); // log;

        try {
            // Retrieve the current request attributes from the RequestContextHolder
            //ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            // Get the HttpServletRequest object from the request attributes
            //HttpServletRequest request = requestAttributes.getRequest();

            // Retrieve the HttpSession from the request (creates a new session if none exists)
            //HttpSession httpSession = request.getSession();

            // Retrieve the current HTTP session from the request context
            HttpSession httpSession = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            httpSession.removeAttribute("message"); // remove "message" from httpSession;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while removing session message");
        }

    }
}

/*
 * this method use like this in thymeleaf templates;
 * 
 * 
 * <th:block th:text="${@sessionHelper.sessionMessageRemove()}"></th:block>
 * 
 * 1. class name in camelCase [sessionHelper]
 * 2. method name [sessionMessageRemove()]
 */


/*
 * 1. RequestContextHolder.getRequestAttributes()
        RequestContextHolder is a Spring utility class that provides access to the request attributes of the current HTTP request.
        getRequestAttributes() returns an instance of RequestAttributes, which represents the request scope attributes.
        Since getRequestAttributes() returns a generic RequestAttributes type, we need to cast it to ServletRequestAttributes to access servlet-specific methods.

    2. (ServletRequestAttributes) RequestContextHolder.getRequestAttributes()
        ServletRequestAttributes is a subclass of RequestAttributes that specifically works with HTTP servlet requests.
        By casting RequestAttributes to ServletRequestAttributes, we gain access to methods for retrieving the HttpServletRequest.

    3. .getRequest()
        getRequest() is a method of ServletRequestAttributes that returns the current HttpServletRequest object.
        This gives us access to the request data, such as parameters, headers, session, etc.

    4. .getSession()
        getSession() is a method of HttpServletRequest that returns the HttpSession object.
        The session allows us to store and retrieve user-specific data across multiple requests.
 */