import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../services/account.service';
import { AccountResponse } from '../../responses/account/account.response';
import { TokenService } from '../../services/token.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  account?: AccountResponse | null;
  isPopoverOpen?: boolean;
  activeNavItem: number = 0;

  constructor(
    private accountService: AccountService,
    private tokenService: TokenService,
    private router:Router
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
    if(index ===0){
      debugger
      this.router.navigate(['/user-profile'])
    }else    if (index === 2) {
      this.accountService.removeAccountFromLocalStorage();
      this.tokenService.removeToken()
      this.account = this.accountService.getAccountFromLocalStorage();
    }
    this.isPopoverOpen = false;
  }

  setActiveNavItem(index: number) {
    this.activeNavItem = index;
  }
}
