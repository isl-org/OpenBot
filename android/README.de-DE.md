# Android Apps

<p align="center">
  <a href="README.md">English</a> |
  <a href="README.zh-CN.md">简体中文</a> |
  <span>Deutsch</span> |
  <a href="README.fr-FR.md">Français</a> |
  <a href="README.es-ES.md">Español</a>
</p>

## Funktionen

Klicken Sie auf die untenstehenden Links, um mehr über die Funktionen der Apps zu erfahren.

- [Robot App](robot/README.md)
- [Controller App](controller/README.md)

## Installation der Apps

Der einfachste Weg, eine der Apps zu erhalten, ist, sie direkt auf das Telefon herunterzuladen, indem Sie den entsprechenden QR-Code verwenden. Wenn Sie den Browser auf Ihrem Telefon verwenden, können Sie auch einfach auf den QR-Code klicken. Sie können dann die APK auf Ihrem Telefon öffnen und [installieren](https://www.lifewire.com/install-apk-on-android-4177185). Beachten Sie, dass die APK nur mit einem Debug-Schlüssel signiert ist.

<table style="width:100%;border:none;text-align:center">
  <tr>
    <td>  <a href="https://app.openbot.org/robot" target="_blank">
    <img alt="🤖 App" width="50%" src="../docs/images/robot_app_qr_code.png" />
  </a>
    </td>
    <td>
  <a href="https://app.openbot.org/controller" target="_blank">
    <img alt="🎮 App" width="50%" src="../docs/images/controller_app_qr_code.png" />
  </a>
      </td>
  </tr>
  <tr>
    <td>🤖 App</td>
    <td>🎮 App</td>
  </tr>
</table>

Alternativ können Sie die APKs aus den Assets einer beliebigen [Veröffentlichung](https://github.com/intel-isl/OpenBot/releases) herunterladen. Wenn Sie die neueste App aus dem Master-Branch möchten, können Sie sie auch aus den Build-Artefakten [hier](https://github.com/intel-isl/OpenBot/actions?query=workflow%3A%22Java+CI+with+Gradle%22) herunterladen. Beachten Sie, dass sie möglicherweise nicht stabil ist. Wenn Sie später Änderungen an der App vornehmen möchten, folgen Sie den untenstehenden Schritten, um die App zu kompilieren und auf Ihrem Telefon zu installieren.

## Apps bauen

### Voraussetzungen

- [Android Studio Electric Eel | 2022.1.1 oder später](https://developer.android.com/studio/index.html) zum Bauen und Installieren der APKs.
- Android-Gerät und Android-Entwicklungsumgebung mit mindestens API 21.
- Derzeit verwenden wir API 33 als Compile-SDK und API 32 als Target-SDK. Es sollte automatisch installiert werden, aber falls nicht, können Sie das SDK manuell installieren. Gehen Sie zu Android Studio -> Einstellungen -> Erscheinungsbild & Verhalten -> Systemeinstellungen -> Android SDK. Stellen Sie sicher, dass API 33 aktiviert ist, und klicken Sie auf Anwenden.

![Android SDK](../docs/images/android_studio_sdk.jpg)

### Build-Prozess

1. Öffnen Sie Android Studio und wählen Sie *Ein bestehendes Android Studio-Projekt öffnen*.
2. Wählen Sie das Verzeichnis OpenBot/android und klicken Sie auf OK.
3. Wenn Sie die [OpenBot-App](app/README.md) installieren möchten, stellen Sie sicher, dass die *app*-Konfiguration ausgewählt ist. Wenn Sie die [Controller-App](controller/README.md) installieren möchten, wählen Sie die *controller*-Konfiguration. Bestätigen Sie den Gradle-Sync, falls erforderlich. Um einen Gradle-Sync manuell durchzuführen, klicken Sie auf das Gradle-Symbol.
  ![Gradle Sync](../docs/images/android_studio_bar_gradle.jpg)
4. Verbinden Sie Ihr Android-Gerät und stellen Sie sicher, dass USB-Debugging in den [Entwickleroptionen](https://developer.android.com/studio/debug/dev-options) aktiviert ist. Abhängig von Ihrer Entwicklungsumgebung könnten [weitere Schritte](https://developer.android.com/studio/run/device) erforderlich sein. Sie sollten Ihr Gerät jetzt in der Navigationsleiste oben sehen.
  ![Phone](../docs/images/android_studio_bar_phone.jpg)
5. Klicken Sie auf die Schaltfläche Ausführen (der grüne Pfeil) oder wählen Sie Ausführen > 'android' ausführen aus dem oberen Menü. Möglicherweise müssen Sie das Projekt mit Build > Projekt neu erstellen neu erstellen.
  ![Run](../docs/images/android_studio_bar_run.jpg)
6. Wenn Sie gefragt werden, ob Sie Instant Run verwenden möchten, klicken Sie auf *Ohne Instant Run fortfahren*.

### Fehlerbehebung

#### Versionen

Wenn Sie eine Nachricht wie `Das Projekt verwendet eine inkompatible Version (AGP 7.4.0) des Android Gradle-Plugins. Die neueste unterstützte Version ist AGP 7.3.0` erhalten, müssen Sie Android Studio aktualisieren oder Ihr Gradle-Plugin downgraden. Sie können mehr über die Versionskompatibilität zwischen Android Studio und dem Gradle-Plugin [hier](https://developer.android.com/studio/releases/gradle-plugin#android_gradle_plugin_and_android_studio_compatibility) lesen.
