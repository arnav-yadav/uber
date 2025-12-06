package com.arnav.uber.service;

import com.arnav.uber.dto.CreateRideRequest;
import com.arnav.uber.exception.BadRequestException;
import com.arnav.uber.exception.NotFoundException;
import com.arnav.uber.model.Ride;
import com.arnav.uber.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepo;

    public RideService(RideRepository rideRepo) {
        this.rideRepo = rideRepo;
    }

    public Ride createRide(String userId, CreateRideRequest req) {
        Ride ride = new Ride();
        ride.setUserId(userId);
        ride.setPickupLocation(req.getPickupLocation());
        ride.setDropLocation(req.getDropLocation());
        ride.setStatus("REQUESTED");
        ride.setCreatedAt(Instant.now());
        return rideRepo.save(ride);
    }

    public List<Ride> getPendingRides() {
        return rideRepo.findByStatus("REQUESTED");
    }

    public Ride acceptRide(String rideId, String driverId) {
        Ride ride = rideRepo.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride not in REQUESTED state");
        }
        ride.setDriverId(driverId);
        ride.setStatus("ACCEPTED");
        return rideRepo.save(ride);
    }

    public Ride completeRide(String rideId) {
        Ride ride = rideRepo.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride not in ACCEPTED state");
        }
        ride.setStatus("COMPLETED");
        return rideRepo.save(ride);
    }

    public List<Ride> getUserRides(String userId) {
        return rideRepo.findByUserId(userId);
    }
}

