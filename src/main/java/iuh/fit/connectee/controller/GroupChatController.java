package iuh.fit.connectee.controller;

import iuh.fit.connectee.model.GroupChat;
import iuh.fit.connectee.repo.GroupChatRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 10/04/2025 - 4:25 PM
 * @project ConnecteeBE
 * @package iuh.fit.connectee.controller
 */

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupChatController {

    private final GroupChatRepository groupRepository;

    @PostMapping("/create")
    public ResponseEntity<GroupChat> createGroup(@RequestBody CreateGroupRequest request) {
        GroupChat group = new GroupChat();
        group.setGroupName(request.getGroupName());
        group.setMembers(request.getMembers()); // danh sách nickname

        return ResponseEntity.ok(groupRepository.save(group));
    }

    @Data
    public static class CreateGroupRequest {
        private String groupName;
        private List<String> members;
    }
}
