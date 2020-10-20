package cn.xzxy.lewy.framework.kafka.domain.sender.model;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lewy95
 */
public class SenderEvent {

    private List<MessageSenderLog> messageSenderLogs;
    private MessageStatus messageStatus;
    private Throwable throwable;
    private int partition;
    private long offset;

    public static SenderEvent waitForSend(List<MessageSenderLog> messageSenderLogs) {
        return new SenderEvent(messageSenderLogs, MessageStatus.WAIT_FOR_SEND, (Throwable) null);
    }

    public static SenderEvent cancel(Throwable throwable, List<MessageSenderLog> messageSenderLogs) {
        return new SenderEvent(messageSenderLogs, MessageStatus.CANCEL, throwable);
    }

    public static SenderEvent fail(Throwable throwable, MessageSenderLog... messageSenderLogs) {
        return new SenderEvent(CollectionUtils.arrayToList(messageSenderLogs), MessageStatus.FAIL, throwable);
    }

    public static SenderEvent success(int partition, long offset, MessageSenderLog... messageSenderLogs) {
        SenderEvent senderEvt = new SenderEvent(CollectionUtils.arrayToList(messageSenderLogs), MessageStatus.SUCCESS, (Throwable) null);
        senderEvt.partition = partition;
        senderEvt.offset = offset;
        return senderEvt;
    }

    private SenderEvent(List<MessageSenderLog> messageSenderLogs, MessageStatus messageStatus, Throwable throwable) {
        this.messageSenderLogs = messageSenderLogs;
        this.messageStatus = messageStatus;
        this.throwable = throwable;
    }

    public List<MessageSenderLog> getMessageSenderLogs() {
        return this.messageSenderLogs;
    }

    public MessageStatus getMessageStatus() {
        return this.messageStatus;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public int getPartition() {
        return this.partition;
    }

    public long getOffset() {
        return this.offset;
    }
}
