//
//  Test.swift
//  ESSTU
//
//  Created by Данила Еремин on 11.11.2022.
//

import SwiftUI


struct DemoScrollViewOffsetView: View {
    @State private var offset = CGFloat.zero
    var body: some View {
        ScrollView {
            VStack {
                ForEach(0..<100) { i in
                    Text("Item \(i)").padding()
                }
            }.background(GeometryReader {
                Color.clear.preference(key: ViewOffsetKey.self,
                    value: -$0.frame(in: .named("scroll")).origin.y)
            })
            .onPreferenceChange(ViewOffsetKey.self) { print("offset >> \($0)") }
        }.coordinateSpace(name: "scroll")
    }
}

struct ViewOffsetKey: PreferenceKey {
    typealias Value = CGFloat
    static var defaultValue = CGFloat.zero
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value += nextValue()
    }
}


struct Test: View {
   @State private var isShowing = true
    
    @State private var y: CGFloat = 1

    @State private var offset = CGFloat.zero
    
    @State private var defaultY = CGFloat.zero
    
    var body: some View {
        VStack{
           Button(action: {
               withAnimation{
                   //isShowing.toggle()
                   if y == 0 {
                       y = 1
                   }else{
                       y = 0
                   }

               }
           }, label: {
               Text("Click")
           })


            Rectangle()
                .fill(Color.red)
                .frame(width: 200,height: y)
                
                .animation(.linear(duration: 1), value: y)
            
            ScrollView{
                LazyVStack{
                    ForEach(0..<100, id: \.self){
                        i in
                        Text("TEst")
                            .frame(maxWidth: .infinity)
                    }
                }
            }.overlay{
                GeometryReader{ proxy -> Color in
                    
                    let minY = proxy.frame(in: .global).minY
                    
                    print(minY)
                    
                    return Color.clear
                }
            }

            
          
        }
        
      
       
    }
}

struct Test_Previews: PreviewProvider {
    static var previews: some View {
        Test()
    }
}
