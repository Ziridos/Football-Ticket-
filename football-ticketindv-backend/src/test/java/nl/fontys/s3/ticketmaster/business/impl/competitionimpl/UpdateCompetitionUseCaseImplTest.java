package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionValidator;
import nl.fontys.s3.ticketmaster.domain.competition.UpdateCompetitionRequest;
import nl.fontys.s3.ticketmaster.domain.competition.UpdateCompetitionResponse;
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
class UpdateCompetitionUseCaseImplTest {

    @Mock
    private CompetitionRepository competitionRepositoryMock;
    @Mock
    private CompetitionConverter competitionConverterMock;
    @Mock
    private CompetitionValidator competitionValidatorMock;

    @InjectMocks
    private UpdateCompetitionUseCaseImpl updateCompetitionUseCase;

    @Test
    void updateCompetition_shouldUpdateCompetitionSuccessfully() {
        // Arrange
        Long competitionId = 1L;
        UpdateCompetitionRequest request = UpdateCompetitionRequest.builder()
                .competitionName("Updated Competition")
                .competitionId(competitionId)
                .build();
        CompetitionEntity existingCompetition = CompetitionEntity.builder()
                .id(competitionId)
                .competitionName("Old Competition")
                .build();
        CompetitionEntity updatedCompetition = CompetitionEntity.builder()
                .id(competitionId)
                .competitionName("Updated Competition")
                .build();
        UpdateCompetitionResponse expectedResponse = UpdateCompetitionResponse.builder()
                .competitionId(competitionId)
                .competitionName("Updated Competition")
                .build();

        when(competitionRepositoryMock.findById(competitionId)).thenReturn(Optional.ofNullable(existingCompetition));
        when(competitionConverterMock.updateEntityFromRequest(existingCompetition, request)).thenReturn(updatedCompetition);
        when(competitionRepositoryMock.save(updatedCompetition)).thenReturn(updatedCompetition);
        when(competitionConverterMock.convertToUpdateCompetitionResponse(updatedCompetition)).thenReturn(expectedResponse);

        // Act
        UpdateCompetitionResponse response = updateCompetitionUseCase.updateCompetition(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(competitionValidatorMock).validateUpdateCompetitionRequest(request, competitionId);
        verify(competitionRepositoryMock).findById(competitionId);
        verify(competitionConverterMock).updateEntityFromRequest(existingCompetition, request);
        verify(competitionRepositoryMock).save(updatedCompetition);
        verify(competitionConverterMock).convertToUpdateCompetitionResponse(updatedCompetition);
    }
}