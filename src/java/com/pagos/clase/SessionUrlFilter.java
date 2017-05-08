/*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package com.pagos.clase;



import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author KevinArnold
 */
@WebFilter("*.xhtml")
public class SessionUrlFilter implements Filter {

    private Session session;
    private Transaction transaccion;

    FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);

        String requestUrl = req.getRequestURL().toString();

        String[] urlPermitidaSinSesion = new String[]{
            "faces/index.xhtml"
        };

        String[] urlPermitidaSinRol = new String[]{
//            "faces/administrador/compras/registrarcompras.xhtml",
//            "faces/administrador/productos/registrarProductos.xhtml"
        };

        boolean redireccionarPeticion;

        if (session.getAttribute("correoElectronico") == null) {
            redireccionarPeticion = true;
            for (String item : urlPermitidaSinSesion) {
                if (requestUrl.contains(item)) {
                    redireccionarPeticion = false;
                    break;
                }
            }
        } else {

            redireccionarPeticion = false;
        }

        if (redireccionarPeticion) {
            res.sendRedirect(req.getContextPath() + "/faces/index.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
