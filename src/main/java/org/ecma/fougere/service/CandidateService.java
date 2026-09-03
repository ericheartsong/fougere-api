package org.ecma.fougere.service;

import jakarta.validation.Valid;
import org.ecma.fougere.domain.Candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateService {


    Optional<Candidate> getCandidateByUuid(String uuid);
    List<Candidate> getAll();

    Optional<Candidate> createCandidate(Candidate candidate);

    Optional<Candidate> updateCandidate(Candidate candidate);

    Optional<Candidate> getCandidateById(String id);
}
