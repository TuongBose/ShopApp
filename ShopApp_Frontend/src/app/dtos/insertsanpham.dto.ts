export class insertSanPhamDTO{
    tensanpham:string;
    gia:number;
    mota:string;
    mathuonghieu:number;
    maloaisanpham:number;

  constructor(data:any){
    this.tensanpham = data.tensanpham;
    this.gia = data.gia;
    this.mota = data.mota;
    this.mathuonghieu = data.mathuonghieu;
    this.maloaisanpham = data.maloaisanpham;
  }
}