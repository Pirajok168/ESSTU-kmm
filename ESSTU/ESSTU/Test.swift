import SwiftUI
import Combine

struct ContentView: View {
    @State private var offset: CGFloat = 0
    
    var body: some View {
        VStack{
            Text("123")
                .overlay{
                    GeometryReader{
                        reader -> AnyView  in
                        offset = reader.frame(in: .named("vs")).width
                        return AnyView(Rectangle()
                            .frame(width: reader.frame(in: .named("vs")).width))
                    }
                }
            
            Text("\(offset)")
               
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .coordinateSpace(name: "vs")
        
    }

    
}

struct ViewOffsetKey: PreferenceKey {
    typealias Value = CGFloat
    static var defaultValue = CGFloat.zero
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value += nextValue()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
