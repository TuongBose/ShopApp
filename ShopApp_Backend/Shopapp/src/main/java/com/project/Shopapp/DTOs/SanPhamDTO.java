package com.project.Shopapp.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamDTO {
    @NotBlank(message = "Ten san pham khong duoc bo trong")
    @Size(min = 5, max = 200, message = "Ten san pham phai tu 5 den 200 ky tu")
    private String TENSANPHAM;

    @Min(value = 0, message = "Gia san pham phai lon hon hoac bang 0")
    private int GIA;
    private int MATHUONGHIEU;
    private String MOTA;
    private int SOLUONGTONKHO;
    private int MALOAISANPHAM;

    private List<MultipartFile> files;
}
