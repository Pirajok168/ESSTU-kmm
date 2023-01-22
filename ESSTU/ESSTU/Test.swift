import SwiftUI
import Combine

struct ContentView: View {
    


    var body: some View {
       Text("Hello world")
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .bottom)
            .overlay{
                Text("Hello world")
                    
            }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

