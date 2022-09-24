//
//  AuthViewModel.swift
//  ESSTU
//
//  Created by Данила Еремин on 18.09.2022.
//

import Foundation
import shared
import Combine
import SwiftUI
 



protocol AuthState{
    var token: Token? { get }
    var isLoading: Bool { get }
    var error: ResponseError? { get }
    var login: String { get }
    var isError: Bool { get  set }
    var password: String { get }
}

protocol AuthEvent{
    func passLogin(login: String)
    func authorise()
    func onRestoreSession()
}





class AuthViewModel: ObservableObject, AuthState{
    
    @Published var isError: Bool = false
    @Published var isLoading: Bool = false
    @Published var login: String = ""
    @Published var password: String = ""
    
    @Published var routing: String = ""
    
    
    @Published private(set) var token: Token?
    private(set) var error: ResponseError?
    
    private let repo: IAuthRepository = ESSTUSdk().repoAuth.authModule
    
   
}

extension AuthViewModel: AuthEvent{
    func passLogin(login: String) {
        self.login = login
    }
    
    func onRestoreSession() {
        isLoading = true
        repo.refreshToken(){
            response, error in
            DispatchQueue.main.async {
                if response is ResponseSuccess{
                    self.token = response?.data
                }else if response is ResponseError_{
                    self.error = response?.error
                }
            }
        }
        isLoading = false
    }
    
    
    
    func authorise() {
        
        repo.auth(login: login, Password: password) { response, error in
            DispatchQueue.main.async {
                if response is ResponseSuccess{
                    self.token = response?.data
                    
                }else if response is ResponseError_{
                    self.error = response?.error
                    self.isError = true
                }
            }
        }
    }
}



