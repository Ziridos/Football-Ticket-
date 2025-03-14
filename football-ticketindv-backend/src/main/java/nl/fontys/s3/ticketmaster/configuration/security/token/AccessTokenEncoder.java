package nl.fontys.s3.ticketmaster.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
