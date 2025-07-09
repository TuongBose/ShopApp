import {IsString, IsNotEmpty, IsPhoneNumber, IsDate} from 'class-validator'

export class LoginDTO{
    @IsString()
    @IsNotEmpty()
    password: string;

    @IsPhoneNumber()
    sodienthoai:string;

    roleid: boolean;

  constructor(data:any){
    this.sodienthoai=data.sodienthoai;
    this.password=data.password;
    this.roleid = data.roleid;
  }
}