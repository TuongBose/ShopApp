import { Injectable } from "@angular/core";
import { environment } from "../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { ApiResponse } from "../responses/api.response";

@Injectable({
    providedIn: 'root'
})

export class CouponService {
    private apiBaseUrl = environment.apiBaseUrl;

    constructor(private http: HttpClient) { }
    calculateCouponValue(couponCode: string, totalAmount: number): Observable<ApiResponse> {
        const url = `${this.apiBaseUrl}/coupons/calculate`;
        const params = new HttpParams()
            .set('couponCode', couponCode)
            .set('totalAmount', totalAmount.toString());

        return this.http.get<ApiResponse>(url, { params });
    }
}