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
        NavigationView{
            ScrollView(.vertical, showsIndicators: false){
                VStack(spacing: 12){
                    ForEach(chat){
                        chat in
                        
                        ChatView(message: chat)
                            .anchorPreference(key: BoundsPreferences.self, value: .bounds, transform: { anchor in
                                return [chat.id : anchor]
                            })
                            .padding(chat.isReply ? .leading : .trailing, 60)
                            .onLongPressGesture{
                                withAnimation(.easeOut){
                                    selectedMessage = chat
                                }
                            }
                    }
                }
                .padding()
                
            }
            .navigationTitle("Мессенджер")
        }
        .overlay{
            if selectedMessage != nil {
                Rectangle()
                    .fill(.ultraThinMaterial)
                    .ignoresSafeArea()
                    .onTapGesture {
                        withAnimation{
                            selectedMessage = nil
                        }
                    }
            }
        }
        .overlayPreferenceValue(BoundsPreferences.self){
                values in
            if let selectedMessage, let preference = values.first(where: { item in
                item.key == selectedMessage.id
            }){
                GeometryReader{
                    proxy in
                    let rect = proxy[preference.value]
                    ChatView(message: selectedMessage)
                        .frame(width: rect.width, height: rect.height)
                        .offset(x: rect.minX, y: rect.minY)
                }
                
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

