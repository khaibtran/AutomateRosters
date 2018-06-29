import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';
import { AttendanceComponent } from './attendance/attendance.component';
import { HeaderComponent } from './header/header.component';
import { HeaderLinksComponent } from './header-links/header-links.component';
import { AttendanceService } from './attendance/service/attendance.service';
import { TransportationComponent } from './transportation/transportation.component';
import { TransportationService } from './transportation/service/transportation.service';

@NgModule({
  declarations: [
    AppComponent,
    AttendanceComponent,
    HeaderComponent,
    HeaderLinksComponent,
    TransportationComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot([
      {
        path: 'attendance',
        component: AttendanceComponent
      },
      {
        path: 'transportation',
        component: TransportationComponent
      }
    ])
  ],
  providers: [AttendanceService, TransportationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
