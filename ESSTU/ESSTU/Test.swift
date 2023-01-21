import SwiftUI
import Combine

struct ContentView: View {
    @State var rotation = 0.0
    var body: some View {
        VStack{
            Text("Hello World")
                .padding(.bottom, 1)
            Text("Hello World")
            Image(systemName: "message")
        }
    }

    
}



struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
