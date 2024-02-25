package me.heesu.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends HttpServlet {

    /**
     * 최초 요청을 받을때 1회 init (warmup)
     */
    @Override
    public void init() throws ServletException{
        System.out.println("init");
    }

    /**
     * 클라이언트의 요청 당 별도의 쓰레드로 처리하고 이때 서블릿 인스턴스의 service() 호출
     *  - service()는 들어온 http요청에 맞는 메서드(ex. doGet(), doPost())로 처리 위임
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        System.out.println("doGet called.");

        res.getWriter().write("Hello Servlet");
    }

    /**
     * 서블릿 컨테이너(ex. 톰캣)의 판단에 따라 해당 서블릿을 메모리에서 내려야 하는 시점에 호출
     */
    @Override
    public void destroy(){
        System.out.println("destroy");
    }
}
