import { DonHang } from "./donhang";
import { SanPham } from "./sanpham";

export interface CTDH {
    giaban:number;
    soluong:number;
    masanpham:SanPham;
    tongtien:number;
    madonhang:DonHang;
}