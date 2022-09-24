//
//  AuthScreenPattern.swift
//  ESSTU
//
//  Created by Данила Еремин on 17.09.2022.
//

import SwiftUI

struct LoginScreen: View {
    @State private var login: String = ""
    @State private var firstViewIsOn = false
    @ObservedObject private var authModel: AuthViewModel = AuthViewModel()
    
   
    
    var body: some View {
        NavigationView(){
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
                
                TextField("Введите логин", text: $authModel.login)
                    .textFieldStyle(.roundedBorder)
                    .disableAutocorrection(true)
                    .textInputAutocapitalization(.never)
                    .padding(.bottom)
                
              
                
                NavigationLink(isActive: $firstViewIsOn) {
                    PasswordScreen(authModel: authModel)
                } label: {
                    Button(action: {
                        firstViewIsOn.toggle()
                    }, label: {
                        Text("Продолжить")
                            .frame(maxWidth: .infinity)
                            .font(.title)
                        
                    })
                }
                
                
                .buttonStyle(.bordered)
                .navigationBarTitleDisplayMode(.inline)
                Spacer()
                
            }.padding()
        }
            
    }
       
}

struct AuthScreenPattern_Previews: PreviewProvider {
    static var previews: some View {
        LoginScreen()
    }
}
