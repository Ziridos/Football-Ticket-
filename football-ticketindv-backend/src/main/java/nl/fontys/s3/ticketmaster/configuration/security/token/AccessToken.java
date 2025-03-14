package nl.fontys.s3.ticketmaster.configuration.security.token;

import nl.fontys.s3.ticketmaster.domain.user.Role;

public interface AccessToken {
    String getSubject(); // email
    Role getRole();
    Long getUserId();
}
