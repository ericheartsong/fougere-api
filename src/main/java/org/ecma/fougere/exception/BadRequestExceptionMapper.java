package org.ecma.fougere.exception;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.LinkedHashMap;
import java.util.Map;

@Provider // Dit à Quarkus de prendre le contrôle dès qu'une BadRequestException est levée
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(BadRequestException exception) {

        // Construction du payload standard (RFC 9457)
        Map<String, Object> errorPayload = new LinkedHashMap<>();
        errorPayload.put("title", "Bad Request");
        errorPayload.put("status", Response.Status.BAD_REQUEST.getStatusCode()); // 400

        errorPayload.put("detail", exception.getMessage());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorPayload)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}