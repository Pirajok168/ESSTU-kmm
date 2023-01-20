import SwiftUI
import Combine

struct ContentView: View {
    
    
    var body: some View {
        List(0...5, id: \.self){
            _ in
            Text("Hello world")
                 .preferredColorScheme(.dark)
        }
     
        
    }

    
}



struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
