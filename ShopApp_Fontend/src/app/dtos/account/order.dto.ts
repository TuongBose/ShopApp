import {IsString, IsNotEmpty, IsPhoneNumber, IsDate} from 'class-validator'

export class OrderDTO{
    userid: number;
    fullname:string;
    email:string;
    sodienthoai:string;
    diachi:string;
    ghichu:string;
    tongtien:number;
    phuongthucthanhtoan:string;

  constructor(data:any){
    this.sodienthoai=data.sodienthoai;
    this.password=data.password;
  }
}