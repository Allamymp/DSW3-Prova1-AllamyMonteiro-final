package org.ifpe.jakarta.main.controller;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ifpe.jakarta.main.entities.Candidate;
import org.ifpe.jakarta.main.entities.PoliticalParty;
import org.ifpe.jakarta.main.logging.CustomLogger;
import org.ifpe.jakarta.main.service.PoliticalPartyService;

import java.util.List;
import java.util.Optional;

@Path("/parties")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class PoliticalPartyController {
    @Inject
    private PoliticalPartyService politicalPartyService;

    @GET
    public Response getAllParties() {
        CustomLogger.info("Retrieving all political parties.");
        List<PoliticalParty> parties = politicalPartyService.findAll();
        return Response.ok(parties).build();
    }

    @GET
    @Path("/{id}")
    public Response getPartyById(@PathParam("id") Long id) {
        CustomLogger.info("Retrieving political party by ID: " + id);
        return politicalPartyService.findById(id)
                .map(party -> Response.ok(party).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity("Party not found with ID: " + id)
                        .build());
    }

    @POST
    public Response createParty(@Valid PoliticalParty politicalParty) {
        CustomLogger.info("Creating new political party: " + politicalParty.getName());
        PoliticalParty createdPoliticalParty = politicalPartyService.save(politicalParty);
        return Response.status(Response.Status.CREATED).entity(createdPoliticalParty).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateParty(@PathParam("id") Long id, @Valid PoliticalParty politicalParty) {
        CustomLogger.info("Updating political party with ID: " + id);
        politicalParty.setId(id);
        PoliticalParty updatedPoliticalParty = politicalPartyService.save(politicalParty);
        return Response.ok(updatedPoliticalParty).build();
    }


    @GET
    @Path("/name/{name}")
    public Response getPartyByName(@PathParam("name") String name) {
        CustomLogger.info("Retrieving political party by name: " + name);
        Optional<PoliticalParty> partyOptional = politicalPartyService.findByName(name);
        if (partyOptional.isPresent()) {
            CustomLogger.info("Political party found: " + partyOptional.get().getName());
            return Response.ok(partyOptional.get()).build();
        } else {
            CustomLogger.warn("Political party not found with name: " + name);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Party not found with name: " + name)
                    .build();
        }
    }

    @GET
    @Path("/abbreviation/{abbreviation}")
    public Response getPartyByAbbreviation(@PathParam("abbreviation") String abbreviation) {
        CustomLogger.info("Retrieving political party by abbreviation: " + abbreviation);
        Optional<PoliticalParty> partyOptional = politicalPartyService.findByAbbreviation(abbreviation);
        if (partyOptional.isPresent()) {
            CustomLogger.info("Political party found: " + partyOptional.get().getName());
            return Response.ok(partyOptional.get()).build();
        } else {
            CustomLogger.warn("Political party not found with abbreviation: " + abbreviation);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Party not found with abbreviation: " + abbreviation)
                    .build();
        }
    }

    @GET
    @Path("/number/{number}")
    public Response getPartyByNumber(@PathParam("number") Integer number) {
        CustomLogger.info("Retrieving political party by number: " + number);
        Optional<PoliticalParty> partyOptional = politicalPartyService.findByNumber(number);
        if (partyOptional.isPresent()) {
            CustomLogger.info("Political party found: " + partyOptional.get().getName());
            return Response.ok(partyOptional.get()).build();
        } else {
            CustomLogger.warn("Political party not found with number: " + number);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Party not found with number: " + number)
                    .build();
        }
    }

    @GET
    @Path("/votes")
    public List<PoliticalParty> getPartiesByVotes() {
        CustomLogger.info("Retrieving all political parties ordered by votes in descending order.");
        return politicalPartyService.findAllOrderedByVotesDesc();
    }

    @GET
    @Path("/candidates/{partyId}/votes")
    public List<Candidate> getPartyCandidatesByVotes(@PathParam("partyId") Long partyId) {
        CustomLogger.info("Retrieving all candidates for party with ID: " + partyId + " ordered by votes in descending order.");
        return politicalPartyService.findAllCandidatesOrderedByVotesDesc(partyId);
    }

    @POST
    @Path("/{partyId}/candidate")
    public Response addCandidate(@PathParam("partyId") Long partyId, Long candidateId) {
        CustomLogger.info("Adding candidate with id: " + candidateId + " to party with ID: " + partyId);
        politicalPartyService.addCandidate(partyId, candidateId);
        CustomLogger.info("Candidate added to party ID: " + partyId);
        return Response.status(Response.Status.CREATED).entity("Candidate added successfully.").build();
    }

    @DELETE
    @Path("/{partyId}/candidate/{candidateId}")
    public Response removeCandidate(@PathParam("partyId") Long partyId, @PathParam("candidateId") Long candidateId) {
        CustomLogger.info("Removing candidate with ID: " + candidateId + " from party with ID: " + partyId);
        politicalPartyService.removeVote(partyId, candidateId);
        CustomLogger.info("Candidate removed with ID: " + candidateId + " from party with ID: " + partyId);
        return Response.ok("Candidate removed successfully.").build();
    }

}
