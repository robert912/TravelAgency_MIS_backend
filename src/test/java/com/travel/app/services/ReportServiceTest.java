package com.travel.app.services;

import com.travel.app.dtos.PackageRankingReportDTO;
import com.travel.app.dtos.SalesReportDTO;
import com.travel.app.repositories.ReportRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReportServiceTest {

    @Test
    void getSalesByPeriodUsesCompleteDayRange() {
        LocalDate startDate = LocalDate.of(2026, 5, 1);
        LocalDate endDate = LocalDate.of(2026, 5, 8);
        FakeReportRepository reportRepository = new FakeReportRepository();
        ReportService reportService = new ReportService(reportRepository);
        List<SalesReportDTO> expected = List.of();
        reportRepository.salesReport = expected;

        List<SalesReportDTO> result = reportService.getSalesByPeriod(startDate, endDate);

        assertEquals(expected, result);
        assertEquals(startDate.atStartOfDay(), reportRepository.salesStartDate);
        assertEquals(endDate.atTime(LocalTime.MAX), reportRepository.salesEndDate);
    }

    @Test
    void getPackageRankingByPeriodUsesCompleteDayRange() {
        LocalDate startDate = LocalDate.of(2026, 4, 1);
        LocalDate endDate = LocalDate.of(2026, 4, 30);
        FakeReportRepository reportRepository = new FakeReportRepository();
        ReportService reportService = new ReportService(reportRepository);
        List<PackageRankingReportDTO> expected = List.of();
        reportRepository.packageRankingReport = expected;

        List<PackageRankingReportDTO> result = reportService.getPackageRankingByPeriod(startDate, endDate);

        assertEquals(expected, result);
        assertEquals(startDate.atStartOfDay(), reportRepository.packageRankingStartDate);
        assertEquals(endDate.atTime(LocalTime.MAX), reportRepository.packageRankingEndDate);
    }

    @Test
    void getSalesByPeriodRejectsMissingDates() {
        ReportService reportService = new ReportService(new FakeReportRepository());

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> reportService.getSalesByPeriod(null, LocalDate.of(2026, 5, 8))
        );

        assertEquals("Debe ingresar fecha de inicio y fecha de termino", error.getMessage());
    }

    @Test
    void getPackageRankingByPeriodRejectsInvalidDateRange() {
        ReportService reportService = new ReportService(new FakeReportRepository());

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
        FakeReportRepository reportRepository = new FakeReportRepository();
        ReportService reportService = new ReportService(reportRepository);

        reportService.getSalesByPeriod(startDate, endDate);

        assertEquals(LocalDateTime.of(2026, 1, 15, 0, 0), reportRepository.salesStartDate);
        assertEquals(LocalDateTime.of(2026, 1, 20, 23, 59, 59, 999999999), reportRepository.salesEndDate);
    }

    private static class FakeReportRepository implements ReportRepository {

        private List<SalesReportDTO> salesReport = List.of();
        private List<PackageRankingReportDTO> packageRankingReport = List.of();
        private LocalDateTime salesStartDate;
        private LocalDateTime salesEndDate;
        private LocalDateTime packageRankingStartDate;
        private LocalDateTime packageRankingEndDate;

        @Override
        public List<SalesReportDTO> findSalesReportByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
            this.salesStartDate = startDate;
            this.salesEndDate = endDate;
            return salesReport;
        }

        @Override
        public List<PackageRankingReportDTO> findPackageRankingReportByPeriod(
                LocalDateTime startDate,
                LocalDateTime endDate
        ) {
            this.packageRankingStartDate = startDate;
            this.packageRankingEndDate = endDate;
            return packageRankingReport;
        }
    }
}
