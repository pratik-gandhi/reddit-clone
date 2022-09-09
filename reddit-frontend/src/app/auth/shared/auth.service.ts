import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SignupRequestPayload } from 'src/app/model/signup-request.payload';
import { Observable } from 'rxjs';
import { LoginRequestPayload } from 'src/app/model/login-request.payload';
import { LoginResponsePayload } from 'src/app/model/login-response.payload';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient) { }

  signup(singupRequestPayload: SignupRequestPayload): Observable<any> {
    return this.httpClient.post("http://localhost:8080/api/v1/auth/signup", singupRequestPayload, { responseType: "text" });
  }

  login(loginRequestPayload: LoginRequestPayload): Observable<LoginResponsePayload> {
    return this.httpClient.post("http://localhost:8080/api/v1/auth/login", loginRequestPayload, { responseType: "json" })
  }
}
