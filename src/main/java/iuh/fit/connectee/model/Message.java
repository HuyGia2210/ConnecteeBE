package iuh.fit.connectee.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Le Tran Gia Huy
 * @created 30/03/2025 - 8:48 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.model
 */
@Getter
@Setter
@Document
public class Message {
    @Id
    private String id;

    private String sender; // nickname của người gửi
    private String receiver; // nickname của người nhận
    private String content; // Nội dung tin nhắn
    private long timestamp; // Thời gian gửi tin

    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }
}

