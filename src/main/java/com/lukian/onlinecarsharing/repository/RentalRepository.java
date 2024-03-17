package com.lukian.onlinecarsharing.repository;

import com.lukian.onlinecarsharing.model.Rental;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findAllByUserId(Pageable pageable, Long userId);

    Optional<Rental> findByUserId(Long userId);

    Optional<Rental> findByIdAndUserId(Long rentalId, Long userId);
}
