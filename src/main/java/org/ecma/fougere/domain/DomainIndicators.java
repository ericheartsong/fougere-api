package org.ecma.fougere.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DomainIndicators {
    private long nbElements;

    public long getNbElements() {
        return nbElements;
    }

    public void setNbElements(long nbElements) {
        this.nbElements = nbElements;
    }
}
