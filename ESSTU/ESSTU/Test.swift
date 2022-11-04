//
//  Test.swift
//  ESSTU
//
//  Created by Данила Еремин on 01.10.2022.
//

import SwiftUI

struct Test: View {
    
    @State private var selectedTab: TypeMessages = .dialogs
    
    @Namespace var namespace

    var body: some View {
       
        NavigationStack{

            ScrollView{
                LazyVStack{
                    ForEach(0..<100, id: \.self){
                        index in
                        Text("\(index)")
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .background(.blue)
                            .padding()
                        Divider()
                    }
                }
            }
            .overlay{
                
                    
                ZStack{
                    Color.clear.background(
                        .ultraThinMaterial)
                    VStack{
                        Text("Мессенджер")
                            .frame(maxWidth: .infinity, alignment: .center)
                            .font(.title3)
                        
                        ScrollView(.horizontal, showsIndicators: false){
        
                            HStack(){
                                ForEach(TypeMessages.allCases, id: \TypeMessages.self){
                                    index in
        
                                    VStack(){
                                        Button(action: {
                                            withAnimation{
                                                selectedTab = index.id
                                            }
                                        }){
                                            Text(index.title)
                                        }
                                        .frame(maxHeight: 50, alignment: .top)
                                        
                                        
                                        
                                        
                                        if selectedTab == index{
                                            Rectangle()
                                                .frame(height: 2)
                                                .matchedGeometryEffect(id: "title", in: namespace, isSource: true)
                                                .foregroundColor(.blue)
                                        }
                                      
                                    }
                                    .frame(maxHeight: 50, alignment: .bottom)
                                    
                                    .padding(.horizontal)
                                }
                                .buttonStyle(.plain)
                            }
                        }
                    }
                    
                }
                .frame(height: 80)
                .frame(maxHeight: .infinity, alignment: .top)
                
                
            }

                
                
            
        }
    }

}

struct NavBar: View {
    
   

    var body: some View {
       
        HStack{
            Text("HelloWorld")
                .font(.largeTitle.bold())
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding()
        }
        .frame(maxHeight: .infinity, alignment: .top)
    }

}



struct Test_Previews: PreviewProvider {
    static var previews: some View {
       
            Test()
              
    }
}
