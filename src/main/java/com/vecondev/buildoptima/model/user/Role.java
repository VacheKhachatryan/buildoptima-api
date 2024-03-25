package com.vecondev.buildoptima.model.user;

import static com.vecondev.buildoptima.model.user.Authority.RESOURCE_READ;
import static com.vecondev.buildoptima.model.user.Authority.RESOURCE_WRITE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
  ADMIN(Set.of(RESOURCE_WRITE)),
  MODERATOR(Set.of(RESOURCE_WRITE)),
  CLIENT(Set.of(RESOURCE_READ));

  private static final String ROLE_PREFIX = "ROLE_";
  private final Set<Authority> authorities;

  Role(Set<Authority> authorities) {
    this.authorities = authorities;
  }

  public List<SimpleGrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

    authorities.forEach(
        authority ->
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.name().toLowerCase())));
    grantedAuthorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + this.name()));

    return grantedAuthorities;
  }
}
