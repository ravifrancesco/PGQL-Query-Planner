# PGQL-Query-Planner

## COST MODEL

### HARDWARE BENCHMARKING

Da fare al primo avvio, poi non influenza le statistiche e i dati ricavati da questo benchmarking possono ritenersi costanti. I dati possono o essere calcolati o settati come costanti (tipo PostegreSQL)

1. Calcoli base su hardware
   - Dimensione blocchi di memoria
   - Velocità operatori base (=,<,>,...)
   - Overhead di startup worker paralleli (?)
   - Coefficente di costo operatori paralleli (?)

2. Accesso sequenziale a dati
   - Accesso a blocchi sequenziali
   - Accesso a tuple sequenziali in un blocco

3. Accesso random a dati
   - Accesso a blocchi random
   - Accesso a tuple random in un blocco

4. Costo accesso a proprietà
   - Accesso a proprietà dei vertex
   - Accesso a proprietà degli edges

### STIMA CARDINALITA' OPERATORI

Per calcolare i costi dei singoli operatori, è necessario calcolare la loro selettività. Da fare spesso per tenere le statistiche aggiornate. La selettività può essere poi interpretata come statistica per calcolare la cardinalità di un operatore.

Ad esempio se MATCH a.id ha selettività s = 0.01 e ci sono 1000 vertex a possiamo assumere che la cardinalità sarà:

|a| = s * 1000 = 10

Bisogna fare attenzione alle seguenti assunzioni:

  - I dati non sono per forza indipendenti (ad esempio una macchina prodotta da Jeep sarà probabilmente un fuoristrada)
  - I dati non sono distribuiti uniformemente (ci saranno più persone in una città piuttosto che in un paese)

Per calcolare in modo intelligente la selettività possiamo fare ricorso alla libreria:

[DataSketches](https://datasketches.github.io/docs/TheChallenge.html) di Yahoo

Per poi utilizzare le statistiche per calcolare la probabilità. La libreria è ottimizzata per essere veloce e resource-friendly.

### AGGIORNAMENTO STATISTICHE GRAFO (DA VEDERE IMPLEMENTAZIONE)

L'implementazione di DataSketch è da vedere!

Un'idea per tenere aggiornate le statistiche sul grafo in modo efficiente e utile è quello di sfruttare il merge di data sketch:
  1. Creare uno sketch all'avvio del database sullo stream di lettura delle tabelle
  2. Creare un numero *k* o un tempo *t* per aggiornamento [1]
  3. Tenere aperto lo stream e continuare a leggere eventuali tuple in entrata o in uscita aggiornando un nuovo sketch
  4. Ogni *k* dati letti o passato un tempo *t* fare merge tra sketch del grafo e quello nuovo

[1] Il tempo *t* potrebbe essere settato dall'utente; il numero *k* potrebbe essere settato dall'utente

#### Datasketches

La lbreria Datasketches per Java può essere trovata [qui](https://github.com/apache/incubator-datasketches-java). C'è da capire se esiste una documentazione dei comandi java.

### CALCOLO COSTO operatori

Da determinare in base ai dati ottenuti.

## LINK UTILI

### Ravi

Cost Model:
* [Comando EXPLAIN PosgreSQL](https://thoughtbot.com/blog/reading-an-explain-analyze-query-plan)
* [Calcolo costi PostgreSQL](http://shiroyasha.io/the-postgresql-query-cost-model.html)



