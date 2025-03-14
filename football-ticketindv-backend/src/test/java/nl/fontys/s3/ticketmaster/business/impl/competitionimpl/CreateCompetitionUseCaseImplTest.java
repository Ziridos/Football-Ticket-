package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionValidator;
import nl.fontys.s3.ticketmaster.domain.competition.CreateCompetitionRequest;
import nl.fontys.s3.ticketmaster.domain.competition.CreateCompetitionResponse;
import nl.fontys.s3.ticketmaster.persitence.CompetitionRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCompetitionUseCaseImplTest {

    @Mock
    private CompetitionRepository competitionRepositoryMock;
    @Mock
    private CompetitionConverter competitionConverterMock;
    @Mock
    private CompetitionValidator competitionValidatorMock;

    @InjectMocks
    private CreateCompetitionUseCaseImpl createCompetitionUseCase;

    @Test
    void createCompetition_shouldCreateAndSaveCompetitionSuccessfully() {
        // Arrange
        CreateCompetitionRequest request = CreateCompetitionRequest.builder()
                .competitionName("Test Competition")
                .build();
        CompetitionEntity competitionEntity = CompetitionEntity.builder()
                .competitionName("Test Competition")
                .build();
        CompetitionEntity savedCompetitionEntity = CompetitionEntity.builder()
                .id(1L)
                .competitionName("Test Competition")
                .build();
        CreateCompetitionResponse expectedResponse = CreateCompetitionResponse.builder()
                .competitionId(1L)
                .competitionName("Test Competition")
                .build();

        when(competitionConverterMock.convertToEntity(request)).thenReturn(competitionEntity);
        when(competitionRepositoryMock.save(competitionEntity)).thenReturn(savedCompetitionEntity);
        when(competitionConverterMock.convertToCreateCompetitionResponse(savedCompetitionEntity)).thenReturn(expectedResponse);

        // Act
        CreateCompetitionResponse response = createCompetitionUseCase.createCompetition(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(competitionValidatorMock).validateCreateCompetitionRequest(request);
        verify(competitionConverterMock).convertToEntity(request);
        verify(competitionRepositoryMock).save(competitionEntity);
        verify(competitionConverterMock).convertToCreateCompetitionResponse(savedCompetitionEntity);
    }
}