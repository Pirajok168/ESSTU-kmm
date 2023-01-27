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
 



protocol AuthStateModel{
    var token: Token? { get }
    var isLoading: Bool { get }
    var error: ResponseError? { get }
    var toAuthScreen: Bool { get }
    
    var login: String { get }
    var password: String { get }
}

protocol IntentAuth{
    func onAutorise()
    func onRestoreSession()
}

class AuthViewModel: ObservableObject, AuthStateModel{
    @Published private(set) var token: Token? = nil
    @Published private(set) var isLoading: Bool = false
    @Published private(set) var error: ResponseError? = nil
    @Published var login: String = ""
    @Published var password: String = ""
    @Published private(set) var toAuthScreen: Bool = false
    
    private let repo: IAuthRepository
    
    init(repo: IAuthRepository){
        self.repo = repo
    }
}

extension AuthViewModel: IntentAuth{
    
    
    func onAutorise() {
        DispatchQueue.main.async {
            self.isLoading = true
        }
        
        repo.auth(login: login, Password: password) { response, error in
            DispatchQueue.main.async {
                switch response{
                case let data as ResponseSuccess<Token>:
                    self.token = data.data
                    self.error = nil
                case let error as ResponseError_<Token>:
                    self.token = nil
                    self.error = error.error
                default:
                    print("Ошибка")
                }
                
                self.isLoading = false
            }
        }
    }
    
    func onRestoreSession() {
        DispatchQueue.main.async {
            self.isLoading = true
        }
        
        repo.refreshToken { response, error in
            print("\(String(describing: response))")
            DispatchQueue.main.async {
                switch response{
                case let data as ResponseSuccess<Token>:
                    self.token = data.data
                    self.error = nil
                case let error as ResponseError_<Token>:
                    self.token = nil
                    self.error = error.error
                    self.toAuthScreen = true
                    print("Ошибка какая то \(String(describing: error.error.message))")
                default:
                    print("Ошибка")
                }
                
                self.isLoading = false
            }
            
        }
        
    }
    
    
    
}




