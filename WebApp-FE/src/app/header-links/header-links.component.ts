import { Component, OnInit } from '@angular/core';
import {
  Router,
  RouterEvent,
  NavigationStart
} from '@angular/router';

@Component({
  selector: 'app-header-links',
  templateUrl: './header-links.component.html',
  styleUrls: ['./header-links.component.css']
})
export class HeaderLinksComponent implements OnInit {

  private router: Router;
  private currentRoute: string;

  constructor(router:Router) { 
    this.router = router;
  }

  ngOnInit() {
  }

  private goToRoute(url: string): void {
    this.router.navigate([url]);
    this.router.events
      .subscribe((event:RouterEvent) => {
        if (event instanceof NavigationStart) {
          this.setCurrentRoute(event.url);
        }
      });
  }

  private setCurrentRoute(url: string): void {
    if (url.indexOf('attendance') > -1) {
      this.currentRoute = 'attendance';
    }
    else if (url.indexOf('transportation') > -1) {
      this.currentRoute = 'transportation';
    } 
    else {
      this.currentRoute = 'attendance';
    }
  }

}
