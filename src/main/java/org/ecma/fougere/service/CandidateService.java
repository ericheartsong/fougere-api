package org.ecma.fougere.service;

import org.bson.types.ObjectId;
import org.ecma.fougere.domain.AvatarInfo;
import org.ecma.fougere.domain.Candidate;
import org.ecma.fougere.domain.DomainIndicators;

import java.util.List;
import java.util.Optional;

public interface CandidateService {


    Optional<Candidate> getCandidateByUuid(String uuid);
    List<Candidate> getAll();

    Optional<Candidate> createCandidate(Candidate candidate);

    Optional<Candidate> updateCandidate(Candidate candidate);

    Optional<Candidate> getCandidateById(ObjectId id);

    List<Candidate> getFiltered(String name);

    Optional<AvatarInfo> getCandidateInfoByUuid(String uuid) throws Exception;

    Optional<DomainIndicators> getCandidateIndicators();
}
