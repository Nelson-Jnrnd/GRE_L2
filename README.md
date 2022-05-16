# Graphe et Réseau
# Algorithme de Dijkstra bidirectionnel
* Prof: Jean-François Hêche
* Assistant: Thibaud Franchetti
* Étudiant : Nelson Jeanrenaud

## Introduction
L’objectif de ce laboratoire est d’implémenter les algorithmes de Dijkstra et Dijkstra bidirectionel
dans leur version orientée pour le calcul de plus court chemin entre deux sommets d’un réseau. Et de
documenter les performances des deux algorithmes.

Les classes de lecture de graphes ont étées fournies en début de laboratoire.

## Comparaison des performances
### Méthodologie
Pour mesurer les performances des deux algorithmes je génére 1000 couples de sommets (source,
déstination) aléatoirement et tente de trouver le plus court chemin entre chacun de ces couples avec
les deux algorithmes. Ensuite nous enregistrons l’état de fin des algorithmes pour chaque recherche
(nombre d’itérations, longueur du chemin, poids du chemin). Les résultats suivants ont été obtenus sur
un graphe de 1000 sommets.

### Résultats
![fig1](https://user-images.githubusercontent.com/79466777/168686069-b3dd751d-6262-41c3-b625-15ca7a39fb9e.png)

Premièrement, on peut remarquer une amélioration du nombre d’itérations avec l’algorithme de
Dijkstra bidirectionel. On passe d’une moyenne de 5009 itérations avec Dijkstra à 3366 itérations, soit
une amélioration de 1643 itérations (réduction de 32.8%).
![fig2](https://user-images.githubusercontent.com/79466777/168686081-bfd24614-c4ff-4058-a512-8a367e573942.png)

Assez logiquement, on peut observer que l’amélioration obtenue avec la version bidirectionelle est
plus conséquente quand le plus court chemin est assez long. Cependant la variance est assez grande et
on peut remarquer un chevauchement entre les deux nuages de points. Cela est dû au fait que pour
certaines combinaisons de sommets les arborescences en avant et en arrière se rejoignent en même
temps que la seule arborescence de Dijkstra simple rejoint le sommet destination. Par la même pensée
on peut imaginer des cas ou Dijkstra simple trouvé une solution en moins d’itérations
![fig3](https://user-images.githubusercontent.com/79466777/168686103-6e8bdbc8-ae51-4597-b200-3cdd528bf193.png)

Pour ce test nous n’avons pas obtenu ce cas théorique. Mais nous pouvons remarquer que les gains en
itérations semblent atteindre leur pic à environ 60 sommets de longueur. J’attribue ça au manque de
données de grandes longueurs de chemin et la variance des résultats.
![fig6](https://user-images.githubusercontent.com/79466777/168686123-3619085e-218b-49b6-a79e-2a0ea5a8845a.png)

La distributions des observations est unimodale
![fig4](https://user-images.githubusercontent.com/79466777/168686155-d4dd449c-2583-48ca-8b70-aa3800183be5.png)

Boxplot des observations en les regroupants tout les 5 sommets dans le plus court chemin
Effectivement, La majoritées des obsérvations sont dans l’interval ]30,65]. On remarque également
que l’ecart-type est très grand pour ces valeurs “centrales”. Cela explique facilement la distribution
obtenue en figure 3. Et en observant les boxplots de la figure 5 on peut voir que la médiane augmente
bien avec la longueur du chemin trouvé.
![fig8](https://user-images.githubusercontent.com/79466777/168686168-ad7fd702-e34c-4a4d-a8aa-daf85e0a7887.png)

Distribution des observations du test sur le graphe de 5000 sommets
![fig9](https://user-images.githubusercontent.com/79466777/168686180-4a4fb41c-0b8e-4634-95a6-7a910d250fee.png)

Itérations gagnées en utilisant le graphe de 5000 sommets

En testant les algorithmes avec le graphe de 5000 sommets ont obtient des distributions de formes
identiques. On peut en conclure que les observations avec un grand nombre d’itération au milieu de la
distribution sont dû à la variance et non au performances des algorithmes par rapport sur cette
longueur de chemin.
![fig5](https://user-images.githubusercontent.com/79466777/168686206-1635f660-7ad9-409f-a020-25423df9d6d0.png)

Moyenne du nombre d’itérations par rapport à la taille du chemin trouvé
Pour illustrer cette amélioration, ce graphique montre la moyenne du nombre d’itération des deux
algorithmes. Les données sont regroupées par le pourcentage arondi du plus long chemin traité lors du
test.

### Conclusion
Dijkstra bidirectionnel offre effectivement un gain non négligeable en nombre d’itération par rapport à
la version simple. En moyenne le nombre d’itération est réduit de 32.8% mais ce gain augmente avec
la longueur du plus court chemin. Dans des graphes relativement petits, les coûts en complexité
spatiale de dijkstra bidirectionnel ne vaudra pas, dans beaucoup de cas, le gain en performance.

On a pu également observer que ces gains ne sont pas une garentie absolue et que sur certaines
configuration de graphe; sommet de départ; sommet d’arrivée, Dijkstra simple peut offrir de
meilleures performances.
