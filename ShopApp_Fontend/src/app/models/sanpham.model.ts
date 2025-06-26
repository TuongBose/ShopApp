export class SanPham {
    tensanpham: string;
    gia: number;
    mathuonghieu: number;
    mota: string;
    soluongtonkho: number;
    maloaisanpham: number;

    constructor(data: any) {
        this.tensanpham = data.tensanpham;
        this.gia = data.gia;
        this.mathuonghieu = data.mathuonghieu;
        this.mota = data.mota;
        this.soluongtonkho = data.soluongtonkho;
        this.maloaisanpham = data.maloaisanpham;
    }
}