package com.conectatec.data.repository;

import com.conectatec.data.model.DashboardResumen;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DashboardRepositoryImpl implements DashboardRepository {

    @Inject
    public DashboardRepositoryImpl() {}

    @Override
    public DashboardResumen getResumen() throws Exception {
        Thread.sleep(300);
        // TODO: reemplazar con llamada a API — GET /docente/dashboard
        return new DashboardResumen(3, 47, 5);
    }
}
