package operator.services;

import org.springframework.security.core.Authentication;

public class RoleService {

    public boolean adminAccess(Authentication auth) {
        return auth.getAuthorities().toString().contains("ADMIN");
    }

    public boolean sellerAccess(Authentication auth) {
        return auth.getAuthorities().toString().contains("SELLER");
    }

}
