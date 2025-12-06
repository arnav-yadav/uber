package com.arnav.uber.controller;

import com.arnav.uber.dto.CreateRideRequest;
import com.arnav.uber.model.Ride;
import com.arnav.uber.model.User;
import com.arnav.uber.service.AuthService;
import com.arnav.uber.service.RideService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final AuthService authService;
    private final RideService rideService;

    public RideController(AuthService authService, RideService rideService) {
        this.authService = authService;
        this.rideService = rideService;
    }

    @PostMapping("/rides")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Ride createRide(@Valid @RequestBody CreateRideRequest req) {
        User user = authService.getCurrentUser();
        return rideService.createRide(user.getId(), req);
    }

    @GetMapping("/user/rides")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Ride> getMyRides() {
        User user = authService.getCurrentUser();
        return rideService.getUserRides(user.getId());
    }

    @PostMapping("/rides/{rideId}/complete")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_DRIVER')")
    public Ride complete(@PathVariable String rideId) {
        return rideService.completeRide(rideId);
    }
}

