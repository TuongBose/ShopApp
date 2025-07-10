import {IsString, IsNotEmpty, IsPhoneNumber, IsDate} from 'class-validator'

export class RegisterDTO{
    @IsString()
    @IsNotEmpty()
    password: string;

    @IsString()
    @IsNotEmpty()
    retypepassword: string;

    email:string;

    @IsString()
    fullname:string;

    @IsString()
    diachi:string;

    @IsPhoneNumber()
    sodienthoai:string;

    @IsDate()
    ngaysinh:Date;

    FACEBOOK_ACCOUNT_ID:number = 0;
    GOOGLE_ACCOUNT_ID:number = 0;

  constructor(data:any){
    this.sodienthoai=data.sodienthoai;
    this.password=data.password;
    this.retypepassword=data.retypepassword;
    this.fullname=data.fullname;
    this.email=data.email;
    this.diachi=data.diachi;
    this.ngaysinh=data.ngaysinh;
    this.FACEBOOK_ACCOUNT_ID=data.FACEBOOK_ACCOUNT_ID || 0;
    this.GOOGLE_ACCOUNT_ID=data.GOOGLE_ACCOUNT_ID || 0;
  }
}