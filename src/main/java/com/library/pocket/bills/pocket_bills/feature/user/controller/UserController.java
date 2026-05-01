package com.library.pocket.bills.pocket_bills.feature.user.controller;

import com.library.pocket.bills.pocket_bills.feature.user.dto.request.UserUpdateRequest;
import com.library.pocket.bills.pocket_bills.feature.user.dto.response.UserProfileResponse;
import com.library.pocket.bills.pocket_bills.feature.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(Authentication authentication) {
        UserProfileResponse response = userService.getCurrentUserProfile(authentication);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateCurrentUser(
            @Valid @RequestBody UserUpdateRequest request,
            Authentication authentication) {

        UserProfileResponse updatedProfile = userService.updateCurrentUser(request, authentication);
        return ResponseEntity.ok(updatedProfile);
    }
}
