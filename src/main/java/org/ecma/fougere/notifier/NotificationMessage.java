package org.ecma.fougere.notifier;


import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class NotificationMessage {

    public enum codeTypeMessage {HANDSHAKE,
                                 ARTWORK_CREATED,
                                 ARTWORK_UPDATED,
                                 CANDIDATE_CREATED,
                                 CANDIDATE_UPDATED}

    private String codeTypeMessage;
    private String connectionId;
    private String id;
    private String messsageInfo;

    public NotificationMessage() {
        super();
    }

    public NotificationMessage (String codeTypeMessage,
                                String connectionId,
                                String id,
                                String messageInfo) {
        setCodeTypeMessage(codeTypeMessage);
        setConnectionId(connectionId);
        setId(id);
        setMessageInfo(messageInfo);
    }

    public String getCodeTypeMessage() {
        return codeTypeMessage;
    }
    public void setCodeTypeMessage(String codeType) {
        this.codeTypeMessage = codeType;
    }

    public String getConnectionId() {
        return connectionId;
    }
    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getMessageInfo() {
        return messsageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messsageInfo = messageInfo;
    }
}
