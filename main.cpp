#include <QGuiApplication>
#include <QQmlApplicationEngine>
//#include <androidtextview.h>
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
