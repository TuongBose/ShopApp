import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../services/account.service';
import { TokenService } from '../../services/token.service';
import { AccountResponse } from '../../responses/account/account.response';

@Component({
  selector: 'app-admin',
  standalone: false,
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent implements OnInit {
  account?: AccountResponse | null;
  admincomponent:string='orders';

  constructor(
    private accountService: AccountService,
    private tokenService: TokenService,
  ) { }

  ngOnInit(): void {
    this.account = this.accountService.getAccountFromLocalStorage();
  }

showAdminComponent(componentName:string):void{
  this.admincomponent=componentName;
}

  logout() {
    this.accountService.removeAccountFromLocalStorage();
    this.tokenService.removeToken()
    this.account = this.accountService.getAccountFromLocalStorage();
  }

}
