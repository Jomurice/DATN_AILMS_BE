package com.datn.ailms.services.stats;

public interface UserStatsDto {
    Long getTotalUsers();
    Long getTotalMales();
    Long getTotalFemales();
    Long getTotalActives();
    Long getTotalBlocked();
}
