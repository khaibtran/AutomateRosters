import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { TransportationService } from '../transportation/service/transportation.service';

@Component({
  selector: 'app-transportation',
  templateUrl: './transportation.component.html',
  styleUrls: ['./transportation.component.css']
})
export class TransportationComponent implements OnInit {

  private transportationService:TransportationService;
  private error:HttpErrorResponse;
  private response:string;

  constructor(transportationService:TransportationService) { 
    this.transportationService = transportationService;
  }

  ngOnInit() {
  }

  automateTransportationUpdates() {
    this.transportationService.generateTransportationUpdate().subscribe(
      (response) => {this.response = response; console.log(response)},
      (error) => this.error = error
    );
  }

}
