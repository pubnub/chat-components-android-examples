# Getting Started with Message Reactions

`getting-started-with-reactions` is a basic 1:1 chat application that features a message input field
and a list of messages that pile up on the screen as you send them. In addition to this basic
functionality, the app contains 6 built-in message reactions that you can add to the messages by
long-tapping on a message and selecting emojis from the bottom menu that slides up from the bottom
edge of the screen when triggered.

<img src="../assets/getting-started-with-reactions.png" alt="Getting Started app for Android" style="width:300px"/> 

## Prerequisites

This application uses [PubNub Kotlin SDK](https://github.com/pubnub/kotlin) (>= 7.3.2) for chat
components and [Jetpack Compose](https://developer.android.com/jetpack/compose) as the UI Toolkit.

To use the app, you need:

* [Android Studio](https://developer.android.com/studio) (>= Dolphin 2021.3.1)
* PubNub account on [Admin Portal](https://dashboard.pubnub.com/)

## Usage

Read the [Message Reactions](https://www.pubnub.com/docs/chat/community-supported/android/message-reactions#enable-reactions)
document to learn how to use the app and better understand the logic behind it.

## Features

The `getting-started-with-reactions` app showcases these components and features:

* [MessageInput](https://www.pubnub.com/docs/chat/community-supported/android/ui-components#messageinput)
* [MessageList](https://www.pubnub.com/docs/chat/community-supported/android/ui-components#messagelist)
* [Message reactions](https://www.pubnub.com/docs/chat/community-supported/android/message-reactions)
* Message Menu (with the copying option)
