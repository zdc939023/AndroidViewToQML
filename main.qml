import QtQuick 2.11
import QtQuick.Window 2.11
import RemoteView 1.0


Window {
    visible: true
    width: 640
    height: 480
    title: qsTr("Hello World")

    RemoteView{
        id:remote
        anchors.fill: parent
    }
}
