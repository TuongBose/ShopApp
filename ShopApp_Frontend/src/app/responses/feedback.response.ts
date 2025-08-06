import { AccountResponse } from "./account/account.response";

export interface FeedbackResponse {
  feedbackid: number;
  accountResponse: AccountResponse;
  noidung: string;
  sosao: number;
  masanpham: number;
  createdAt: Date;
}