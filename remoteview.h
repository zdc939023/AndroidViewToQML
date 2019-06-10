#ifndef REMOTEVIEW_H
#define REMOTEVIEW_H
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
