# Covoiturage USTHB

Application Java en console pour gérer le covoiturage au sein de l’USTHB. Elle permet notamment de gérer les utilisateurs (étudiants, enseignants et ATS), les trajets et les courses, ainsi que les inscriptions de passagers et les évaluations.

## Prérequis

- JDK 23 ou supérieur

## Lancer le projet

### Avec Eclipse

1. Importer le dossier comme **projet Eclipse existant**.
2. Vérifier que le projet utilise un JDK 23 ou supérieur.
3. Exécuter `main.Main` depuis `src/main/Main.java`.

### Avec PowerShell

Depuis la racine du projet :

```powershell
javac -d bin (Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
java -p bin -m CovoiturageUSTHB/main.Main
```

Le programme affiche un menu dans le terminal. Suivre les invites pour choisir une opération.

## Structure

- `src/main` : point d’entrée et interactions avec le menu.
- `src/model` : modèles des utilisateurs, courses, trajets et évaluations.
- `src/DateBase` : services et sauvegarde des données.
- `src/Enum` : énumérations utilisées par l’application.

## Données

Les sauvegardes sérialisées sont placées dans le dossier `data/`. **Actuellement, le démarrage de l’application supprime les fichiers de sauvegarde existants puis initialise des données de test.** Les données créées pendant une exécution ne sont donc pas conservées au prochain lancement.
