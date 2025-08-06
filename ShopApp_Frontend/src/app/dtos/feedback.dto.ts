// src/app/dtos/feedback.dto.ts
export class FeedbackDTO {
  user_id: number;
  content: string;
  sosao: number;
  product_id: number;

  constructor(data:any){
    this.user_id=data.userid;
    this.content=data.noidung;
    this.sosao=data.sosao;
    this.product_id=data.masanpham;
  }
}