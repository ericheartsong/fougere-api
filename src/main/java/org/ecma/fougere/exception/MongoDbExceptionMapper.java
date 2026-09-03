package org.ecma.fougere.exception;

import com.mongodb.MongoWriteException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.LinkedHashMap;
import java.util.Map;

@Provider // Indique à Quarkus de prendre le contrôle lors de cette exception
public class MongoDbExceptionMapper implements ExceptionMapper<MongoWriteException> {

    @Override
    public Response toResponse(MongoWriteException exception) {
        // Le code 11000 correspond spécifiquement à la violation d'un index unique (Duplicate Key)
        if (exception.getError().getCode() == 11000) {

            // Format standard Quarkus (RFC 9457)
            Map<String, Object> errorPayload = new LinkedHashMap<>();
            errorPayload.put("title", "Conflict");
            errorPayload.put("status", Response.Status.CONFLICT.getStatusCode()); // Code HTTP 409
            errorPayload.put("detail", "UNIQUE-0001");

            return Response.status(Response.Status.CONFLICT)
                    .entity(errorPayload)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // Si c'est une autre erreur d'écriture MongoDB, on renvoie une 500 standardisée
        Map<String, Object> genericError = new LinkedHashMap<>();
        genericError.put("title", "nternal Server Error");
        genericError.put("status", 500); // Code HTTP 409
        genericError.put("detail", "MONGODB-0001");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(genericError)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
