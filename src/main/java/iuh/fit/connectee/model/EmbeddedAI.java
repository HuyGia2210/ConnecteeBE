package iuh.fit.connectee.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author Le Tran Gia Huy
 * @created 02/05/2025 - 12:52 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.model
 */

@Getter
@Setter
@Document
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmbeddedAI {
    @Id
    @EqualsAndHashCode.Include
    @Indexed(unique = true)
    private String id;

    private String nickname;

    private String prompt;

    private String result;

    private long timestamp;

    public EmbeddedAI(String nickname, String prompt, String result) {
        this.nickname = nickname;
        this.prompt = prompt;
        this.result = result;
        this.timestamp = System.currentTimeMillis();
    }
}
