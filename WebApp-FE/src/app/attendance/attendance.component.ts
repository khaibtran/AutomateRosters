import { Component, OnInit } from '@angular/core';
import { AttendanceService } from './service/attendance.service';


@Component({
  selector: 'app-attendance',
  templateUrl: './attendance.component.html',
  styleUrls: ['./attendance.component.css']
})
export class AttendanceComponent implements OnInit {

  private attendanceService:AttendanceService;

  constructor(attendanceService:AttendanceService) { 
    this.attendanceService = attendanceService;
    
  }

  ngOnInit() {
  }

}
