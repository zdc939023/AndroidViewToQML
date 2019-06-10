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
