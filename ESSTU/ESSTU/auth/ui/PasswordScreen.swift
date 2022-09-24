
//
//  PasswordScreen.swift
//  ESSTU
//
//  Created by Данила Еремин on 18.09.2022.
//

import SwiftUI


struct PasswordScreen: View {
   
    @EnvironmentObject var authViewModel: AuthViewModel
    @EnvironmentObject var authNavigation: AuthNavigation
    
    @State var data: String = ""
    
    
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
                
                Text("Пароль")
                    .font(.title2)
                    .fontWeight(.bold)
                
                TextField("Введите пароль", text: $authViewModel.password)
                    .textFieldStyle(.roundedBorder)
                    .disableAutocorrection(true)
                    .textInputAutocapitalization(.never)
                    .padding(.bottom)
                
                Button(action: {
                    authViewModel.authorise()
                }, label: {
                    Text("Авторизироваться")
                        .frame(maxWidth: .infinity)
                        .font(.title)
                    
                })
                .alert(authViewModel.error?.message ?? "Неизвестная ошибка", isPresented: $authViewModel.isError) {
                           Button("OK", role: .cancel) { }
                       }
                .buttonStyle(.bordered)
                
                
                Spacer()
                
            }
            .navigationBarBackButtonHidden(true)
            .toolbar{
                ToolbarItem(placement:.navigationBarLeading){
                    Button{
                        authNavigation.path.removeLast()
                    } label: {
                        Text("Back")
                    }
                }
            }
            .padding()
        
        
        
    }
}

struct PasswordScreen_Previews: PreviewProvider {
    static var previews: some View {
        PasswordScreen()
    }
}

