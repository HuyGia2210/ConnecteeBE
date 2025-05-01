package iuh.fit.connectee.model;

import iuh.fit.connectee.model.misc.Gender;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

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
    @Indexed(unique = true)
    private String nickname;

    private String accId;
    private String fullName;
    private LocalDate dob;
    private Gender gender;
    private String email;
    private Set<String> friendList = new LinkedHashSet<>();
//    private Set<String> friendRequestSendingList = new LinkedHashSet<>();
    private Set<String> friendRequests = new LinkedHashSet<>();
}
