package com.team.project.domain.user.api;

import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.api.request.AddUserRoleRequest;
import com.team.project.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{userId}/roles")
    public ResponseEntity<Void> addUserRole(
            @PathVariable UUID userId,
            @Valid @RequestBody AddUserRoleRequest request,
            @CurrentUser UserDto currentUser
    ) {
        userService.addUserRole(userId, request, currentUser.getId());
        return ResponseEntity.ok().build();
    }

}
