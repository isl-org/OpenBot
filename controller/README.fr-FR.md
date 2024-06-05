# Contrôleurs OpenBot

<p align="center">
  <a href="README.md">English</a> |
  <a href="README.zh-CN.md">简体中文</a> |
  <a href="README.de-DE.md">Deutsch</a> |
  <span>Français</span> |
  <a href="README.es-ES.md">Español</a>
</p>

Il existe plusieurs façons de contrôler le robot OpenBot.

## Contrôleur Bluetooth

La manière la plus simple de contrôler le robot est de connecter une manette de jeu via Bluetooth. La plupart des manettes de jeu BT devraient fonctionner. Nous avons testé la [manette PS4](https://www.amazon.de/-/en/Sony-Dualshock-Gamepad-Playstation-Black/dp/B01LYWPQUN), la [manette XBox](https://www.amazon.de/-/en/QAT-00002/dp/B07SDFLVKD) et plusieurs manettes de rechange telles que la [X3](https://www.amazon.com/Controller-Wireless-Joystick-Bluetooth-Android/dp/B08H5MM64P).

## [Contrôleur Node.js](node-js)

Ce contrôleur nécessite que votre téléphone et un ordinateur (par exemple, un ordinateur portable, un Raspberry PI) soient connectés au même réseau WiFi. Après une connexion réussie, vous obtenez un flux vidéo en direct à faible latence et pouvez utiliser votre clavier pour contrôler le robot depuis le navigateur.

Remerciements spéciaux à Ivo Zivkov [izivkov@gmail.com](mailto:izivkov@gmail.com) pour le développement.

## [Contrôleur Python](python)

Ce contrôleur nécessite que votre téléphone et un ordinateur (par exemple, un ordinateur portable, un Raspberry PI) soient connectés au même réseau WiFi. Après une connexion réussie, vous obtenez un flux vidéo RTSP en direct et pouvez utiliser votre clavier pour contrôler le robot depuis le terminal. Vous pouvez utiliser le script Python comme modèle pour développer votre propre contrôleur.

Remerciements spéciaux à Ivo Zivkov [izivkov@gmail.com](mailto:izivkov@gmail.com) pour le développement.