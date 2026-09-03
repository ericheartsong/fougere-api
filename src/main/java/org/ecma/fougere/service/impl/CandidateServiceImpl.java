package org.ecma.fougere.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.ecma.fougere.domain.Candidate;
import org.ecma.fougere.service.CandidateService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CandidateServiceImpl implements CandidateService {

    @Override
    public Optional<Candidate> getCandidateByUuid(String uuid) {
        return Candidate.find("uuid", uuid).firstResultOptional();
    }


    @Override
    public Optional<Candidate> getCandidateById(ObjectId id) {
        return Candidate.findByIdOptional(id);
    }

    @Override
    public List<Candidate> getAll() {
        return Candidate.listAll();
    }

    @Override
    public Optional<Candidate> createCandidate(Candidate candidate) {
        if (candidate.id != null) {
            return Optional.empty();
        }
        candidate.persist();
        return Optional.of(candidate);
    }

    @Override
    public Optional<Candidate> updateCandidate(Candidate candidate) {
        if (ObjectId.isValid(candidate.id.toHexString())) {
            Candidate candidateExist = Candidate.findById(candidate.id);
            if (candidateExist != null) {
                candidate.update();
                return Optional.of(candidate);
            } else  {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

}
