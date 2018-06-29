import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';

import { Secure } from './secure';
import { headersToString } from 'selenium-webdriver/http';

@Injectable()
export class AttendanceService {

    private url:string = 'https://crescentcity.schoolrunner.org/api/v1/class-absence-totals';
    private http:HttpClient;
    private credentials = new Secure;

    httpOptions = {
        headers: new HttpHeaders({
            'Authorization': 'Basic ' + btoa(this.credentials.user() + ':' + this.credentials.pass)
        }),
        params: new HttpParams()
    };

    constructor(http:HttpClient) {
        this.http = http;
    }

    getMissingAttendance(date:string):Observable<Object> {
        this.httpOptions.params.set('min_date', date).set('max_date', date);
        return this.http.get(this.url, this.httpOptions);
    }
}