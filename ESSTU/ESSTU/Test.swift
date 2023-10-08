import SwiftUI
import Combine

struct Message: Identifiable{
    var id: String = UUID().uuidString
    var message: String
    var isReply: Bool = false
    var emojiValue: String = ""
    var isEmojiAdded: Bool = false
}

struct BoundsPreferences: PreferenceKey{
    static var defaultValue: [String: Anchor<CGRect>] = [:]
    
    static func reduce(value: inout [String : Anchor<CGRect>], nextValue: () -> [String : Anchor<CGRect>]) {
        value.merge(nextValue()){
            $1
        }
    }
}

struct ContentView: View {
    
    @State var chat: [Message] = [
        Message(message: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s"),
        Message(message: "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", isReply: true),
    ]
    
    @State var selectedMessage: Message?
    
    var body: some View {
        ScrollView(.vertical, showsIndicators: false){
            ForEach(0..<100){ parametr in
                Text("parametr")
            }
        }
    }
    
    @ViewBuilder
    func ChatView(message: Message) -> some View{
        Text(message.message)
            .padding(15)
            .background(message.isReply ? Color.gray : Color.blue)
            .foregroundColor(message.isReply ? .black : .white)
            .clipShape(RoundedRectangle(cornerRadius:  15, style: .continuous))
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

