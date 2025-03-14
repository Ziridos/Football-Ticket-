package nl.fontys.s3.ticketmaster.business.impl.stadiumimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.stadiuminterfaces.StadiumConverter;
import nl.fontys.s3.ticketmaster.domain.stadium.GetAllStadiumsRequest;
import nl.fontys.s3.ticketmaster.domain.stadium.GetAllStadiumsResponse;
import nl.fontys.s3.ticketmaster.domain.stadium.Stadium;
import nl.fontys.s3.ticketmaster.persitence.StadiumRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.StadiumEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllStadiumsUseCaseImplTest {

    @Mock
    private StadiumRepository stadiumRepositoryMock;

    @Mock
    private StadiumConverter stadiumConverterMock;

    @InjectMocks
    private GetAllStadiumsUseCaseImpl getAllStadiumsUseCase;

    @Test
    void getAllStadiums_WithDefaultParameters_shouldReturnAllStadiums() {
        // Arrange
        GetAllStadiumsRequest request = GetAllStadiumsRequest.builder()
                .page(0)
                .size(10)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        List<StadiumEntity> stadiumEntities = List.of(
                StadiumEntity.builder()
                        .id(1L)
                        .stadiumName("Stadium 1")
                        .stadiumAddress("Address 1")
                        .stadiumPostalCode("12345")
                        .stadiumCity("City 1")
                        .stadiumCountry("Country 1")
                        .build(),
                StadiumEntity.builder()
                        .id(2L)
                        .stadiumName("Stadium 2")
                        .stadiumAddress("Address 2")
                        .stadiumPostalCode("67890")
                        .stadiumCity("City 2")
                        .stadiumCountry("Country 2")
                        .build()
        );

        List<Stadium> stadiums = List.of(
                Stadium.builder()
                        .stadiumId(1L)
                        .stadiumName("Stadium 1")
                        .stadiumAddress("Address 1")
                        .stadiumPostalCode("12345")
                        .stadiumCity("City 1")
                        .stadiumCountry("Country 1")
                        .build(),
                Stadium.builder()
                        .stadiumId(2L)
                        .stadiumName("Stadium 2")
                        .stadiumAddress("Address 2")
                        .stadiumPostalCode("67890")
                        .stadiumCity("City 2")
                        .stadiumCountry("Country 2")
                        .build()
        );

        Page<StadiumEntity> pageResponse = new PageImpl<>(stadiumEntities, PageRequest.of(0, 10), 2);

        when(stadiumRepositoryMock.findByFilters(
                eq(null), eq(null), eq(null),
                any(Pageable.class))).thenReturn(pageResponse);
        when(stadiumConverterMock.convertToDomain(stadiumEntities.get(0))).thenReturn(stadiums.get(0));
        when(stadiumConverterMock.convertToDomain(stadiumEntities.get(1))).thenReturn(stadiums.get(1));

        // Act
        GetAllStadiumsResponse response = getAllStadiumsUseCase.getAllStadiums(request);

        // Assert
        assertNotNull(response);
        assertEquals(stadiums, response.getStadiums());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        verify(stadiumConverterMock, times(2)).convertToDomain(any(StadiumEntity.class));
    }

    @Test
    void getAllStadiums_WithFilters_shouldReturnFilteredStadiums() {
        // Arrange
        GetAllStadiumsRequest request = GetAllStadiumsRequest.builder()
                .name("Emirates")
                .city("London")
                .country("England")
                .page(0)
                .size(10)
                .sortBy("stadiumName")
                .sortDirection("DESC")
                .build();

        StadiumEntity stadiumEntity = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Emirates Stadium")
                .stadiumAddress("Highbury House")
                .stadiumPostalCode("N5 1BU")
                .stadiumCity("London")
                .stadiumCountry("England")
                .build();

        Stadium stadium = Stadium.builder()
                .stadiumId(1L)
                .stadiumName("Emirates Stadium")
                .stadiumAddress("Highbury House")
                .stadiumPostalCode("N5 1BU")
                .stadiumCity("London")
                .stadiumCountry("England")
                .build();

        Page<StadiumEntity> pageResponse = new PageImpl<>(List.of(stadiumEntity), PageRequest.of(0, 10), 1);

        when(stadiumRepositoryMock.findByFilters(
                eq("Emirates"), eq("London"), eq("England"),
                any(Pageable.class))).thenReturn(pageResponse);
        when(stadiumConverterMock.convertToDomain(stadiumEntity)).thenReturn(stadium);

        // Act
        GetAllStadiumsResponse response = getAllStadiumsUseCase.getAllStadiums(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getStadiums().size());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        verify(stadiumConverterMock).convertToDomain(any(StadiumEntity.class));
    }

    @Test
    void getAllStadiums_WithPagination_shouldReturnPagedResults() {
        // Arrange
        GetAllStadiumsRequest request = GetAllStadiumsRequest.builder()
                .page(1)
                .size(2)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        List<StadiumEntity> stadiumEntities = List.of(
                StadiumEntity.builder()
                        .id(3L)
                        .stadiumName("Stadium 3")
                        .stadiumCity("City 3")
                        .build(),
                StadiumEntity.builder()
                        .id(4L)
                        .stadiumName("Stadium 4")
                        .stadiumCity("City 4")
                        .build()
        );

        List<Stadium> stadiums = List.of(
                Stadium.builder()
                        .stadiumId(3L)
                        .stadiumName("Stadium 3")
                        .stadiumCity("City 3")
                        .build(),
                Stadium.builder()
                        .stadiumId(4L)
                        .stadiumName("Stadium 4")
                        .stadiumCity("City 4")
                        .build()
        );

        Page<StadiumEntity> pageResponse = new PageImpl<>(stadiumEntities, PageRequest.of(1, 2), 5);

        when(stadiumRepositoryMock.findByFilters(
                eq(null), eq(null), eq(null),
                any(Pageable.class))).thenReturn(pageResponse);
        when(stadiumConverterMock.convertToDomain(stadiumEntities.get(0))).thenReturn(stadiums.get(0));
        when(stadiumConverterMock.convertToDomain(stadiumEntities.get(1))).thenReturn(stadiums.get(1));

        // Act
        GetAllStadiumsResponse response = getAllStadiumsUseCase.getAllStadiums(request);

        // Assert
        assertNotNull(response);
        assertEquals(stadiums, response.getStadiums());
        assertEquals(5, response.getTotalElements());
        assertEquals(3, response.getTotalPages());
        assertEquals(1, response.getCurrentPage());
        verify(stadiumConverterMock, times(2)).convertToDomain(any(StadiumEntity.class));
    }

    @Test
    void getAllStadiums_WithLocationFilter_shouldReturnStadiumsInLocation() {
        // Arrange
        GetAllStadiumsRequest request = GetAllStadiumsRequest.builder()
                .city("Amsterdam")
                .country("Netherlands")
                .page(0)
                .size(10)
                .sortBy("stadiumName")
                .sortDirection("ASC")
                .build();

        StadiumEntity stadiumEntity = StadiumEntity.builder()
                .id(1L)
                .stadiumName("Johan Cruijff Arena")
                .stadiumCity("Amsterdam")
                .stadiumCountry("Netherlands")
                .build();

        Stadium stadium = Stadium.builder()
                .stadiumId(1L)
                .stadiumName("Johan Cruijff Arena")
                .stadiumCity("Amsterdam")
                .stadiumCountry("Netherlands")
                .build();

        Page<StadiumEntity> pageResponse = new PageImpl<>(List.of(stadiumEntity), PageRequest.of(0, 10), 1);

        when(stadiumRepositoryMock.findByFilters(
                eq(null), eq("Amsterdam"), eq("Netherlands"),
                any(Pageable.class))).thenReturn(pageResponse);
        when(stadiumConverterMock.convertToDomain(stadiumEntity)).thenReturn(stadium);

        // Act
        GetAllStadiumsResponse response = getAllStadiumsUseCase.getAllStadiums(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getStadiums().size());
        assertEquals(1, response.getTotalElements());
        assertEquals("Amsterdam", response.getStadiums().get(0).getStadiumCity());
        assertEquals("Netherlands", response.getStadiums().get(0).getStadiumCountry());
        verify(stadiumConverterMock).convertToDomain(any(StadiumEntity.class));
    }
}