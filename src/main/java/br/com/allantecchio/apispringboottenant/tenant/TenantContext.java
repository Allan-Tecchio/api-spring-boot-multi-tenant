package br.com.allantecchio.apispringboottenant.tenant;

public class TenantContext {

    private static final ThreadLocal<TenantDb> CURRENT = new ThreadLocal<>();

    public static void set(TenantDb db) {
        CURRENT.set(db);
    }

    public static TenantDb get() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }
}
