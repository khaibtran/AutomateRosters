import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class TransportationService{

    private url:string = 'http://localhost:8080/api/v1/transportation';
    private http:HttpClient;

    constructor(http:HttpClient) {
        this.http = http;
    }

    generateTransportationUpdate():Observable<string> {
        return this.http.get<string>(this.url);
    }
}