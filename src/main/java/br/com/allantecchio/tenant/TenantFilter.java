package br.com.allantecchio.tenant;

import br.com.allantecchio.security.CryptoService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import br.com.allantecchio.tenant.TenantRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TenantFilter extends OncePerRequestFilter {

    private final TenantRepository repo;
    private final CryptoService crypto;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/admin") || path.startsWith("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws IOException, ServletException {

        String tenantId = req.getHeader("X-Enterprise-Id");

        if (tenantId == null || tenantId.isBlank()) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "X-Enterprise-Id Required");
            return;
        }

        TenantDb db = repo.findByTenantId(tenantId);
        if (db == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Enterprise not Found");
            return;
        }

        try {
            String decryptedPass = crypto.decrypt(db.getPass());

            TenantContext.set(new TenantDb(
                    db.getUrl(),
                    db.getUser(),
                    decryptedPass
            ));

            chain.doFilter(req, res);

        } finally {
            TenantContext.clear();
        }
    }

}

