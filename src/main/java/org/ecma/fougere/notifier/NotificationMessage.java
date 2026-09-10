package org.ecma.fougere.notifier;



public class NotificationMessage {

    public enum codeTypeMessage {HANDSHAKE,
                                 ARTWORK_CREATED}
    private String codeTypeMessage;
    private String connectionId;
    private String id;
    private String messsageInfo;

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

    public String getMesssageInfo() {
        return messsageInfo;
    }

    public void setMesssageInfo(String messsageInfo) {
        this.messsageInfo = messsageInfo;
    }
}
