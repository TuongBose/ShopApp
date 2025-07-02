import { CTDH } from "../models/ctdh";

export interface DonHangResponse{
    madonhang: number,
    diachi: string,
    userid: number,
    ghichu: string,
    email: string,
    tongtien: number,
    sodienthoai: string,
    ngaydathang: Date,
    fullname: string,
    trangthai: string,
    phuongthucthanhtoan: string,
    ctdhList: CTDH[],
}