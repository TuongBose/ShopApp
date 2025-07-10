export class CartItemDTO{
    masanpham:number;
    quantity:number;

  constructor(data:any){
    this.masanpham=data.masanpham;
    this.quantity=data.quantity
  }
}