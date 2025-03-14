package nl.fontys.s3.ticketmaster.business.impl.clubimpl;

import nl.fontys.s3.ticketmaster.business.interfaces.clubinterfaces.ClubConverter;
import nl.fontys.s3.ticketmaster.domain.club.Club;
import nl.fontys.s3.ticketmaster.domain.club.GetAllClubsRequest;
import nl.fontys.s3.ticketmaster.domain.club.GetAllClubsResponse;
import nl.fontys.s3.ticketmaster.persitence.ClubRepository;
import nl.fontys.s3.ticketmaster.persitence.entity.ClubEntity;
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
class GetAllClubsUseCaseImplTest {

    @Mock
    private ClubRepository clubRepositoryMock;

    @Mock
    private ClubConverter clubConverterMock;

    @InjectMocks
    private GetAllClubsUseCaseImpl getAllClubsUseCase;

    @Test
    void getAllClubs_WithDefaultParameters_shouldReturnAllClubs() {
        // Arrange
        GetAllClubsRequest request = GetAllClubsRequest.builder()
                .page(0)
                .size(10)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        List<ClubEntity> clubEntities = List.of(
                ClubEntity.builder().id(1L).clubName("Club A").build(),
                ClubEntity.builder().id(2L).clubName("Club B").build()
        );

        List<Club> clubs = List.of(
                Club.builder().clubId(1L).clubName("Club A").build(),
                Club.builder().clubId(2L).clubName("Club B").build()
        );

        Page<ClubEntity> pageResponse = new PageImpl<>(clubEntities, PageRequest.of(0, 10), 2);

        when(clubRepositoryMock.findByFilters(
                eq(null), eq(null), eq(null), eq(null),
                any(Pageable.class))).thenReturn(pageResponse);
        when(clubConverterMock.convertToClub(clubEntities.get(0))).thenReturn(clubs.get(0));
        when(clubConverterMock.convertToClub(clubEntities.get(1))).thenReturn(clubs.get(1));

        // Act
        GetAllClubsResponse response = getAllClubsUseCase.getAllClubs(request);

        // Assert
        assertNotNull(response);
        assertEquals(clubs, response.getClubs());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        verify(clubConverterMock, times(2)).convertToClub(any(ClubEntity.class));
    }

    @Test
    void getAllClubs_WithFilters_shouldReturnFilteredClubs() {
        // Arrange
        GetAllClubsRequest request = GetAllClubsRequest.builder()
                .name("Club")
                .stadiumName("Stadium")
                .stadiumCity("City")
                .stadiumCountry("Country")
                .page(0)
                .size(10)
                .sortBy("clubName")
                .sortDirection("DESC")
                .build();

        ClubEntity clubEntity = ClubEntity.builder().id(1L).clubName("Club A").build();
        Club club = Club.builder().clubId(1L).clubName("Club A").build();
        Page<ClubEntity> pageResponse = new PageImpl<>(List.of(clubEntity), PageRequest.of(0, 10), 1);

        when(clubRepositoryMock.findByFilters(
                eq("Club"), eq("Stadium"), eq("City"), eq("Country"),
                any(Pageable.class))).thenReturn(pageResponse);
        when(clubConverterMock.convertToClub(clubEntity)).thenReturn(club);

        // Act
        GetAllClubsResponse response = getAllClubsUseCase.getAllClubs(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getClubs().size());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        verify(clubConverterMock).convertToClub(any(ClubEntity.class));
    }

    @Test
    void getAllClubs_WithPagination_shouldReturnPagedResults() {
        // Arrange
        GetAllClubsRequest request = GetAllClubsRequest.builder()
                .page(1)
                .size(2)
                .sortBy("id")
                .sortDirection("ASC")
                .build();

        List<ClubEntity> clubEntities = List.of(
                ClubEntity.builder().id(3L).clubName("Club C").build(),
                ClubEntity.builder().id(4L).clubName("Club D").build()
        );

        List<Club> clubs = List.of(
                Club.builder().clubId(3L).clubName("Club C").build(),
                Club.builder().clubId(4L).clubName("Club D").build()
        );

        // Total of 5 elements, showing page 1 (second page) with 2 elements per page
        Page<ClubEntity> pageResponse = new PageImpl<>(clubEntities, PageRequest.of(1, 2), 5);

        when(clubRepositoryMock.findByFilters(
                eq(null), eq(null), eq(null), eq(null),
                any(Pageable.class))).thenReturn(pageResponse);
        when(clubConverterMock.convertToClub(clubEntities.get(0))).thenReturn(clubs.get(0));
        when(clubConverterMock.convertToClub(clubEntities.get(1))).thenReturn(clubs.get(1));

        // Act
        GetAllClubsResponse response = getAllClubsUseCase.getAllClubs(request);

        // Assert
        assertNotNull(response);
        assertEquals(clubs, response.getClubs());
        assertEquals(5, response.getTotalElements());
        assertEquals(3, response.getTotalPages());
        assertEquals(1, response.getCurrentPage());
        verify(clubConverterMock, times(2)).convertToClub(any(ClubEntity.class));
    }

    @Test
    void getAllClubs_WithCustomSorting_shouldReturnSortedResults() {
        // Arrange
        GetAllClubsRequest request = GetAllClubsRequest.builder()
                .page(0)
                .size(10)
                .sortBy("clubName")
                .sortDirection("DESC")
                .build();

        List<ClubEntity> clubEntities = List.of(
                ClubEntity.builder().id(2L).clubName("Club B").build(),
                ClubEntity.builder().id(1L).clubName("Club A").build()
        );

        List<Club> clubs = List.of(
                Club.builder().clubId(2L).clubName("Club B").build(),
                Club.builder().clubId(1L).clubName("Club A").build()
        );

        Page<ClubEntity> pageResponse = new PageImpl<>(clubEntities, PageRequest.of(0, 10), 2);

        when(clubRepositoryMock.findByFilters(
                eq(null), eq(null), eq(null), eq(null),
                any(Pageable.class))).thenReturn(pageResponse);
        when(clubConverterMock.convertToClub(clubEntities.get(0))).thenReturn(clubs.get(0));
        when(clubConverterMock.convertToClub(clubEntities.get(1))).thenReturn(clubs.get(1));

        // Act
        GetAllClubsResponse response = getAllClubsUseCase.getAllClubs(request);

        // Assert
        assertNotNull(response);
        assertEquals(clubs, response.getClubs());
        assertEquals(2, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());
        verify(clubConverterMock, times(2)).convertToClub(any(ClubEntity.class));
    }
}