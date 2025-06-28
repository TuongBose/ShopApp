import { Injectable } from "@angular/core";
import { SanPhamService } from "./sanpham.service";

@Injectable({
    providedIn: 'root'
})

export class CartService {
    private cart: Map<number, number> = new Map();

    constructor(private sanPhamService: SanPhamService) {
        const storedCart = localStorage.getItem('cart');
        if (storedCart) {
            this.cart = new Map(JSON.parse(storedCart));
        }
    }

    addToCart(masanpham: number, quantity: number = 1): void {
        debugger
        if (this.cart.has(masanpham)) {
            this.cart.set(masanpham, this.cart.get(masanpham)! + quantity);
        } else {
            this.cart.set(masanpham, quantity);
        }

        this.saveCartToLocalStorage();
    }

    getCart(): Map<number, number> {
        return this.cart;
    }

    private saveCartToLocalStorage(): void {
        debugger
        localStorage.setItem('cart', JSON.stringify(Array.from(this.cart.entries())));
    }

    clearCart():void{
        this.cart.clear();
        this.saveCartToLocalStorage();
    }
}