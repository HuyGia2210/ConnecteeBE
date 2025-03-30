package iuh.fit.connectee.model;

import iuh.fit.connectee.model.misc.Status;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 29/03/2025 - 10:04 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.model
 */

@Getter
@Setter
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AppUser {
    @Id
    @EqualsAndHashCode.Include
    private String nickName;

    private String accId;
    private String fullName;
    private String email;
    private List<String> friendNickNames = new ArrayList<>();
}
