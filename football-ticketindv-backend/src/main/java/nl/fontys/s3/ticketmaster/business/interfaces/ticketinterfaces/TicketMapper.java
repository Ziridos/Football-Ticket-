package nl.fontys.s3.ticketmaster.business.interfaces.ticketinterfaces;

import nl.fontys.s3.ticketmaster.domain.ticket.*;
import nl.fontys.s3.ticketmaster.persitence.entity.MatchEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.TicketEntity;
import nl.fontys.s3.ticketmaster.persitence.entity.UserEntity;

public interface TicketMapper {
    CreateTicketResponse toCreateTicketResponse(TicketEntity entity);
    GetTicketResponse toGetTicketResponse(TicketEntity entity);
    TicketEntity toTicketEntity(CreateTicketRequest request, UserEntity user, MatchEntity match);
    TicketEntity toUpdatedTicketEntity(TicketEntity originalTicket, UpdateTicketRequest request);
    UpdateTicketResponse toUpdateTicketResponse(TicketEntity ticket);
    TicketDTO toTicket(TicketEntity entity);}
