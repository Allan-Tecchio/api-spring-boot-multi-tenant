package br.com.allantecchio.apispringboottenant.tenant;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TenantFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String url  = req.getHeader("X-Tenant-Db-Url");
        String user = req.getHeader("X-Tenant-Db-User");
        String pass = req.getHeader("X-Tenant-Db-Pass");

        if (url == null || user == null || pass == null) {
            throw new RuntimeException("Headers de banco não informados");
        }

        TenantContext.set(new TenantDb(url, user, pass));

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
