package br.com.allantecchio.tenant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TenantDb {
    private String url;
    private String user;
    private String pass;
}
