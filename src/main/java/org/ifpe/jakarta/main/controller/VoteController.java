package org.ifpe.jakarta.main.controller;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ifpe.jakarta.main.entities.Candidate;
import org.ifpe.jakarta.main.entities.PoliticalParty;
import org.ifpe.jakarta.main.entities.TotalVotes;
import org.ifpe.jakarta.main.repository.TotalVotesRepository;
import org.ifpe.jakarta.main.request.CandidateRequest;
import org.ifpe.jakarta.main.request.VoteRequest;
import org.ifpe.jakarta.main.service.CandidateService;
import org.ifpe.jakarta.main.service.PoliticalPartyService;

import java.util.Objects;
import java.util.Optional;


@Path("/votes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_XML)
@RequestScoped
public class VoteController {

    @Inject
    private CandidateService candidateService;
    @Inject
    private PoliticalPartyService politicalPartyService;
    @Inject
    private TotalVotesRepository totalVotesRepository;

    @POST
    @Path("/addVote")
    public Response addVote(VoteRequest voteRequest) {
        System.out.println("Entrou");
        System.out.println("partido:" + voteRequest.getPartyId());
        System.out.println("candidato:" + voteRequest.getCandidateId());
        if (voteRequest != null
                && voteRequest.getPartyId() != null
                && voteRequest.getCandidateId() != null) {
            candidateService.addVote(voteRequest.getCandidateId());
            politicalPartyService.addVote(voteRequest.getPartyId());
            Optional<TotalVotes> totalVotes = totalVotesRepository.findById(1L);
            if (totalVotes.isPresent()) {
                totalVotes.get().addVote();
                totalVotesRepository.save(totalVotes.get());
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid vote request").build();
        }
    }

    @POST
    public Response getCandidateByParty(CandidateRequest candidateRequest) {
        System.out.println("entrou");
        System.out.println("partido: " + candidateRequest.getPartyNumber());
        System.out.println("candidato:" + candidateRequest.getCandidateNumber());
        if (candidateRequest != null
                && candidateRequest.getPartyNumber() != null
                && candidateRequest.getCandidateNumber() != null) {

            Candidate cd = null;
            Optional<PoliticalParty> pp = politicalPartyService.findByNumberWithoutLazy(candidateRequest.getPartyNumber());

            if (pp.isPresent()) {

                pp.get().getCandidates().size();

                for (Candidate candidate : pp.get().getCandidates()) {
                    if (Objects.equals(candidate.getNumber(), candidateRequest.getCandidateNumber())) {
                        cd = candidate;
                        System.out.println("Candidate: " + candidate.getName() + ", Number: " + candidate.getNumber());
                        break;
                    }
                }
            }

            if (cd != null) {
                return Response.status(Response.Status.OK).entity(cd).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Candidate not found").build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request data").build();
        }
    }

    @POST
    @Path("/blank")
    public Response voteBlank() {
        Optional<TotalVotes> totalVotes = totalVotesRepository.findById(1L);
        if (totalVotes.isPresent()) {
            totalVotes.get().addVote();
            totalVotesRepository.save(totalVotes.get());
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    @GET
    @Path("/result")
    public Response getResult() {
        Optional<TotalVotes> votes = totalVotesRepository.findById(1L);
        if (votes.isPresent()) {
            return Response.ok().entity(votes.get().getTotal()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}

