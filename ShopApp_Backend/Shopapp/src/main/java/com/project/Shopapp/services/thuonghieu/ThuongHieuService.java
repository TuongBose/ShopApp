package com.project.Shopapp.services.thuonghieu;

import com.project.Shopapp.models.ThuongHieu;
import com.project.Shopapp.repositories.ThuongHieuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.project.Shopapp.dtos.ThuongHieuDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThuongHieuService implements IThuongHieuService {
    private final ThuongHieuRepository thuongHieuRepository;

    @Override
    public ThuongHieu createThuongHieu(ThuongHieuDTO thuongHieuDTO) {
        ThuongHieu newThuongHieu = ThuongHieu.builder().TENTHUONGHIEU(thuongHieuDTO.getTENTHUONGHIEU()).build();
        return thuongHieuRepository.save(newThuongHieu);
    }

    @Override
    public List<ThuongHieu> getAllThuongHieu() {
        return thuongHieuRepository.findAll();
    }

    @Override
    public ThuongHieu getThuongHieuByMATHUONGHIEU(int id) {
        return thuongHieuRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong co MATHUONGHIEUNAY"));
    }

    @Override
    public ThuongHieu updateThuongHieu(int id, ThuongHieuDTO thuongHieuDTO) {
        ThuongHieu existingThuongHieu = getThuongHieuByMATHUONGHIEU(id);
        existingThuongHieu.setTENTHUONGHIEU(thuongHieuDTO.getTENTHUONGHIEU());
        thuongHieuRepository.save(existingThuongHieu);
        return existingThuongHieu;
    }

    @Override
    public void deleteThuongHieu(int id) {
        thuongHieuRepository.deleteById(id);
    }
}
