//
//  AuthViewModel.swift
//  ESSTU
//
//  Created by Данила Еремин on 18.09.2022.
//

import Foundation
import shared
import Combine


protocol AuthState{
    var token: Token? { get }
    var isLoading: Bool { get }
    var error: ResponseError? { get }
    var login: String { get }
    var isError: Bool { get  set }
}

protocol AuthEvent{
    func passLogin(login: String)
    func authorise()
}



class AuthViewModel: ObservableObject, AuthState{
    @Published var isError: Bool = false

    private(set) var token: Token?
    @Published  var isLoading: Bool = false
    private(set) var error: ResponseError?
    @Published var login: String = ""
    @Published var password: String = ""
    @Published var data: String = ""
    
    private let repo: IAuthRepository = ESSTUSdk().repoAuth.authModule
    
   
}

extension AuthViewModel: AuthEvent{
    func passLogin(login: String) {
        self.login = login
    }
    
    
    
    func authorise() {
        
        repo.auth(login: login, Password: password) { response, error in
            DispatchQueue.main.async {
                if response is ResponseSuccess{
                    
                }else if response is ResponseError_{
                    self.error = response?.error
                    self.isError = true
                }
            }
    
        }
    }
}



