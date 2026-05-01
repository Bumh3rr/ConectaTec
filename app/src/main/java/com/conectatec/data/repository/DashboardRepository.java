package com.conectatec.data.repository;

import com.conectatec.data.model.DashboardResumen;

public interface DashboardRepository {
    DashboardResumen getResumen() throws Exception;
}
