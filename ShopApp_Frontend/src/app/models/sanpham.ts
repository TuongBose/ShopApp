import { HinhAnh } from "./hinhanh.image";

export interface SanPham {
    masanpham:number;
    tensanpham: string;
    gia: number;
    mathuonghieu: number;
    mota: string;
    soluongtonkho: number;
    maloaisanpham: number;
    thumbnail:string;
    hinhAnhUrls: HinhAnh[];
}