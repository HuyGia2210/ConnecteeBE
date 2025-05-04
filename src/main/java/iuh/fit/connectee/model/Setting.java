package iuh.fit.connectee.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Le Tran Gia Huy
 * @created 01/05/2025 - 4:03 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.model
 */

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Setting {
    private String lang = "vn";
    private String scrMode = "light";
}
