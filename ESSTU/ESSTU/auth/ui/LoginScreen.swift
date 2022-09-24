//
//  AuthScreenPattern.swift
//  ESSTU
//
//  Created by Данила Еремин on 17.09.2022.
//

import SwiftUI

struct LoginScreen: View {
    @EnvironmentObject  var authViewModel: AuthViewModel
    @EnvironmentObject  var authNavigation: AuthNavigation
    
   
    
    var body: some View {
       
            VStack(alignment: .leading){
                Text("Добро пожаловать в личный кабинет ВСГУТУ")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .padding(.bottom, 10)
                
                Text("Выполните авторизацию")
                    .font(.title)
                    .fontWeight(.light)
                    .padding(.bottom, 40)
                
                Text("Логин")
                    .font(.title2)
                    .fontWeight(.bold)
                
                TextField("Введите логин", text: $authViewModel.login)
                    .textFieldStyle(.roundedBorder)
                    .disableAutocorrection(true)
                    .textInputAutocapitalization(.never)
                    .padding(.bottom)
                
                
                
                Button(action: {
                    authNavigation.toPasswordScreen()
                }, label: {
                    Text("Продолжить")
                        .frame(maxWidth: .infinity)
                        .font(.title)
                    
                })
                
                
                .buttonStyle(.bordered)
                
                Spacer()
                
            }
            .navigationBarBackButtonHidden(true)
            .padding()
       
            
    }
       
}

struct AuthScreenPattern_Previews: PreviewProvider {
    static var previews: some View {
        LoginScreen()
    }
}

