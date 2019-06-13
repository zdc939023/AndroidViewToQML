# Android 原生View显示到QML界面上

本例子主要参考与：https://github.com/FalsinSoft/QtAndroidTools 的实现，在使用QtQuick for Android开发时总离不开一些与Android原生的交互，有些原生View在QtQuick中是不具备的或者不完善的，如果必需具备Android原生View的效果，可以通过以上进行借鉴实现

注意事项：在使用SurfaceView嵌入到QML中时需要设置如下两个属性，意思就是设置当前图层为最顶部，否则，看不到。
```
                            mSurfaceView.setZOrderOnTop(true);
                            mSurfaceView.setZOrderMediaOverlay(true);
```

实现效果图如下：

<img src="https://github.com/zdc212133/AndroidViewToQML/blob/master/screenshot/1.png"/>

#### 核心代码如下

1,Java 类

```
import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.Gravity;

public class RemoteView {
    private final Activity mactivity;
    private final ViewGroup mviewGroup;
    private TextView textView;
    private static final int APP_STATE_CREATE = 0;
    private static final int APP_STATE_START = 1;
    private static final int APP_STATE_STOP = 2;
    private static final int APP_STATE_DESTROY = 3;


    public RemoteView(Activity instance){
        mactivity=instance;
        mviewGroup= (ViewGroup) mactivity.getWindow().getDecorView().getRootView();
    }

public void appStateChanged(int newState)
{
    switch(newState)
    {
        case APP_STATE_CREATE:
            createRemoteView();
            break;
        case APP_STATE_DESTROY:
            destroyRemoteView();
            break;
    }
}

    public void createRemoteView(){
        if (textView!=null)return;
        mactivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final FrameLayout.LayoutParams LayoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                textView=new TextView(mactivity);
                textView.setLayoutParams(LayoutParams);

                textView.setText("Simple Android");
                textView.setHeight(400);
                textView.setWidth(400);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundColor(Color.GRAY);
                mviewGroup.addView(textView);
            }
        });
    }


    public void destroyRemoteView(){
        if (textView!=null)return;
        mactivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mviewGroup.removeView(textView);
                textView=null;
            }
        });
    }
}
```
2,C++ 类

```
// .h 文件
#pragma once

#include <QObject>
#include <QQuickItem>
#include <QQmlEngine>
#include <QtAndroid>
#include <QtAndroidExtras>
#include <QtAndroidExtras/QAndroidJniObject>
#include<QtAndroidExtras/QAndroidJniEnvironment>

class RemoteView : public QQuickItem
{
    Q_OBJECT

public:
    RemoteView(QQuickItem*parent=nullptr);
    ~RemoteView();
private slots:
    void appStateChanged(Qt::ApplicationState State);
private :
#ifdef Q_OS_ANDROID
    const QAndroidJniObject m_JavaRemoteView;

    enum APP_STATE
    {
        APP_STATE_CREATE = 0,
        APP_STATE_START,
        APP_STATE_STOP,
        APP_STATE_DESTROY
    };
    void SetNewAppState(APP_STATE NewState);
#endif
};

#endif // REMOTEVIEW_H

//.cpp文件
#include "remoteview.h"
#include <QGuiApplication>
#include <QScreen>

RemoteView::RemoteView(QQuickItem *parent):QQuickItem (parent),
    m_JavaRemoteView("com/tst/simple/RemoteView",
                     "(Landroid/app/Activity;)V",
                     QtAndroid::androidActivity().object<jobject>())
{
    connect(qGuiApp,&QGuiApplication::applicationStateChanged,this,&RemoteView::appStateChanged);
    SetNewAppState(APP_STATE_CREATE);
}

RemoteView::~RemoteView(){
    SetNewAppState(APP_STATE_DESTROY);
}

void RemoteView::appStateChanged(Qt::ApplicationState State){
    SetNewAppState((State == Qt::ApplicationActive) ? APP_STATE_START : APP_STATE_STOP);
}

void RemoteView::SetNewAppState(APP_STATE NewState){
    if(m_JavaRemoteView.isValid()){
       m_JavaRemoteView.callMethod<void>("appStateChanged","(I)V",NewState);
    }
}
```
3，在main.cpp文件中进行注册
```
#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <remoteview.h>

int main(int argc, char *argv[])
{
    QCoreApplication::setAttribute(Qt::AA_EnableHighDpiScaling);

    QGuiApplication app(argc, argv);

    QQmlApplicationEngine engine;
    qmlRegisterType<RemoteView>("RemoteView",1,0,"RemoteView");

    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));
    if (engine.rootObjects().isEmpty())
        return -1;

    return app.exec();
}
```
4,QML界面中的使用
```
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
```
通过如上步骤，即可实现Android原生View显示的QtQuick界面上的实现，可以根据自己的需求进行定制需要的操作。
