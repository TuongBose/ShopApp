package com.project.Shopapp.Services;

import com.project.Shopapp.DTOs.CTDH_DTO;
import com.project.Shopapp.Models.CTDH;
import com.project.Shopapp.Responses.CTDHResponse;

import java.util.List;

public interface ICTDHService {
    CTDH createCTDH(CTDH_DTO ctdh_dto) throws Exception;
    CTDH getCTDHByID(int id) throws Exception;
    CTDH updateCTDH(int id, CTDH_DTO ctdh_dto);
    void deleteCTDH(int id);
    List<CTDH> getCTDHByMADONHANG(int MADONHANG) throws Exception;
}
