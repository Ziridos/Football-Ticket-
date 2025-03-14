package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidStadiumException;
import nl.fontys.s3.ticketmaster.domain.stadium.CreateStadiumRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.UpdateStadiumRequest;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StadiumValidatorImplTest {

    @Mock
    private StadiumRepository stadiumRepositoryMock;

    @InjectMocks
    private StadiumValidatorImpl stadiumValidator;

    @Test
    void validateCreateStadiumRequest_withValidRequest_shouldNotThrowException() {
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("Valid Stadium")
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("City")
                .stadiumCountry("Country")
                .build();

        when(stadiumRepositoryMock.existsByStadiumName("Valid Stadium")).thenReturn(false);

        assertDoesNotThrow(() -> stadiumValidator.validateCreateStadiumRequest(request));
        verify(stadiumRepositoryMock).existsByStadiumName("Valid Stadium");
    }

    @Test
    void validateCreateStadiumRequest_withNullRequest_shouldThrowException() {
        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateCreateStadiumRequest(null));
        assertEquals("Stadium request cannot be null", exception.getMessage());
    }

    @Test
    void validateCreateStadiumRequest_withExistingName_shouldThrowException() {
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("Existing Stadium")
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("City")
                .stadiumCountry("Country")
                .build();

        when(stadiumRepositoryMock.existsByStadiumName("Existing Stadium")).thenReturn(true);

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateCreateStadiumRequest(request));
        assertEquals("Stadium with this name already exists", exception.getMessage());
        verify(stadiumRepositoryMock).existsByStadiumName("Existing Stadium");
    }

    @Test
    void validateCreateStadiumRequest_withInvalidPostalCode_shouldThrowException() {
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("Valid Stadium")
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("Invalid")
                .stadiumCity("City")
                .stadiumCountry("Country")
                .build();

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateCreateStadiumRequest(request));
        assertEquals("Invalid postal code format", exception.getMessage());
    }

    @Test
    void validateStadiumExists_withExistingId_shouldNotThrowException() {
        Long id = 1L;
        when(stadiumRepositoryMock.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> stadiumValidator.validateStadiumExists(id));
        verify(stadiumRepositoryMock).existsById(id);
    }

    @Test
    void validateStadiumExists_withNonExistingId_shouldThrowException() {
        Long id = 1L;
        when(stadiumRepositoryMock.existsById(id)).thenReturn(false);

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateStadiumExists(id));
        assertEquals("Stadium not found", exception.getMessage());
        verify(stadiumRepositoryMock).existsById(id);
    }

    @Test
    void validateUpdateStadiumRequest_withValidRequest_shouldNotThrowException() {
        Long id = 1L;
        String stadiumName = "Updated Stadium";
        UpdateStadiumRequest request = UpdateStadiumRequest.builder()
                .stadiumName(stadiumName)
                .stadiumAddress("456 Stadium Ave")
                .stadiumPostalCode("5678 CD")
                .stadiumCity("New City")
                .stadiumCountry("New Country")
                .build();

        StadiumEntity existingStadium = StadiumEntity.builder()
                .id(id)
                .stadiumName("Old Stadium Name")
                .build();

        when(stadiumRepositoryMock.existsById(id)).thenReturn(true);
        when(stadiumRepositoryMock.findById(id)).thenReturn(Optional.of(existingStadium));
        when(stadiumRepositoryMock.existsByStadiumName(stadiumName)).thenReturn(false);

        assertDoesNotThrow(() -> stadiumValidator.validateUpdateStadiumRequest(id, request));

        verify(stadiumRepositoryMock).existsById(id);
        verify(stadiumRepositoryMock).findById(id);
        verify(stadiumRepositoryMock).existsByStadiumName(stadiumName);
    }

    @Test
    void validateUpdateStadiumRequest_withNullRequest_shouldThrowException() {
        Long id = 1L;
        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateUpdateStadiumRequest(id, null));
        assertEquals("Update request cannot be null", exception.getMessage());
    }

    @Test
    void validateUpdateStadiumRequest_withNonExistingId_shouldThrowException() {
        Long id = 1L;
        UpdateStadiumRequest request = UpdateStadiumRequest.builder()
                .stadiumName("Updated Stadium")
                .stadiumAddress("456 Stadium Ave")
                .stadiumPostalCode("5678 CD")
                .stadiumCity("New City")
                .stadiumCountry("New Country")
                .build();

        when(stadiumRepositoryMock.existsById(id)).thenReturn(false);

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateUpdateStadiumRequest(id, request));
        assertEquals("Stadium not found", exception.getMessage());
        verify(stadiumRepositoryMock).existsById(id);
    }

    @Test
    void validateUpdateStadiumRequest_withExistingName_shouldThrowException() {
        Long id = 1L;
        String existingStadiumName = "Existing Stadium";
        UpdateStadiumRequest request = UpdateStadiumRequest.builder()
                .stadiumName(existingStadiumName)
                .stadiumAddress("456 Stadium Ave")
                .stadiumPostalCode("5678 CD")
                .stadiumCity("New City")
                .stadiumCountry("New Country")
                .build();

        StadiumEntity existingStadium = StadiumEntity.builder()
                .id(id)
                .stadiumName("Current Stadium")  // Different name than the update request
                .build();

        when(stadiumRepositoryMock.existsById(id)).thenReturn(true);
        when(stadiumRepositoryMock.findById(id)).thenReturn(Optional.of(existingStadium));
        when(stadiumRepositoryMock.existsByStadiumName(existingStadiumName)).thenReturn(true);

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateUpdateStadiumRequest(id, request));
        assertEquals("Stadium with this name already exists", exception.getMessage());

        verify(stadiumRepositoryMock).existsById(id);
        verify(stadiumRepositoryMock).findById(id);
        verify(stadiumRepositoryMock).existsByStadiumName(existingStadiumName);
    }

    @Test
    void validateCreateStadiumRequest_withEmptyName_shouldThrowException() {
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("  ")  // Empty with spaces
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("City")
                .stadiumCountry("Country")
                .build();

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateCreateStadiumRequest(request));
        assertEquals("Stadium name cannot be empty", exception.getMessage());
    }

    @Test
    void validateCreateStadiumRequest_withNullName_shouldThrowException() {
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName(null)
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("City")
                .stadiumCountry("Country")
                .build();

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateCreateStadiumRequest(request));
        assertEquals("Stadium name cannot be empty", exception.getMessage());
    }

    @Test
    void validateCreateStadiumRequest_withEmptyAddress_shouldThrowException() {
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("Valid Stadium")
                .stadiumAddress("  ")  // Empty with spaces
                .stadiumPostalCode("1234 AB")
                .stadiumCity("City")
                .stadiumCountry("Country")
                .build();

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateCreateStadiumRequest(request));
        assertEquals("Stadium address cannot be empty", exception.getMessage());
    }

    @Test
    void validateCreateStadiumRequest_withNullAddress_shouldThrowException() {
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("Valid Stadium")
                .stadiumAddress(null)
                .stadiumPostalCode("1234 AB")
                .stadiumCity("City")
                .stadiumCountry("Country")
                .build();

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateCreateStadiumRequest(request));
        assertEquals("Stadium address cannot be empty", exception.getMessage());
    }

    @Test
    void validateCreateStadiumRequest_withEmptyCity_shouldThrowException() {
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("Valid Stadium")
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("  ")  // Empty with spaces
                .stadiumCountry("Country")
                .build();

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateCreateStadiumRequest(request));
        assertEquals("Stadium city cannot be empty", exception.getMessage());
    }

    @Test
    void validateCreateStadiumRequest_withNullCity_shouldThrowException() {
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("Valid Stadium")
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity(null)
                .stadiumCountry("Country")
                .build();

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateCreateStadiumRequest(request));
        assertEquals("Stadium city cannot be empty", exception.getMessage());
    }

    @Test
    void validateCreateStadiumRequest_withEmptyCountry_shouldThrowException() {
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("Valid Stadium")
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("City")
                .stadiumCountry("  ")  // Empty with spaces
                .build();

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateCreateStadiumRequest(request));
        assertEquals("Stadium country cannot be empty", exception.getMessage());
    }

    @Test
    void validateCreateStadiumRequest_withNullCountry_shouldThrowException() {
        CreateStadiumRequest request = CreateStadiumRequest.builder()
                .stadiumName("Valid Stadium")
                .stadiumAddress("123 Stadium St")
                .stadiumPostalCode("1234 AB")
                .stadiumCity("City")
                .stadiumCountry(null)
                .build();

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateCreateStadiumRequest(request));
        assertEquals("Stadium country cannot be empty", exception.getMessage());
    }

    @Test
    void validateUpdateStadiumRequest_withEmptyFields_shouldThrowException() {
        Long id = 1L;
        when(stadiumRepositoryMock.existsById(id)).thenReturn(true);

        UpdateStadiumRequest request = UpdateStadiumRequest.builder()
                .stadiumName("Valid Stadium")
                .stadiumAddress("  ")  // Empty address
                .stadiumPostalCode("1234 AB")
                .stadiumCity("City")
                .stadiumCountry("Country")
                .build();

        InvalidStadiumException exception = assertThrows(InvalidStadiumException.class,
                () -> stadiumValidator.validateUpdateStadiumRequest(id, request));
        assertEquals("Stadium address cannot be empty", exception.getMessage());
    }
}