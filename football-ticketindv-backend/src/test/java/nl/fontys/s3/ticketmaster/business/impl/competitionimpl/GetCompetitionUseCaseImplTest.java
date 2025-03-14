package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionConverter;
import nl.fontys.s3.ticketmaster.business.interfaces.competitioninterfaces.CompetitionValidator;
import nl.fontys.s3.ticketmaster.domain.competition.GetCompetitionRequest;
import nl.fontys.s3.ticketmaster.domain.competition.GetCompetitionResponse;
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
class GetCompetitionUseCaseImplTest {

    @Mock
    private CompetitionRepository competitionRepositoryMock;
    @Mock
    private CompetitionConverter competitionConverterMock;
    @Mock
    private CompetitionValidator competitionValidatorMock;

    @InjectMocks
    private GetCompetitionUseCaseImpl getCompetitionUseCase;

    @Test
    void getCompetition_shouldReturnCompetition() {
        // Arrange
        Long competitionId = 1L;
        GetCompetitionRequest request = GetCompetitionRequest.builder()
                .competitionId(competitionId)
                .build();
        CompetitionEntity competitionEntity = CompetitionEntity.builder()
                .id(competitionId)
                .competitionName("Test Competition")
                .build();
        GetCompetitionResponse expectedResponse = GetCompetitionResponse.builder()
                .competitionName("Test Competition")
                .competitionId(competitionId)
                .build();

        when(competitionRepositoryMock.findById(competitionId)).thenReturn(Optional.ofNullable(competitionEntity));
        when(competitionConverterMock.convertToGetCompetitionResponse(competitionEntity)).thenReturn(expectedResponse);

        // Act
        GetCompetitionResponse response = getCompetitionUseCase.getCompetition(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(competitionValidatorMock).validateCompetitionExists(competitionId);
        verify(competitionRepositoryMock).findById(competitionId);
        verify(competitionConverterMock).convertToGetCompetitionResponse(competitionEntity);
    }
}