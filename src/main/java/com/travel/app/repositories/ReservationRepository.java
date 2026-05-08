package com.travel.app.repositories;

import com.travel.app.dtos.PackageRankingReportDTO;
import com.travel.app.dtos.SalesReportDTO;
import com.travel.app.entities.ReservationEntity;
import com.travel.app.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByActive(Integer active);

    List<ReservationEntity> findByPersonIdAndActive(Long personId, Integer active);

    List<ReservationEntity> findByTourPackageIdAndActive(Long tourPackageId, Integer active);

    List<ReservationEntity> findByStatusAndActive(String status, Integer active);

    @Query("SELECT COALESCE(SUM(r.passengersCount), 0) FROM ReservationEntity r " +
            "WHERE r.tourPackage.id = :packageId " +
            "AND r.status IN :statuses " +
            "AND r.active = 1")
    Integer countPassengersByPackageIdAndStatuses(@Param("packageId") Long packageId,
                                                  @Param("statuses") List<ReservationStatus> statuses);

    @Query("""
            SELECT new com.travel.app.dtos.SalesReportDTO(
                r.id,
                COALESCE(p.createdAt, r.reservationDate),
                r.reservationDate,
                p.createdAt,
                person.fullName,
                person.email,
                tourPackage.name,
                tourPackage.destination,
                COALESCE(r.passengersCount, 0),
                COALESCE(r.subtotal, 0),
                COALESCE(r.totalAmount, 0),
                r.status
            )
            FROM ReservationEntity r
            JOIN r.person person
            JOIN r.tourPackage tourPackage
            LEFT JOIN PaymentEntity p ON p.reservation = r
            WHERE r.active = 1
              AND r.status = com.travel.app.enums.ReservationStatus.PAGADA
              AND (
                    r.reservationDate BETWEEN :startDate AND :endDate
                    OR p.createdAt BETWEEN :startDate AND :endDate
              )
            ORDER BY COALESCE(p.createdAt, r.reservationDate) ASC, r.id ASC
            """)
    List<SalesReportDTO> findSalesReportByPeriod(@Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT new com.travel.app.dtos.PackageRankingReportDTO(
                tourPackage.id,
                tourPackage.name,
                tourPackage.destination,
                COUNT(r.id),
                COALESCE(SUM(r.passengersCount), 0),
                COALESCE(SUM(r.totalAmount), 0)
            )
            FROM ReservationEntity r
            JOIN r.tourPackage tourPackage
            LEFT JOIN PaymentEntity p ON p.reservation = r
            WHERE r.active = 1
              AND r.status = com.travel.app.enums.ReservationStatus.PAGADA
              AND (
                    r.reservationDate BETWEEN :startDate AND :endDate
                    OR p.createdAt BETWEEN :startDate AND :endDate
              )
            GROUP BY tourPackage.id, tourPackage.name, tourPackage.destination
            ORDER BY COUNT(r.id) DESC, COALESCE(SUM(r.passengersCount), 0) DESC, tourPackage.name ASC
            """)
    List<PackageRankingReportDTO> findPackageRankingReportByPeriod(@Param("startDate") LocalDateTime startDate,
                                                                   @Param("endDate") LocalDateTime endDate);

    default Integer countConfirmedPassengersByPackageId(Long packageId) {
        return countPassengersByPackageIdAndStatuses(packageId,
                List.of(ReservationStatus.PENDIENTE, ReservationStatus.PAGADA));
    }
}
