package nl.fontys.s3.ticketmaster.business.impl.competitionimpl;

import nl.fontys.s3.ticketmaster.domain.competition.*;
import nl.fontys.s3.ticketmaster.persitence.entity.CompetitionEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompetitionConverterImplTest {

    private final CompetitionConverterImpl competitionConverter = new CompetitionConverterImpl();

    @Test
    void convertToEntity_shouldConvertCreateRequestToEntity() {
        CreateCompetitionRequest request = new CreateCompetitionRequest("Test Competition");
        CompetitionEntity result = competitionConverter.convertToEntity(request);
        assertEquals("Test Competition", result.getCompetitionName());
    }

    @Test
    void convertToCreateCompetitionResponse_shouldConvertEntityToCreateResponse() {
        CompetitionEntity entity = CompetitionEntity.builder()
                .id(1L)
                .competitionName("Test Competition")
                .build();
        CreateCompetitionResponse result = competitionConverter.convertToCreateCompetitionResponse(entity);
        assertEquals(1L, result.getCompetitionId());
        assertEquals("Test Competition", result.getCompetitionName());
    }

    @Test
    void convertToGetCompetitionResponse_shouldConvertEntityToGetResponse() {
        CompetitionEntity entity = CompetitionEntity.builder()
                .id(1L)
                .competitionName("Test Competition")
                .build();
        GetCompetitionResponse result = competitionConverter.convertToGetCompetitionResponse(entity);
        assertEquals(1L, result.getCompetitionId());
        assertEquals("Test Competition", result.getCompetitionName());
    }

    @Test
    void convertToUpdateCompetitionResponse_shouldConvertEntityToUpdateResponse() {
        CompetitionEntity entity = CompetitionEntity.builder()
                .id(1L)
                .competitionName("Test Competition")
                .build();
        UpdateCompetitionResponse result = competitionConverter.convertToUpdateCompetitionResponse(entity);
        assertEquals(1L, result.getCompetitionId());
        assertEquals("Test Competition", result.getCompetitionName());
    }

    @Test
    void convertToCompetition_shouldConvertEntityToCompetition() {
        CompetitionEntity entity = CompetitionEntity.builder()
                .id(1L)
                .competitionName("Test Competition")
                .build();
        Competition result = competitionConverter.convertToCompetition(entity);
        assertEquals(1L, result.getCompetitionId());
        assertEquals("Test Competition", result.getCompetitionName());
    }

    @Test
    void updateEntityFromRequest_shouldUpdateEntityWithRequestData() {
        CompetitionEntity entity = CompetitionEntity.builder()
                .id(1L)
                .competitionName("Old Competition")
                .build();

        UpdateCompetitionRequest request = UpdateCompetitionRequest.builder()
                .competitionId(1L)
                .competitionName("Updated Competition")
                .build();

        CompetitionEntity result = competitionConverter.updateEntityFromRequest(entity, request);
        assertEquals(1L, result.getId());
        assertEquals("Updated Competition", result.getCompetitionName());
    }
}