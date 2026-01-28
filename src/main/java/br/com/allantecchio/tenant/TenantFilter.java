package br.com.allantecchio.tenant;

import br.com.allantecchio.security.CryptoService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import br.com.allantecchio.tenant.TenantRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TenantFilter implements Filter {

    private final TenantRepository repo;
    private final CryptoService crypto;

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String tenantId = req.getHeader("X-Tenant-Id");
        if (tenantId == null) {
            throw new RuntimeException("Tenant não informado");
        }

        TenantDb db = repo.findByTenantId(tenantId);

        String decryptedPass = crypto.decrypt(db.getPass());

        TenantContext.set(new TenantDb(
                db.getUrl(),
                db.getUser(),
                decryptedPass
        ));

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
