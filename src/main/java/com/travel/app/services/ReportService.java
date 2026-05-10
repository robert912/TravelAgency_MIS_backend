package com.travel.app.services;

import com.travel.app.dtos.PackageRankingReportDTO;
import com.travel.app.dtos.SalesReportDTO;
import com.travel.app.repositories.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<SalesReportDTO> getSalesByPeriod(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        return reportRepository.findSalesReportByPeriod(startOfDay(startDate), endOfDay(endDate));
    }

    public List<PackageRankingReportDTO> getPackageRankingByPeriod(LocalDate startDate, LocalDate endDate) {
        validateDateRange(startDate, endDate);
        return reportRepository.findPackageRankingReportByPeriod(startOfDay(startDate), endOfDay(endDate));
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Debe ingresar fecha de inicio y fecha de termino");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de termino");
        }
    }

    private LocalDateTime startOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    private LocalDateTime endOfDay(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }
}
