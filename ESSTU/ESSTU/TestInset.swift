//
//  TestInset.swift
//  ESSTU
//
//  Created by Данила Еремин on 24.01.2023.
//

import SwiftUI

struct TestInset: View {
    @State var offser: CGFloat = .zero
    let topEdge: CGFloat
    var body: some View {
        
        ScrollView(.vertical){
            VStack{
                Text("\(offser)")
            }
            .frame(maxWidth: .infinity)
            .padding(.top, topEdge)
            .background{
                GeometryReader{
                    proxy -> Color in
                    DispatchQueue.main.async {
                        self.offser = proxy.frame(in: .global).minY
                    }
                    return Color.clear
                }
            }
            
        }
        .navigationTitle("123")
            
        
    }
}

struct TestInset_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView{
            GeometryReader{ proxy in
                let topEdge = proxy.safeAreaInsets.top
                TestInset(topEdge: topEdge)
                    .ignoresSafeArea()
            }
        }
    }
}
