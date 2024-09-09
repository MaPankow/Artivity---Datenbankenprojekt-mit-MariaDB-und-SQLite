# SQL- Datenbank für ein Social Network

Artivity ist ein fiktives soziales Netzwerk, das ich nutze, um verschiedene Datenbankmodelle damit zu modellieren und zu implementieren.
In diesem Projekt werde ich eine Datenbank in der relationalen Datenbank MariaDB anlegen. Dieses Projekt dient außerdem dazu, SQL zu nutzen. Ich lege die Datenbank mithilfe von LAMPP und PHP MaAdmin an, benutze jedoch SQL-Code anstatt der zue Verfügung gestellten Funktionen.

## 1. Beschreibung des Social Networks "Artivity" (Arbeitstitel)

Bei dem Social Network handelt es sich um eine (fiktive) soziale Plattform für KünstlerInnen und Kunstinteressierte, in der das Zeigen von Bildern, Musik etc. und Organisieren in Gruppen im Vordergrund stehen soll.

Hierbei können die User Profile erstellen und sich mit Profilbild, Text, Links zu anderen Profilen und Portfolios sowie das Taggen von Kunstformen, an denen sie interessiert sind oder die sie ausüben, vorstellen. Es sind alle Arten von KünstlerInnen eingeladen, ob sie malen, bildhauen, Gebäude planen, 3D-Grafiken erstellen, musizieren, schreiben etc. Auch Kunstinteressierte sowie Menschen, die KünstlerInnen eine Bühne oder Ausstellungsmöglichkeit im Real Life anbieten können, sind eingeladen.
Vom Aufbau her ist es eher an Facebook angelehnt.


## Datenmodellierung

### Use-Case
Als erstes erstelle ich ein Use-Case-Diagramm, das abbildet, welche Akteure auf der Plattform aktiv sind und was diese tun können sollen. Hierfür schon vorab ein paar Gedanken:


Mit den **Users** als *Actor* fägt es natürlich an. Die Use-Cases finden im System des Netzwerks **Artivity** statt.

Beim Erstellen haben sich für mich gewisse *Cluster* ergeben, um die sich verschiedene Use-Cases thematisch gebildet haben (auch wenn einige Use-Cases culster-übergreifend sind):

1. Anmelden und Userprofil erstellen
Das Userprofil auszufüllen ist erforderlich, ein Profilbild kann hochgeladen werden.

2. Posting und alles drum und dran
Dazu gehören Posts und Kommentare, die die Users verfassen können, sowie dazugehörige Use-Cases wie andere Users taggen, Hashtags erstellen, Bilder hochladen oder Referenzlinks angeben. Auch können Posts und Kommentare gelikt werden.

3. Gruppenaktivitäten
Hier tritt eine weitere Person als Actor ins Geschehen ein, denn wer eine Gruppe gründet, ist automatisch Admin/a dieser Gruppe. Bei der Erstellung der Gruppe kann ein Gruppenbild hochgeladen werden. Users können sich in der Gruppe organisieren und Posts und Kommentare schreiben.

4. Clusterübergreifende Use-Cases
Beispielsweise können Users für ihre Profile, für Gruppen oder Postings Schlagwörter aussuchen, die hier eine (grob gehaltene) Kunstrichtung bezeichnen und vom Netzwerk vorgegeben sind. Für feinere Spezifizierung ihrer Beiträge und in diesem Fall auch Kommentare können die User Hashtags nutzen. Z. B. für das Schlagwort "Literatur" könnten #schreiben #kriminalroman #coverdesign gewählt werden, die sich in diesem Fall um ein Buchgenre und Fragen ums Buch drehen (man kann mehrere Schlagwörter nehmen, so könnte #coverdesign für das Literaturgenre oder das Designgenre passend sein)

## Entity-Relationship-Diagramm 
### ERD konzeptuell

Hier habe ich mit den Entitäten begonnen, die sich aus den Use-Cases ergaben. Dann beschrieb ich grob, welche Eigenschaften die Entitäten haben sollen. (Ids sind z. B. noch nicht dabei. 

Die Entitäten setze ich ich in Relation zueinander, indem ich beschreibe, was in der Beziehung gemacht wird die kleinen Zahlen an den Pfeilen geben an, ob es jeweils eine One-to-One-, eine One-to-Many- oder eine Many-to-Many-Beziehung ist. 

In einem nächsten Schritt löse ich die Many-to-Many-Beziehungen auf, da sie in der Datenbank gegen die Datenintegrität verstoßen. Sie können schlichtweg nicht so in Tabellen angelegt werden, dass kein Durcheinander entsteht.

Dieses Auflösen der Many-to-Many-Beziehung erfolgt über das Anlegen von Hilfsentitäten, die eine Entität "dazwischenschieben", die auf die Ids beider Entitäten zugreift und zu beiden Entitäten jeweils eine Many-to-One-Beziehung hat.

### ERD physisch

Es erfolgt die Überführung in ein logisches Datenmodell. Die Entitäten und ihre Eigenschaften werden nun in Tabellen angelegt. Hierbei werden die Beziehungen nicht nur mit Verbindungspfeilen dargestellt, sondern auch als Spalten in den Tabellen angelegt. Dabei werden auch die Primary Keys und Foreign Keys festgelegt. Die Beziehungen werden nicht mehr mit Worten beschrieben sondern mit spezifischen Pfeilen dargestellt, die auf die jeweils passenden Egenschaften verweisen.

Diese Enittäten werden Tabellen in  der Datenbank sein und ihre Eigenschaften werden die Spalten.

Sind die Tabellen und Spalten richtig modelliert, trage die Datentypen und weitere Befehle eingtragen, die in der SQL-Datenbank beim Erstellen der Tabellen zu beachten sind.

Das physische ERD bildet die Datenbank ab. Nun wird sie erstellt.


## SQL-Code für MariaDB

```sql
CREATE DATABASE artivity_netzwerk;
```

### Users

```sql
USE artivity_netzwerk;

CREATE TABLE users (
    id_user INT AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    pwd VARCHAR(255) NOT NULL,
    profilbild BLOB,
    profilinfo TEXT,
    homepage VARCHAR(100),
    impressum VARCHAR(100),
    PRIMARY KEY (id_user)
    );
```

User-Datensatz eingeben:

```sql
INSERT INTO users(username, email, pwd, profilbild, profilinfo, homepage, impressum) VALUES
('user1', 'email1@emailhausen.de', 'geilesPasswort', 0xFF00FF00, NULL, 'home.de', 'home.de/impressum');
```


### Schlagwörter

```sql
CREATE TABLE schlagwoerter (
    id_schlagwort INT AUTO_INCREMENT,
    schlagwort VARCHAR(50),
    PRIMARY KEY (id_schlagwort)
    );
```
```sql
INSERT INTO schlagwoerter(schlagwort) VALUES
('Architektur'),
('Illustration'),
('Schreiben'),
('Journalismus'),
('Grafikdesign'),
('Malerei'),
('Musik'),
('3D-Grafik'),
('Bildende Kunst'),
('Schmuckdesign'),
('Modedesign'),
('Film/Video'),
('Content Creation'),
('Installation'),
('Fotografie'),
('Gallerie'),
('Veranstaltungsort'),
('Veranstaltungsorganisation');
``` 

### Gruppen

 ```sql
CREATE TABLE gruppen (
    id_gruppe INT AUTO_INCREMENT,
    fid_gruppenadmin INT,
    gruppenname VARCHAR(100),
    beschreibung TEXT,
    gruppenbild BLOB,
    PRIMARY KEY (id_gruppe),
    FOREIGN KEY (fid_gruppenadmin) REFERENCES users(id_user)
    );
```

```sql
INSERT INTO gruppen(fid_gruppenadmin, gruppenname, beschreibung, gruppenbild) VALUES
(1, 'DUH!', 'I´m a baaaaaaaad guy!', NULL);

INSERT INTO gruppen(fid_gruppenadmin, gruppenname, beschreibung, gruppenbild) VALUES
(2, 'Plattenbaunerds', 'Für Leute die viele von einem in strukturierter Anordnung lieben', NULL),
(4, 'Wir zeichnen Berlin', 'Wir treffen uns jede Woche an einem anderen Ort in der Stadt zum Zeichnen', NULL);
```

### Hilfstabelle Gruppenmitglieder
Ein/e User kann in mehreren Gruppen sein und eine Gruppe kann mehrere Users haben. Um diese Many-to-many-Beziehung aufzulösen, setze ich eine Hilfstabelle ein, die ich User_Gruppe nennen könnte, aber Gruppenmitglieder bietet sich hier eher an.


```sql
CREATE TABLE gruppenmitglieder (
    fid_user INT,
    fid_gruppe INT,
    PRIMARY KEY (fid_user, fid_gruppe),
    FOREIGN KEY (fid_user) REFERENCES users(id_user),
    FOREIGN KEY (fid_gruppe) REFERENCES gruppen(id_gruppe)
    );
```

```sql
INSERT INTO gruppenmitglieder (fid_user, fid_gruppe) VALUES 
(2, 2),
(3, 2),
(4, 3),
(4, 1),
(5, 2)
(1, 1);
```
Die/der Gruppenadmin/a muss natürlich auch ein Gruppenmitglied sein. Daher der Gedanke, dass bei einer Gruppengründung zwei Querys erfolgen, erst die Gruppengründung und dann die Eintragung in die Gruppenmitglieder-Tabelle. Diese Querys kann ich gemeinsam abfeuern, aber auch nur, weil ich mich gerade noch so erinnern kann, wie viele Gruppen ich habe und welche ID jetzt dran wäre.
Mit einem Backend würden die Querys hintereinander laufen, damit erst die Gruppen-Id der neuen Gruppe vergeben wird. Diese Befehle laufen nacheinander durch, sobald die Person, die die Gruppe erstellt, fertig ist und die Erstellung bestätigt.

### Posts



```sql
USE artivity_netzwerk;

CREATE TABLE posts (
    id_post INT AUTO_INCREMENT,
    titel VARCHAR(100),
    fid_user INT,
    fid_username VARCHAR(50),
    fid_gruppe INT,
    body TEXT,
    datei BLOB,
    likes INT,
    erstellt_am TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_post),
    FOREIGN KEY (fid_user) REFERENCES users(id_user),
    FOREIGN KEY (fid_username) REFERENCES users(username),
    FOREIGN KEY (fid_gruppe) REFERENCES gruppen(id_gruppe)
    );
```

Einträge:
```sql
INSERT INTO posts(titel, fid_user, fid_gruppe, body, datei) VALUES
('What was I made for1', 1, 1, 'I used to float, now I just fall down ...', NULL), 
('What was I made for2', 1, 1, 'I used to know, but I`m not sure now ...', NULL), 
('What was I made for3', 1, 1, '...what I was made for. What was I made for?', NULL);
```

### Kommentare

```sql
CREATE TABLE kommentare (
    id_kommentar INT AUTO_INCREMENT,
    fid_post INT,
    fid_user INT,
    fid_username VARCHAR(50),
    body TEXT,
    datei BLOB,
    likes INT,
    erstellt_am TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_kommentar),
    FOREIGN KEY (fid_post)REFERENCES posts(id_post),
    FOREIGN KEY (fid_user) REFERENCES users(id_user),
    FOREIGN KEY (fid_username) REFERENCES users(username)
    );
```


### Schlagwort_Bezeichnung
Jetzt, wo die Tabellen Gruppen, Posts, Users und Schlagwörter vorhanden sind, kann ich sie über FOREIGN KEYS verlinken. Ich kann die Hilfstabelle erstellen, mittels der die users ihren Profilen, ihren Post und Gruppn beim Erstellen Schlagwörter zuweisen können.

So sieht der Wahnsinn dann aus:
```sql
CREATE TABLE schlagwort_bezeichnung (
    id_schlagwort_bezeichnung INT AUTO_INCREMENT,
    fid_schlagwort INT,
    fid_user INT,
    fid_post INT,
    fid_gruppe INT,
    PRIMARY KEY (id_schlagwort_bezeichnung),
    FOREIGN KEY(fid_schlagwort) REFERENCES schlagwoerter(id_schlagwort),
    FOREIGN KEY(fid_user) REFERENCES users(id_user),
    FOREIGN KEY(fid_post) REFERENCES posts(id_post),
    FOREIGN KEY(fid_gruppe) REFERENCES gruppen(id_gruppe)
    );
```
``` sql
INSERT INTO schlagwort_bezeichnung (fid_schlagwort, fid_post) VALUES
(13, 1),
(7, 1),
(7, 2),
(1, 3);
```

Beispiel einer SQL-Query, in der von Post-Id 2 Titel, Body und Schlagwort angezeigt werden sollen:

```sql
SELECT p.titel, p.body, s.schlagwort FROM schlagwort_bezeichnung sb
INNER JOIN posts p ON sb.fid_post = p.id_post
INNER JOIN schlagwoerter s ON s.id_schlagwort = sb.fid_schlagwort
WHERE p.id_post = 2;
```



### Hashtags
Hashtags verfolgen eigentlich das gleiche Prinzip wie Schlagwörter, nur, dass sie durch die Users angelegt werden und somit für eine spezifischere Themenwahl (z. B. Buchgenre oder eine Subkultur oder generelle Themen wie Blockaden) zur Verfügung stehen.
In der Datenbank verfolge ich dasselbe Prinzip wie bei den Schlagwörtern. Es gibt die Tabelle, in der die Hashtags gesammelt werden, und eine Hilfstabelle, in der sie Posts und Kommentaren (keinen Gruppen) zugeordnet werden.

```sql
CREATE TABLE hashtags (
    id_hashtag INT AUTO_INCREMENT,
    hashtag VARCHAR(100),
    PRIMARY KEY (id_hashtag)
    );
```
### Hashtag_Bezeichnung

```sql
CREATE TABLE hashtag_bezeichnung (
    id_hashtag_bezeichnung INT AUTO_INCREMENT,
    fid_hashtag INT,
    fid_kommentar INT,
    fid_post INT,
    PRIMARY KEY (id_hashtag_bezeichnung),
    FOREIGN KEY (fid_hashtag) REFERENCES hashtags(id_hashtag),
    FOREIGN KEY (fid_kommentar) REFERENCES kommentare(id_kommentar),
    FOREIGN KEY (fid_post) REFERENCES posts(id_post)
    );
```

### Likes_Posts

```sql
CREATE TABLE likes_posts (
    fid_user INT,
    fid_post INT,
    PRIMARY KEY (fid_user, fid_post),
    FOREIGN KEY (fid_user) REFERENCES users(id_user),
    FOREIGN KEY (fid_post) REFERENCES posts(id_post)
    );
```

Ein paar Likes für Posts eintragen:
```sql
INSERT INTO likes_posts (fid_user, fid_post) VALUES (1, 2), (2, 2), (1, 3), (4, 2), (3, 3);
```

Die Anzahl von Likes für Posts abfragen:

```sql
SELECT COUNT(fid_post) FROM `likes_posts` WHERE fid_post = 2; 
```
Ergibt 3.

### Likes_Kommentare

```sql
CREATE TABLE likes_kommentare (
    fid_user INT,
    fid_kommentar INT,
    PRIMARY KEY (fid_user, fid_kommentar),
    FOREIGN KEY (fid_user) REFERENCES users(id_user),
    FOREIGN KEY (fid_kommentar) REFERENCES kommentare(id_kommentar)
    );
```
### Referenzlinks und Tags auf andere Users
Finden über Verlinkungen auf Anwendungsebene statt. Ich halte es nicht für nötig, sie extra in der Datenbank abzuspeichern, da es erstens viele weitere Tabellen erfordern würde und zweitens fallen mir keine Use-Cases ein, die hierfür eine extra Datenbank erfordern, zumindest nicht in diesem Rahmen. (Es könnte vielleicht marketingtechnisch relevat sein, doch das Thema würde jetzt zu sehr abweichen)
Ein Use-Case, der Links relevant machen könnte, wäre vielleicht Admin-Tätigkeit, in der man auf einen externen Link in einem Post/Kommentar aufmerksam wird, der zu Falschinformationen oder Hassinhalten führt, und nun möchte ich als Admina wissen, ob dieser Link öfter gepostet worden ist. Dies ließe sich aber auch mit einem LIKE-Operator abfragen.

Z. B. gibt die folgende Abfrage den Post aus, den ich testweise angefertigt habe und in dem das Wort "float" vorkommt:
```sql 
SELECT * FROM posts WHERE body LIKE ('%float%');
```
Die Abfrage habe ich auf die Schnelle auf das Wort gemacht, weil ich keinen Test-Post mit einem Link eigegeben habe. Aber man kann alles zwischen die '% ... %' eingeben, auch einen Link, den man wiederfinden möchte.