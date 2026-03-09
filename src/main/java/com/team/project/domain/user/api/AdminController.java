package com.team.project.domain.user.api;

import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.user.api.request.AddUserRoleRequest;
import com.team.project.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "admin", description = "admin API")
@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @Operation(summary = "유저 권한 추가", description = "관리자가 유저 권한을 추가합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 정보 추가 공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인가되지 않은 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 정보 찾을 수 없음", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = """
                              {
                                "userId" : "111"
                            	"role": "ROLE_CUSTOMER"
                              }
                            """
                    )
            ))
    })
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
