package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import nl.fontys.s3.ticketmaster.business.exception.InvalidCompetitionException;
import nl.fontys.s3.ticketmaster.domain.competition.CreateCompetitionRequest;
import nl.fontys.s3.ticketmaster.domain.competition.UpdateCompetitionRequest;
import nl.fontys.s3.ticketmaster.persitence.CompetitionRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionValidatorImplTest {

    @Mock
    private CompetitionRepository competitionRepositoryMock;

    @InjectMocks
    private CompetitionValidatorImpl competitionValidator;

    @Test
    void validateCreateCompetitionRequest_shouldPassForValidRequest() {
        CreateCompetitionRequest request = new CreateCompetitionRequest("New Competition");
        when(competitionRepositoryMock.existsByCompetitionName("New Competition")).thenReturn(false);
        assertDoesNotThrow(() -> competitionValidator.validateCreateCompetitionRequest(request));
    }

    @Test
    void validateCreateCompetitionRequest_shouldThrowExceptionForNullName() {
        CreateCompetitionRequest request = new CreateCompetitionRequest(null);
        assertThrows(InvalidCompetitionException.class, () -> competitionValidator.validateCreateCompetitionRequest(request));
    }

    @Test
    void validateCreateCompetitionRequest_shouldThrowExceptionForExistingName() {
        CreateCompetitionRequest request = new CreateCompetitionRequest("Existing Competition");
        when(competitionRepositoryMock.existsByCompetitionName("Existing Competition")).thenReturn(true);
        assertThrows(InvalidCompetitionException.class, () -> competitionValidator.validateCreateCompetitionRequest(request));
    }



    @Test
    void validateUpdateCompetitionRequest_shouldFailWhenCompetitionDoesNotExist() {
        // Arrange
        UpdateCompetitionRequest request = new UpdateCompetitionRequest("Updated Competition", 1L);
        when(competitionRepositoryMock.existsById(1L)).thenReturn(false);

        // Act & Assert
        InvalidCompetitionException exception = assertThrows(InvalidCompetitionException.class,
                () -> competitionValidator.validateUpdateCompetitionRequest(request, 1L));
        assertEquals("Competition with the given ID does not exist.", exception.getMessage());

        // Verify
        verify(competitionRepositoryMock).existsById(1L);
        verify(competitionRepositoryMock, never()).existsByCompetitionName(any());
    }









    @Test
    void validateUpdateCompetitionRequest_shouldThrowExceptionForNonExistingCompetition() {
        UpdateCompetitionRequest request = new UpdateCompetitionRequest("Updated Competition", 1L);
        when(competitionRepositoryMock.existsById(1L)).thenReturn(false);
        assertThrows(InvalidCompetitionException.class, () -> competitionValidator.validateUpdateCompetitionRequest(request, 1L));
    }

    @Test
    void validateCompetitionExists_shouldPassForExistingCompetition() {
        when(competitionRepositoryMock.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> competitionValidator.validateCompetitionExists(1L));
    }

    @Test
    void validateCompetitionExists_shouldThrowExceptionForNonExistingCompetition() {
        when(competitionRepositoryMock.existsById(1L)).thenReturn(false);
        assertThrows(InvalidCompetitionException.class, () -> competitionValidator.validateCompetitionExists(1L));
    }

    @Test
    void validateCompetition_shouldPassForExistingCompetition() {
        CompetitionEntity existingCompetition = CompetitionEntity.builder()
                .id(1L)
                .competitionName("Existing Competition")
                .build();
        when(competitionRepositoryMock.findByCompetitionName("Existing Competition")).thenReturn(Optional.ofNullable(existingCompetition));
        assertDoesNotThrow(() -> competitionValidator.validateCompetition("Existing Competition"));
    }

    @Test
    void validateCompetition_shouldThrowExceptionForNonExistingCompetition() {
        when(competitionRepositoryMock.findByCompetitionName("Non-existing Competition")).thenReturn(Optional.empty());
        assertThrows(InvalidCompetitionException.class,
                () -> competitionValidator.validateCompetition("Non-existing Competition"));
    }
}