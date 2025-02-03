package org.ifpe.jakarta.main.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ifpe.jakarta.main.entities.Candidate;
import org.ifpe.jakarta.main.logging.CustomLogger;
import org.ifpe.jakarta.main.service.CandidateService;

import java.util.List;

@Path("/candidates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class CandidateController {

    @Inject
    private CandidateService candidateService;

    /**
     * Retrieves a list of all candidates.
     *
     * @return A Response with the list of candidates.
     */
    @GET
    public Response getAll() {
        CustomLogger.info("Retrieving all candidates.");
        List<Candidate> candidates = candidateService.findAll();
        return Response.status(Response.Status.OK).entity(candidates).build();
    }

    /**
     * Retrieves a candidate by their ID.
     *
     * @param id The ID of the candidate to retrieve.
     * @return A Response with the candidate data, or an error if not found.
     */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        CustomLogger.info("Retrieving candidate with ID: " + id);
        Candidate candidate = candidateService.findById(id)
                .orElseThrow(() -> new WebApplicationException("Candidate not found with ID: " + id, Response.Status.NOT_FOUND));
        return Response.status(Response.Status.OK).entity(candidate).build();
    }

    /**
     * Creates a new candidate.
     *
     * @param candidate The candidate to create.
     * @return A Response indicating the result of the creation.
     */
    @POST
    public Response create(Candidate candidate) {
        CustomLogger.info("Creating candidate: " + candidate.getName());
        Candidate createdCandidate = candidateService.create(candidate);
        return Response.status(Response.Status.CREATED).entity(createdCandidate).build();
    }

    /**
     * Updates an existing candidate.
     *
     * @param id The ID of the candidate to update.
     * @param candidate The updated candidate data.
     * @return A Response with the updated candidate.
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Candidate candidate) {
        CustomLogger.info("Updating candidate with ID: " + id);
        Candidate updatedCandidate = candidateService.update(candidate);
        return Response.status(Response.Status.OK).entity(updatedCandidate).build();
    }

    /**
     * Deletes a candidate by their ID.
     *
     * @param id The ID of the candidate to delete.
     * @return A Response indicating the result of the deletion.
     */
    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") Long id) {
        CustomLogger.info("Deleting candidate with ID: " + id);
        candidateService.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build(); // No content status for successful deletion
    }

    /**
     * Retrieves all candidates ordered by their vote count in descending order.
     *
     * @return A Response with the list of candidates ordered by vote count.
     */
    @GET
    @Path("/ordenados-votos")
    public Response getAllOrderedByVotes() {
        CustomLogger.info("Retrieving all candidates ordered by vote count.");
        List<Candidate> candidates = candidateService.findAllOrderedByVotes();
        return Response.status(Response.Status.OK).entity(candidates).build();
    }



}
