package com.project.Shopapp.services.ctdh;

import com.project.Shopapp.dtos.CTDH_DTO;
import com.project.Shopapp.models.CTDH;

import java.util.List;

public interface ICTDHService {
    CTDH createCTDH(CTDH_DTO ctdh_dto) throws Exception;
    CTDH getCTDHByID(int id) throws Exception;
    CTDH updateCTDH(int id, CTDH_DTO ctdh_dto);
    void deleteCTDH(int id);
    List<CTDH> getCTDHByMADONHANG(int MADONHANG) throws Exception;
}
