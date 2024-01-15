# M1IF03 2023 Base

Ce dépôt contiendra les sources de code pour l'UE M1IF03 en 2023-2024.

Il est conseillé d'en faire un fork et de le puller à chaque début de TP. Sinon, vous pouvez aussi puller ce repository séparément et recopier le nouveau code dans le vôtre.

Dans ce cas, vous conserverez la structure par TP et y mettrez le code développé à chaque TP. Y compris les scripts de configuration.

Dans tous les cas, n'oubliez pas d'ajouter vos enseignants en tant que reporters : Lionel Médini pour commencer, nous vous demanderons d'ajouter les intervenants extérieurs quand leurs comptes auront été créés.

## TP7
### Section indiquant la procédure d'installation correspondant aux optimisations réalisées

### Section indiquant les mesures de performance correspondant aux éléments en bleu de ce TP
**Déploiement sur tomcat** :
* temps de chargement de la page html initiale : 73ms
* temps d'affichage de l'appshell : 832ms
* temps d'affichage du chemin critique de rendu (CRP) : 847ms


**Déploiement sur nginx** :
* temps de chargement de la page html initiale : 33ms
* temps d'affichage de l'appshell : 390ms
* temps d'affichage du chemin critique de rendu (CRP) : 400ms

**Avec CDN** :
* temps de chargement de la page html initiale : 30ms Amélioration : 10%
* temps d'affichage de l'appshell : 250ms Amélioration : 36%
* temps d'affichage du chemin critique de rendu (CRP) : 280ms Amélioration : 30%

**Avec async et/ou defer (et cdn)** :
* temps de chargement de la page html initiale : 32ms Amélioration : 4%
* temps d'affichage de l'appshell : 250ms Amélioration : 36%
* temps d'affichage du chemin critique de rendu (CRP) : 280ms Amélioration : 30%

**Réduction du nombre de ressources critiques** :
On a réduit le nombre de resources critiques à l'aide de async.

**Minification (et async et cdn)** :
* temps de chargement de la page html initiale : ms Amélioration : %
* temps d'affichage de l'appshell : ms Amélioration : %
* temps d'affichage du chemin critique de rendu (CRP) : ms Amélioration : %

