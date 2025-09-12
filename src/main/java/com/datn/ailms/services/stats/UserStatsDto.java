package com.datn.ailms.services.stats;

import lombok.*;
import lombok.experimental.FieldDefaults;


public interface UserStatsDto {
    Long getTotalUsers();
    Long getTotalMales();
    Long getTotalFemales();
    Long getTotalActives();
    Long getTotalBlocked();
}
