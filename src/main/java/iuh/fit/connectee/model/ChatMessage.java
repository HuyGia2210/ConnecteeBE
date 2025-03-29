package iuh.fit.connectee.model;

import lombok.*;

/**
 * @author Le Tran Gia Huy
 * @created 29/03/2025 - 11:56 AM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.model
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String content;
    private String sender;
    private MessageType type;
}
