package nl.fontys.s3.ticketmaster.configuration.security.token.impl;

import nl.fontys.s3.ticketmaster.domain.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccessTokenImplTest {

    private AccessTokenImpl accessToken;
    private static final String SUBJECT = "test@example.com";
    private static final Long USER_ID = 1L;
    private static final Role ROLE = Role.ADMIN;

    @BeforeEach
    void setUp() {
        accessToken = new AccessTokenImpl(SUBJECT, USER_ID, ROLE);
    }

    @Test
    void testEquals() {
        // Test equal objects
        AccessTokenImpl sameToken = new AccessTokenImpl(SUBJECT, USER_ID, ROLE);
        assertEquals(accessToken, sameToken);
        assertEquals(sameToken, accessToken);

        // Test same object
        assertEquals(accessToken, accessToken);

        // Test null
        assertNotEquals(null, accessToken);

        // Test different class
        assertNotEquals(new Object(), accessToken);

        // Test different values
        AccessTokenImpl differentSubject = new AccessTokenImpl("other@example.com", USER_ID, ROLE);
        AccessTokenImpl differentUserId = new AccessTokenImpl(SUBJECT, 2L, ROLE);
        AccessTokenImpl differentRole = new AccessTokenImpl(SUBJECT, USER_ID, Role.USER);

        assertNotEquals(accessToken, differentSubject);
        assertNotEquals(accessToken, differentUserId);
        assertNotEquals(accessToken, differentRole);
    }

    @Test
    void canEqual() {
        // Test with same class
        AccessTokenImpl otherToken = new AccessTokenImpl(SUBJECT, USER_ID, ROLE);
        assertTrue(accessToken.canEqual(otherToken));

        // Test with different class
        assertFalse(accessToken.canEqual(new Object()));
    }

    @Test
    void testHashCode() {
        // Test same objects have same hashCode
        AccessTokenImpl sameToken = new AccessTokenImpl(SUBJECT, USER_ID, ROLE);
        assertEquals(accessToken.hashCode(), sameToken.hashCode());

        // Test different objects have different hashCodes
        AccessTokenImpl differentToken = new AccessTokenImpl("other@example.com", 2L, Role.USER);
        assertNotEquals(accessToken.hashCode(), differentToken.hashCode());

        // Test consistency
        int firstHashCode = accessToken.hashCode();
        int secondHashCode = accessToken.hashCode();
        assertEquals(firstHashCode, secondHashCode);
    }

    @Test
    void getSubject() {
        assertEquals(SUBJECT, accessToken.getSubject());

        // Test with null subject
        AccessTokenImpl nullSubjectToken = new AccessTokenImpl(null, USER_ID, ROLE);
        assertNull(nullSubjectToken.getSubject());
    }

    @Test
    void getUserId() {
        assertEquals(USER_ID, accessToken.getUserId());

        // Test with null userId
        AccessTokenImpl nullUserIdToken = new AccessTokenImpl(SUBJECT, null, ROLE);
        assertNull(nullUserIdToken.getUserId());
    }

    @Test
    void getRole() {
        assertEquals(ROLE, accessToken.getRole());

        // Test with null role
        AccessTokenImpl nullRoleToken = new AccessTokenImpl(SUBJECT, USER_ID, null);
        assertNull(nullRoleToken.getRole());
    }

    @Test
    void constructor_WithAllNullValues() {
        AccessTokenImpl nullToken = new AccessTokenImpl(null, null, null);
        assertNull(nullToken.getSubject());
        assertNull(nullToken.getUserId());
        assertNull(nullToken.getRole());
    }

    @Test
    void equalsAndHashCode_Consistency() {
        AccessTokenImpl token1 = new AccessTokenImpl(SUBJECT, USER_ID, ROLE);
        AccessTokenImpl token2 = new AccessTokenImpl(SUBJECT, USER_ID, ROLE);

        // If two objects are equal, their hash codes should be equal
        assertEquals(token1, token2);
        assertEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    void equals_Symmetry() {
        AccessTokenImpl token1 = new AccessTokenImpl(SUBJECT, USER_ID, ROLE);
        AccessTokenImpl token2 = new AccessTokenImpl(SUBJECT, USER_ID, ROLE);

        // Test symmetry of equals
        assertEquals(token1, token2);
        assertEquals(token2, token1);
    }

    @Test
    void equals_Transitivity() {
        AccessTokenImpl token1 = new AccessTokenImpl(SUBJECT, USER_ID, ROLE);
        AccessTokenImpl token2 = new AccessTokenImpl(SUBJECT, USER_ID, ROLE);
        AccessTokenImpl token3 = new AccessTokenImpl(SUBJECT, USER_ID, ROLE);

        // Test transitivity of equals
        assertEquals(token1, token2);
        assertEquals(token2, token3);
        assertEquals(token1, token3);
    }
}