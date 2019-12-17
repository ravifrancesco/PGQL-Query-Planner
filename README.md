# PGQL-Query-Planner

## NOTE IMPORTANTI

Hardware benchmark da implementare (probabilmente con pgx)

Cose da chiamare all'inizio del programma:
- Inizializzare Settings con Settings settings = new settings() e poi inizializzare tutte le settings all'interno con i setters. (chiedi pure se hai dubbi, i commenti dovrebbero aiutare)
- Creare un oggetto GraphCreateSketches e settare vertexCsvPath e edgeCsvPath con i setters. Poi chiamare la funzione GraphCreateSketches.graphToSketches(String graphName) con un nome a scelta da dare al grafo (crerà una dircetory con questo nome in cui salvare tutti gli sketch)
- creare un oggetto Statistics (si occupa lui di riprender tutti gli sketches dai file salvati). Il constructore prende il nome del grado (deve essere lo stesso nome con cui si è creato il GraphCreateSketches perchè lo usa per leggere i file degli sketches) e un EstimateBound (guardare il pacchegetto graph.statistics)

Ora puoi inizire a creare i plan degli operatore (guarda i constructor dei vari operatori):
- CartesianProduct prende un solo parent, ricava l'altro da quello (chiedi pure se hai dubbi)
- CommonNeighbor e EdgeMatch prendono un solo parent:
  - Se il parent è un cartesian Product riconoscono che hanno un solo parent
  - Altrimenti automaticamente parent2 = parent1.getParent


## LINK UTILI

### Ravi

Cost Model:
* [Comando EXPLAIN PosgreSQL](https://thoughtbot.com/blog/reading-an-explain-analyze-query-plan)
* [Calcolo costi PostgreSQL](http://shiroyasha.io/the-postgresql-query-cost-model.html)



