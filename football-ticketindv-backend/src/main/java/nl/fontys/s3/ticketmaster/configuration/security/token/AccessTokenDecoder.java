package nl.fontys.s3.ticketmaster.configuration.security.token;

public interface AccessTokenDecoder {
    AccessToken decode(String accessTokenEncoded);
}
