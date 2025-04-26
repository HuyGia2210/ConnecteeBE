package iuh.fit.connectee.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 10/04/2025 - 4:22 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.model.misc
 */
@Data
@Document(collection = "chat_groups")
public class GroupChat {
    @Id
    @Indexed(unique = true)
    private String id;

    private String groupName;
    private List<String> members = new ArrayList<>(); // danh sách nickname
}
