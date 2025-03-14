package nl.fontys.s3.ticketmaster.configuration.security.token.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nl.fontys.s3.ticketmaster.configuration.security.token.AccessToken;
import nl.fontys.s3.ticketmaster.domain.user.Role;

@EqualsAndHashCode
@Getter
public class AccessTokenImpl implements AccessToken {
    private final String subject; // email
    private final Long userId;
    private final Role role;

    public AccessTokenImpl(String subject, Long userId, Role role) {
        this.subject = subject;
        this.userId = userId;
        this.role = role;
    }
}
