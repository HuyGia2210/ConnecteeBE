package iuh.fit.connectee.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Le Tran Gia Huy
 * @created 04/05/2025 - 8:13 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.model
 */

@Getter
@Setter
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Admin {
    @Id
    @EqualsAndHashCode.Include
    @Indexed(unique = true)
    private String id;
    @EqualsAndHashCode.Include
    private String username;
    private String password;

    private boolean isAdmin = true;
}
