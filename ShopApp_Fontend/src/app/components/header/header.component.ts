import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../services/account.service';
import { AccountResponse } from '../../responses/account/account.response';
import { CartService } from '../../services/cart.service';
import { TokenService } from '../../services/token.service';
import { NgbPopoverConfig } from '@ng-bootstrap/ng-bootstrap'

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  account?: AccountResponse | null;
  isPopoverOpen?: boolean;
  constructor(
    private accountService: AccountService,
    private popoverConfig: NgbPopoverConfig,
    private tokenService: TokenService
  ) { }

  ngOnInit(): void {
    debugger
    this.account = this.accountService.getAccountFromLocalStorage();
  }

  togglePopover(event: Event): void {
    event.preventDefault();
    this.isPopoverOpen = !this.isPopoverOpen;
  }

  handleItemClick(index: number): void {
    alert(`Clicked on "${index}"`);
    if (index === 2) {
      this.accountService.removeAccountFromLocalStorage();
      this.tokenService.removeToken()
      this.account = this.accountService.getAccountFromLocalStorage();
    }
    this.isPopoverOpen = false;
  }
}
