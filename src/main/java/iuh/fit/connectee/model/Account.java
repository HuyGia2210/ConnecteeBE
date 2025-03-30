package iuh.fit.connectee.model;

import iuh.fit.connectee.model.misc.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Le Tran Gia Huy
 * @created 29/03/2025 - 10:10 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.model
 */

@Getter
@Setter
@Document
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {
    @Id
    @EqualsAndHashCode.Include
    private String accId;

    private String username;
    private String password;
    private Status status;
}
