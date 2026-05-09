package com.travel.app.services;

import com.travel.app.dtos.PackageRankingReportDTO;
import com.travel.app.dtos.SalesReportDTO;
import com.travel.app.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void getSalesByPeriodUsesCompleteDayRange() {
        LocalDate startDate = LocalDate.of(2026, 5, 1);
        LocalDate endDate = LocalDate.of(2026, 5, 8);
        List<SalesReportDTO> expected = List.of();

        when(reservationRepository.findSalesReportByPeriod(
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX)
        )).thenReturn(expected);

        List<SalesReportDTO> result = reportService.getSalesByPeriod(startDate, endDate);

        assertEquals(expected, result);
        verify(reservationRepository).findSalesReportByPeriod(
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX)
        );
    }

    @Test
    void getPackageRankingByPeriodUsesCompleteDayRange() {
        LocalDate startDate = LocalDate.of(2026, 4, 1);
        LocalDate endDate = LocalDate.of(2026, 4, 30);
        List<PackageRankingReportDTO> expected = List.of();

        when(reservationRepository.findPackageRankingReportByPeriod(
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX)
        )).thenReturn(expected);

        List<PackageRankingReportDTO> result = reportService.getPackageRankingByPeriod(startDate, endDate);

        assertEquals(expected, result);
        verify(reservationRepository).findPackageRankingReportByPeriod(
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX)
        );
    }

    @Test
    void getSalesByPeriodRejectsMissingDates() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> reportService.getSalesByPeriod(null, LocalDate.of(2026, 5, 8))
        );

        assertEquals("Debe ingresar fecha de inicio y fecha de termino", error.getMessage());
    }

    @Test
    void getPackageRankingByPeriodRejectsInvalidDateRange() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> reportService.getPackageRankingByPeriod(
                        LocalDate.of(2026, 5, 9),
                        LocalDate.of(2026, 5, 8)
                )
        );

        assertEquals("La fecha de inicio no puede ser posterior a la fecha de termino", error.getMessage());
    }

    @Test
    void getSalesByPeriodPassesStartAndEndBoundariesToRepository() {
        LocalDate startDate = LocalDate.of(2026, 1, 15);
        LocalDate endDate = LocalDate.of(2026, 1, 20);
        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        reportService.getSalesByPeriod(startDate, endDate);

        verify(reservationRepository).findSalesReportByPeriod(startCaptor.capture(), endCaptor.capture());
        assertEquals(LocalDateTime.of(2026, 1, 15, 0, 0), startCaptor.getValue());
        assertEquals(LocalDateTime.of(2026, 1, 20, 23, 59, 59, 999999999), endCaptor.getValue());
    }
}
