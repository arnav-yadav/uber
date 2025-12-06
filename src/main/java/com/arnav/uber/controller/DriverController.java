package com.arnav.uber.controller;

import com.arnav.uber.model.Ride;
import com.arnav.uber.model.User;
import com.arnav.uber.service.AuthService;
import com.arnav.uber.service.RideService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver")
public class DriverController {

    private final AuthService authService;
    private final RideService rideService;

    public DriverController(AuthService authService, RideService rideService) {
        this.authService = authService;
        this.rideService = rideService;
    }

    @GetMapping("/rides/requests")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public List<Ride> getPending() {
        return rideService.getPendingRides();
    }

    @PostMapping("/rides/{rideId}/accept")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public Ride accept(@PathVariable String rideId) {
        User driver = authService.getCurrentUser();
        return rideService.acceptRide(rideId, driver.getId());
    }
}
