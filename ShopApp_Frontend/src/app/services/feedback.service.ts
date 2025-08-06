import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ApiResponse } from "../responses/api.response";
import { FeedbackDTO } from "../dtos/feedback.dto";

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
  private apiUrl = `${environment.apiBaseUrl}/feedbacks`;

  constructor(private http: HttpClient) {}

  getFeedbacksByProduct(productId: number): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.apiUrl}?product_id=${productId}`);
  }

  insertFeedback(feedbackDTO: FeedbackDTO): Observable<ApiResponse> {
    debugger
    return this.http.post<ApiResponse>(`${this.apiUrl}`, feedbackDTO);
  }
}