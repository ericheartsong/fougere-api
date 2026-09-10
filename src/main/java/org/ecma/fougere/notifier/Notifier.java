package org.ecma.fougere.notifier;

import io.smallrye.mutiny.Uni;

public interface Notifier {

    Uni<Void> Notifier (NotificationMessage message , String connectionId);
}
