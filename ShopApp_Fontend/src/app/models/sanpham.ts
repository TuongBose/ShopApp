import { HinhAnh } from "./hinhanh";

export interface SanPham {
    tensanpham: string;
    gia: number;
    mathuonghieu: number;
    mota: string;
    soluongtonkho: number;
    maloaisanpham: number;
    hinhanh: HinhAnh[];
}