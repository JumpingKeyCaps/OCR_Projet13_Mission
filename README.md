<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->


<!-- PROJECT LOGO -->
<br />
<div align="center">
    <img src="app/src/main/ic_launcher-playstore.png" alt="Logo" width="200" height="200">

<h3 align="center">Hexagonal Games</h3>

  <p align="center">
    The Android app Hexagonal Games
  </p>
</div>

#  Hexagonal Games 

Une application Android communautaire pour les passionnés de jeux de stratégie. Elle permet de publier du contenu, commenter, recevoir des notifications et gérer son compte, le tout grâce à Firebase et Jetpack Compose.

---

## 🚀 Présentation

**Contexte** :  
Développement d’une application mobile type "réseau social simplifié", intégrant des services Firebase (authentification, base de données, stockage, notifications) et une interface entièrement en **Jetpack Compose**.

**Mission** :  
- Permettre à des utilisateurs de créer un compte, se connecter, publier du contenu, commenter et liker.
- Utiliser une architecture MVVM claire, testable et maintenable.
- Gérer l’envoi de notifications Firebase Cloud Messaging.
- Exploiter les composants modernes d’Android (Compose, ViewModel, Hilt, Coroutines, Flows).
- Consulter un **fil d’actualité** riche en publications, mèmes et photos autour des jeux de stratégie.
- Interagir via un système de **commentaires** robuste sur chaque publication.

---

## 📸 Fonctionnalités Clés



- Authentification via **FirebaseUI Auth** (email/mot de passe).
- Création de publications avec sélection de média via **Photo Picker**.
- Affichage des publications en temps réel depuis **Firestore**.
- Stockage des médias dans **Firebase Storage**.
- Affichage détaillé d’une publication avec ses **commentaires**.
- Ajout de commentaires par les utilisateurs connectés.
- Écran de **gestion de compte** (déconnexion + suppression).
- **Notifications push** via Firebase Cloud Messaging.
- **Gestion du compte** : déconnexion, suppression
- **Tests unitaires** (JUnit5 + MockK)
- **Architecture MVVM propre et scalable**

---

## 🛠️ Stack Technique

| Composant            | Techno                          |
|----------------------|---------------------------------|
| UI                   | Jetpack Compose                 |
| Architecture         | MVVM (ViewModel, UseCase, etc.) |
| Auth & Backend       | Firebase Auth, Firestore        |
| Images & Upload      | Firebase Storage + Photo Picker |
| Notifications        | Firebase Cloud Messaging        |
| Test                 | JUnit 5 + MockK                 |
| Langage principal    | Kotlin                          |
| Min SDK              | Android 8.0 (API 26)            |

---



## 📈 Tâches réalisées

| Étape | Objectifs | Résultats |
| :--- | :--- | :--- |
| **Configuration Firebase** | Auth, Firestore, Storage, FCM | Intégration complète et fonctionnelle |
| **Architecture Hexagonale** | Séparation Domain / Data / UI | Clean, scalable, testable |
| **Interface Compose** | UI moderne, réactive, fluide | UX intuitive et responsive |
| **Authentification** | Création / Connexion comptes | Sécurisée via Firebase |
| **Création de post** | Ajout photo + description | Stockage image + métadonnées |
| **Commentaires & Likes** | Interaction sociale | Base Firestore bien structurée |
| **Notifications push** | Réception en temps réel | Firebase Cloud Messaging |
| **Profil utilisateur** | Données persos + logout | Persistance avec DataStore |
| **Documentation** | README clair et complet | ✅ Conforme aux standards |



---

##  Tests
-Tous les UseCases et ViewModels sont testés avec JUnit 5 et MockK

---

## 🎯 Résultat final

L'application Hexagonal Games offre une expérience utilisateur fluide et intuitive.
Grâce à l'intégration robuste de Firebase et l'architecture MVVM, elle garantit une performance optimale et une maintenance facilitée.
Les utilisateurs peuvent interagir facilement avec le contenu, recevoir des notifications pertinentes et gérer leurs publications, créant ainsi une communauté dynamique.

---

![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![MVVM](https://img.shields.io/badge/MVVM-000000?style=for-the-badge&logo=android&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit%205-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![MockK](https://img.shields.io/badge/MockK-F16529?style=for-the-badge&logo=mockk&logoColor=white)

