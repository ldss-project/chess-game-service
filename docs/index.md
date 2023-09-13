---
title: Homepage
layout: default
nav_order: 1
---

# Chess Game Service
{: .no_toc}

## Contenuti
{: .no_toc}

- TOC
{:toc}

---

## Descrizione

Il **Chess Game Service** è un servizio che gestisce la configurazione, l'esecuzione
e la terminazione di partite di scacchi, oltre che la connessione dei giocatori a tali
partite.

## Implementazione

L'implementazione del **Chess Game Service** è descritta dal seguente diagramma delle classi
UML.

![Chess Game Service Class Diagram](/chess-game-service/resources/images/chess-game-service.png)

Come si può vedere dal diagramma, l'implementazione del servizio dipende dal framework
[HexArc](https://github.com/ldss-project/hexarc) e da un [Legacy Chess Engine](https://github.com/jahrim/PPS-22-chess)
(i cui componenti nel diagramma sono identificabili attraverso il prefisso `Legacy-`).

In particolare, il servizio definisce due componenti principali:
- `ChessGamePort`: definisce le funzionalità del servizio. Tali funzionalità si distinguono in due categorie:
    - **Chess Game Management**: gestiscono la creazione e ricerca delle partite di scacchi nel servizio, oltre
      che della connessione dei giocatori a tali partite.
    - **Chess Game Execution**: gestiscono lo svolgimento di una partita di scacchi.
- `ChessGameHttpAdapter`: espone alcune delle funzionalità della `ChessGamePort`. In particolare, espone le
  funzionalità relative al **Chess Game Management** attraverso un contratto di tipo _REST_, disponibile al
  seguente [link](/swagger-apis/chess-game-service/latest/rest), mentre espone le funzionalità relative alla
  **Chess Game Execution** attraverso un contratto di tipo _WebSocket_, disponibile al seguente
  [link](/swagger-apis/chess-game-service/latest/async).

### Chess Game Execution

In questa sezione, si descriverà come vengono esposte le funzionalità relative all'esecuzione di una partita di
scacchi all'interno di questo servizio.

#### Modellazione del dominio

L'esecuzione di una partita di scacchi è gestita da un `ChessGameServer`, ovvero un server di gioco all'interno
del servizio.

Il `ChessGameServer` è descritto da uno stato, modellato dalla classe `ServerState`. Il `ServerState` di un
`ChessGameServer` contiene le seguenti informazioni sul server:
- `serverSituation`: la situazione attuale in cui si trova il `ChessGameServer`, modellata dall'enumerazione
  `ServerSituation`. Una `ServerSituation` può essere uno dei seguenti stati:
    - `NotConfigured`: indica che il `ChessGameServer` è in attesa di essere configurato prima di poter cominciare
      la partita.
    - `WaitingForPlayers`: indica che il `ChessGameServer` è in attesa della partecipazione alla partita di un numero
      sufficiente di giocatori.
    - `Ready`: indica che il `ChessGameServer` è pronto a cominciare la partita ed è in attesa che qualcuno la
      cominci.
    - `Running`: indica che il `ChessGameServer` sta eseguendo la partita ed è reattivo ai comandi dei giocatori.
    - `Terminated`: indica che la partita gestita dal `ChessGameServer` è terminata, per suo naturale decorso oppure
      a causa di un errore nel server.
- `serverError`: l'errore più recente generato nel `ChessGameServer`, se presente. I possibili errori nel `ChessGameServer`
  sono modellati dalla classe `ChessGameException` e possono essere:
    - `GameConfiguredException`: generato se si prova a configurare il `ChessGameServer` quando è già stato configurato.
    - `GameNotReadyException`: generato se si prova a cominciare la partita del `ChessGameServer` quando non è pronto a
      cominciarla.
    - `GameNotRunningException`: generato se si prova a inviare un comando al `ChessGameServer` quando non sta eseguendo
      la partita.
    - `GameNotWaitingForPlayersException`: generato se si prova a partecipare alla partita del `ChessGameServer` quando
      non è in attesa di giocatori.
    - `GameNotWaitingForPromotionException`: generato se si prova a promuovere un pedone nella partita del
      `ChessGameServer` quando non è in attesa della promozione di un pedone.
    - `GameTerminatedException`: generato se si prova ad eseguire un azione sul `ChessGameServer` quando è terminato e
      l'azione richiede che non lo sia.
    - `GameWaitingForPromotionException`: generato se si prova ad eseguire un azione sul `ChessGameServer` diversa dalla
      promozione quando è in attesa della promozione di un pedone.
    - `InternalServerError`: generato a causa di un errore imprevisto nel `ChessGameServer`. Indica un probabile errore
      d'implementazione all'interno del servizio.
    - `PlayerAlreadyExistingException`: generato se si prova a partecipare al `ChessGameServer` come un giocatore di una
      specifica squadra quando un giocatore di quella squadra è già presente.
- `subscriptions`: l'insieme delle sottoscrizioni agli eventi generati dal `ChessGameServer`. Tale insieme è modellato
  come una mappa da degli identificatori alle corrispondenti sottoscrizioni, modellate dalla classe `MessageConsumer`
  di [Vertx](https://vertx.io/).
- `gameState`: lo stato della partita ospitata dal `ChessGameServer`, modellato dalla classe `GameState`.

All'interno del `ServerState` di un `ChessGameServer`, il `GameState` racchiude le seguenti informazioni:
- `legacy`: una conversione del `GameState` in un formato comprensibile per il
  [Legacy Chess Engine](https://github.com/jahrim/PPS-22-chess).
- `chessboard`: lo stato della scacchiera nella partita, modellato dalla classe legacy `Chessboard`. Una
  `Chessboard` è un insieme di pezzi, modellati dalla classe legacy `Piece`, assegnati a una certa posizione
  sulla scacchiera, modellata dalla classe legacy `Position`.

  Un pezzo sulla scacchiera può essere un re, una regina, una torre, un alfiere, un cavallo o un pedone,
  modellati rispettivamente dalle classi legacy `King`, `Queen`, `Rook`, `Bishop`, `Knight` e `Pawn`.
  A ogni pezzo sono associate delle regole di movimento gestite dal
  [Legacy Chess Engine](https://github.com/jahrim/PPS-22-chess).
- `currentTurn`: il turno corrente nella partita. Il turno indica il colore del giocatore a cui è
  permesso di effettuare una mossa. Tale colore è modellato dall'enumerazione legacy `Team`, che consiste
  dei valori `WHITE` e `BLACK`.
- `moveHistory`: lo storico delle mosse effettuate durante la partita, modellato dalla classe `MoveHistory`.
  La `MoveHistory` permette di registrare e di ottenere le mosse compiute dai diversi pezzi sulla scacchiera,
  modellate dalla classe legacy `Move`.

  Una mossa può essere una cattura, la doppia mossa del pedone, l'arrocco o la presa al varco, modellate
  rispettivamente dalle classi legacy `CaptureMove`, `DoubleMove`, `CastlingMove` e `EnPassantMove`.
- `gameSituation`: la situazione in cui si trova la partita, modellata dalla classe `GameSituation`. Essa
  può essere:
    - `None`: indica che non è presente nessuna situazione particolare nella partita;
    - `Check`: indica che il re del giocatore di turno potrebbe essere catturato dall'avversario nel turno successivo;
    - `Stale`: indica che il giocatore di turno non ha mosse disponibili;
    - `Checkmate`: indica che si è nelle situazioni di `Check` e di `Stale`;
    - `Promotion`: indica che un pedone del giocatore di turno ha raggiunto l'estremità opposta della scacchiera, quindi
      è in attesa di essere promosso a un altro pezzo.
- `gameOver`: indica il risultato della partita, se è terminata. Tale risultato è modellato dalla classe `GameOver`,
  che include il giocatore che ha vinto la partita (se presente), modellato dalla classe legacy `Player`, e la causa
  della terminazione della partita, modellata dalla classe `GameOverCause`.

  Le cause della terminazione di una partita includono lo scacco matto, lo stallo, lo scadere del tempo di uno dei
  due giocatori e la resa, modellati rispettivamente dai valori `Checkmate`, `Stalemate`, `Timeout` e `Surrender`.
- `timers`: indica il tempo rimasto a ciascun giocatore per effettuare le proprie mosse.
- `gameConfiguration`: la configurazione della partita, modellata dalla classe `GameConfiguration`.

All'interno del `GameState` di un `ChessGameServer`, la `GameConfiguration` racchiude le seguenti informazioni:
- `legacy`: una conversione della `GameConfiguration` in un formato comprensibile per il
  [Legacy Chess Engine](https://github.com/jahrim/PPS-22-chess).
- `gameMode`: la modalità di gioco della partita, modellata dall'enumerazione legacy `GameMode`. Una `GameMode`
  può assumere i seguenti valori:
    - `PVP`: indica che entrambi i giocatori nella partita sono umani. Nel caso di questo servizio, ciò è sempre
      vero.
    - `PVE`: indica che un giocatore della partita è umano mentre l'altro è un computer.
- `timeConstraint`: indica i vincoli di tempo imposti ai giocatori per eseguire le loro mosse, modellati
  dall'enumerazione `TimeConstraint`. Un `TimeConstraint` può assumere i seguenti valori:
    - `NoLimit`: nessun vincolo di tempo è applicato alle mosse dei giocatori.
    - `MoveLimit`: ogni giocatore ha un vincolo di tempo per effettuare le proprie mosse e tale vincolo si
      resetta ad ogni mossa;
    - `PlayerLimit`: ogni giocatore ha un vincolo di tempo per effettuare le proprie mosse e tale vincolo non
      si resetta ad ogni mossa.
- `blackPlayer`: indica il giocatore appartenente alla squadra nera, modellato dalla classe legacy
  `BlackPlayer`.
- `whitePlayer`: indica il giocatore appartenente alla squadra bianca, modellato dalla classe legacy
  `WhitePlayer`.
- `isPrivate`: indica se la partita è pubblica, ovvero accessibile a chiunque, o privata, ovvero accessibile
  solo ai giocatori che ne conoscono l'identificatore.
- `gameId`: indica l'identificatore della partita. Tale identificatore può essere associato alla partita anche
  dopo la sua creazione.

#### Esecuzione di una partita

Il `ChessGameServer` espone le seguenti funzionalità per controllare l'esecuzione di una partita di scacchi:
- `configure`: applica una configurazione specifica alla partita ospitata dal `ChessGameServer`. Al termine
  dell'operazione, in base al contenuto della configurazione specificata, il `ChessGameServer` può entrare nello
  stato `Ready`, se sono già stati specificati entrambi i giocatori della partita, oppure `WaitingForPlayers`,
  altrimenti.
- `join`: fa partecipare un giocatore specificato alla partita ospitata dal `ChessGameServer`. Al termine
  dell'operazione, se sono già stati specificati entrambi i giocatori della partita, il `ChessGameServer` entra nello
  stato `Ready`.
- `start`: fa cominciare la partita ospitata dal `ChessGameServer`, eventualmente inizializzando i timer relativi ai
  giocatori e specificando una callback da eseguire ad ogni tick dei timer e allo scadere di un timer.

  Al termine dell'operazione, il `ChessGameServer` entra nello stato `Running`. Mentre, allo scadere di uno dei timer,
  se presenti, il `ChessGameServer` entra nello stato `Terminated`.
- `stop`: fa terminare la partita ospitata dal `ChessGameServer`, fermando i timer relativi ai giocatori e rimuovendo
  tutte le sottoscrizioni agli eventi del `ChessGameServer`. Al termine dell'operazione, il `ChessGameServer` entra
  nello stato `Terminated`.
- `findMoves`: restituisce tutte le possibili mosse di un pezzo su una specifica posizione nella scacchiera.
- `applyMove`: applica una mossa specifica ad un pezzo sulla scacchiera di uno dei due giocatori, aggiornando la
  scacchiera e lo storico delle mosse, quindi verificando la situazione della partita dopo la sua applicazione e
  passando al turno del giocatore avversario.

  Al termine dell'operazione, se la situazione nella partita è `Stale` o `Checkmate`, il `ChessGameServer` entra nello
  stato `Terminated`, altrimenti rimane nello stato `Running`.
- `promote`: promuove il pedone del giocatore di turno al pezzo specificato se in attesa di essere promosso,
  verificando la situazione della partita dopo la sua promozione e passando al turno del giocatore avversario. I pezzi
  ai quali è possibile promuovere un pedone sono modellati dalla classe `PromotionChoice` e sono la regina, la torre,
  l'alfiere e il cavallo (rispettivamente i valori `Queen`, `Rook`, `Bishop` e `Knight`).

  Al termine dell'operazione, se la situazione nella partita è `Stale` o `Checkmate`, il `ChessGameServer` entra nello
  stato `Terminated`, altrimenti ritorna nello stato `Running`.
- `subscribe`: registra una callback da eseguire ogni volta che viene generato uno specifico tipo di evento dal
  `ChessGameServer`. Gli eventi generati dal `ChessGameServer` sono modellati dalla classe `Event`, se non contengono
  dei dati in aggiunta al tipo di evento, oppure dalla classe `Event.Payload`, se contengono tali dati.

  In particolare, gli eventi generati da un `ChessGameServer` sono organizzati in una gerarchia, la cui radice è
  l'evento `ChessGameServiceEvent`. L'organizzazione gerarchica permette ai giocatori di sottoscriversi a un
  sotto-albero della gerarchia, senza dover specificare tutti gli eventi che esso include. Di seguito, viene mostrata
  tale gerarchia:
    - `ChessGameServiceEvent`: un evento generato all'interno di questo servizio. Esso può essere:
        - `LoggingEvent`: un evento generato quando un `ChessGameServer` aggiunge un nuovo messaggio al suo registro dei
          messaggi;
        - `ServerStateUpdateEvent`: un evento generato quando lo stato di un `ChessGameServer` viene modificato.
          Esso può essere:
            - `ServerSituationUpdateEvent`: un evento generato quando la situazione di un `ChessGameServer`
              viene modificata.
            - `SubscriptionUpdateEvent`: un evento generato quando le sottoscrizioni a un `ChessGameServer`
              vengono modificate.
            - `ServerErrorUpdateEvent`: un evento generato quando l'ultimo errore occorso all'interno di un `ChessGameServer`
              viene modificato.
            - `GameStateUpdateEvent`: un evento generato quando lo stato della partita ospitata da un `ChessGameServer` viene
              modificato. Esso può essere:
                - `ChessboardUpdateEvent`: un evento generato quando la scacchiera nella partita ospitata da un `ChessGameServer`
                  viene modificata.
                - `GameOverUpdateEvent`: un evento generato quando il risultato della partita ospitata da un `ChessGameServer`
                  viene modificato, ovvero quando la partita termina.
                - `GameSituationUpdateEvent`: un evento generato quando la situazione nella partita ospitata da un
                  `ChessGameServer` viene modificata.
                - `MoveHistoryUpdateEvent`: un evento generato quando lo storico delle mosse nella partita ospitata da
                  un `ChessGameServer` viene modificato.
                - `TurnUpdateEvent`: un evento generato quando il turno corrente nella partita ospitata da un
                  `ChessGameServer` viene modificato.
                - `PlayerUpdateEvent`: un evento generato quando un giocatore nella partita ospitata da un
                  `ChessGameServer` viene modificato. Esso può essere:
                    - `BlackPlayerUpdateEvent`: un evento generato quando il giocatore della squadra nera nella partita
                      ospitata da un `ChessGameServer` viene modificato.
                    - `WhitePlayerUpdateEvent`: un evento generato quando il giocatore della squadra bianca nella partita
                      ospitata da un `ChessGameServer` viene modificato.
                - `TimerUpdateEvent`: un evento generato quando il tempo rimasto a uno dei due giocatori nella partita ospitata
                  da un `ChessGameServer` viene modificato.
                    - `BlackTimerUpdateEvent`: un evento generato quando il tempo rimasto al giocatore della squadra nera nella
                      partita ospitata da un `ChessGameServer` viene modificato.
                    - `WhiteTimerUpdateEvent`: un evento generato quando il tempo rimasto al giocatore della squadra bianca nella
                      partita ospitata da un `ChessGameServer` viene modificato.
- `unsubscribe`: rimuove alcune sottoscrizioni tra quelle registrate sul `ChessGameServer`, dato il loro identificatore.

Il ciclo di vita di un `ChessGameServer` può quindi essere riassunto nel seguente diagramma a stati.

![Chess Game Server State Diagram](/chess-game-service/resources/images/chess-game-server-state-diagram.png)

Entrando nei dettagli dell'implementazione del `ChessGameServer`, modellata dalla classe `BasicChessGameServer`,
si è deciso di estrarre alcune delle sue funzionalità delegandole a delle classi separate, in modo da evitare la
definizione di una _god class_. In particolare, il `BasicChessGameServer` fa affidamento a due mixin:
- `BasicChessGameServerExecutionManager`: fornisce dei metodi di supporto per modificare l'esecuzione di alcune
  porzioni di codice all'interno del `BasicChessGameServer`. Il `BasicChessGameServerExecutionManager` viene
  principalmente utilizzato per garantire che le funzionalità del `BasicChessGameServer` siano eseguite nello
  stato adeguato, come descritto dal diagramma degli stati. In caso contrario, il `BasicChessGameServerExecutionManager`
  genera un'opportuna `ChessGameServiceException`.
- `BasicChessGameServerEventManager`: gestisce la sottoscrizione agli eventi del `BasicChessGameServer` e la loro
  pubblicazione. Inoltre, fornisce un insieme di `Receiver` corrispondenti a diverse porzioni dello stato del
  `BasicChessGameServer`.

  Un `Receiver` è una funzione che osserva dei valori. In particolare, il `BasicChessGameServerEventManager` utilizza
  dei `Receiver` per associare la modifica di una certa porzione di stato alla pubblicazione dell'evento corrispondente.
  In questo modo, finché lo stato viene modificato attraverso tali `Receiver`, si è sicuri che i giocatori sottoscritti
  al `BasicChessGameServer` siano notificati di tali cambiamenti di stato.

#### Connessione di un giocatore

L'accesso ai `ChessGameServer` è limitato e mediato dalla `ChessGamePort`, che funge da _reverse proxy_ per
i `ChessGameServer`, gestendo le interazioni tra i giocatori e le partite a cui stanno partecipando.

In particolare, la `ChessGamePort` espone le stesse funzionalità di un `ChessGameServer`, generalizzandole
con la possibilità di applicarle a un qualunque `ChessGameServer` tra quelli registrati nel servizio.

Durante la connessione di un giocatore verso uno specifico `ChessGameServer`, la `ChessGamePort` esporrà le
funzionalità di quel `ChessGameServer` al giocatore, permettendo l'interazione con esso.

### Chess Game Management

La `ChessGamePort` espone le seguenti funzionalità relative al **Chess Game Management**:
- `getGames`: restituisce la `ChessGameMap` del servizio, ovvero una mappa dagli identificatori delle partite
  ai server di gioco che le ospitano, chiamati `ChessGameServer`;
- `createGame`: crea un `ChessGameServer` a partire dalla configurazione della partita, chiamata `GameConfiguration`;
- `deleteGame`: termina ed elimina un `ChessGameServer` dato il suo identificatore;
- `findPublicGame`: ricerca il primo `ChessGameServer` pubblico ancora in attesa di giocatori e restituisce
  il suo identificatore;
- `findPrivateGame`: ricerca il `ChessGameServer` privato con un identificatore specificato e, se esiste ed è
  ancora in attesa di giocatori, restituisce il suo identificatore.

# TODO

Tali funzionalità sono definite nei termini dei concetti del dominio del servizio.
In particolare, i modelli relativi a tali concetti sono i seguenti:
- `Score`: modella un punteggio nel servizio;
- `UserScore`: modella il punteggio di un utente nel servizio;
- `UserScoreHistory`: modella le statistiche di un utente nel servizio.

L'implementazione della `StatisticsPort` è modellata dallo `StatisticsModel`.
Lo `StatisticsModel` gestisce la persistenza dei dati nel servizio attraverso una
`PersistentCollection` e per comunicare con la `PersistentCollection` utilizza il
linguaggio delle query `MongoDBQueryLanguage`. Quindi, implementa tutte le funzionalità
della `StatisticsPort` attraverso delle opportune query.

Lo `StatisticsHttpAdapter` e lo `StatisticsModel` possono generare delle eccezioni,
modellate dalla classe `StatisticsServiceException`. In particolare, l'utente che
utilizza il servizio potrebbe essere notificato delle seguenti
`StatisticsServiceException`s:
- `MalformedInputException`: indica all'utente che l'input specificato per una certa
  funzionalità da lui richiesta non è corretto;
- `UserNotFoundException`: indica all'utente che il nome utente da lui specificato non è
  associato a nessun dato nel sistema.

## Verifica

Per verificare il sistema, è stata creata una suite di test manuali su
[Postman](https://www.postman.com/), in modo da accertarsi che tutte le funzionalità
esposte dal contratto _REST_ del servizio producessero i risultati attesi.

In futuro, si dovrà creare degli _unit test_ equivalenti, ma automatici. Per fare ciò,
sarà necessario approfondire come creare un database [MongoDB](https://www.mongodb.com)
di tipo _in-memory_ in [Scala](https://scala-lang.org/).

## Esecuzione

Per eseguire il sistema è disponibile un jar al seguente
[link](https://github.com/ldss-project/statistics-service/releases).

Per eseguire il jar è sufficiente utilizzare il seguente comando:
```shell
java -jar statistics-service-<version>.jar \
--mongodb-connection MONGODB_CONNECTION_STRING
```

In particolare, il jar permette di specificare i seguenti argomenti a linea di comando:
- `--mongodb-connection MONGODB_CONNECTION_STRING`: obbligatorio. Permette di specificare
  la stringa (`MONGODB_CONNECTION_STRING`) per connettersi all'istanza di
  [MongoDB](https://www.mongodb.com) che sarà utilizzata dal servizio per memorizzare i propri
  dati.
- `--mongodb-database DATABASE_NAME`: opzionale. Permette di indicare il nome del database (`DATABASE_NAME`)
  all'interno dell'istanza di [MongoDB](https://www.mongodb.com) specificata in cui il servizio memorizzerà i
  propri dati. Default: `statistics`.
- `--mongodb-collection COLLECTION_NAME`: opzionale. Permette di indicare il nome della collezione
  (`COLLECTION_NAME`) all'interno del database [MongoDB](https://www.mongodb.com) specificato in cui il
  servizio memorizzerà i propri dati. Default: `scores`.
- `--http-host HOST`: opzionale. Permette di indicare il nome dell'host (`HOST`) su cui sarà esposto il
  contratto _REST_ del servizio. Default: `localhost`.
- `--http-port PORT`: opzionale. Permette di indicare la porta dell'host (`PORT`) su cui sarà esposto il
  contratto _REST_ del servizio. Default: `8080`.
- `--allowed-origins ORIGIN_1;ORIGIN_2;...;`: opzionale. Permette di indicare una lista dei siti web che
  saranno autorizzati a comunicare con il servizio. Tale lista consiste in una sequenza di URL separati
  da `;`. Default: _nessun sito web autorizzato_.

In alternativa, un'immagine per eseguire il jar è stata pubblicata anche su [Docker](https://www.docker.com/).
Per eseguire il servizio tramite [Docker](https://www.docker.com/) è sufficiente utilizzare il seguente comando:
```shell
docker run -it jahrim/io.github.jahrim.chess.statistics-service:<version> \
--mongodb-connection MONGODB_CONNECTION_STRING
``````