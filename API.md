# Userdatenbank API

## Struktur

### request
parameter username, password und request sowie request spezifische parameter bei update

### return
JSON in form

`{statuscode: 400}`

oder

`{
	statuscode: 200,
	username: "foo",
	wpm: 0,
	games_won: 0,
	games_played: 0
}`

(bei der Abfrage der Nutzerdaten)

## Funktionen
### registrierung eines Nutzers
**Methode:** POST
**parameter:** username, password, register

**example:** `curl -X POST -d username="foo" -d password="foo" -d request="register"`

**return:**
| statuscode | Bedeutung |
| :---: | :---: |
| 201 | Nutzer wurde erfolgreich registriert |
| 406 | Nutzer ist bereits registriert |

### authentifizierung eines Nutzers
**Methode:** POST
**parameter:** username, password, authentificate

**example:** `curl -X POST -d username="foo" -d password="foo" -d request="authentificate"`

**return:**
| statuscode | Bedeutung |
| :---: | :---: |
| 202 | Nutzer erfolgreich authentifiziert |
| 401 | falsches password |
| 404 | Nutzer nicht gefunden |

### abfrage der Daten eines Nutzers
**Methode:** POST
**parameter:** username, password, request

**example:** `curl -X POST -d username="foo" -d password="foo" -d request="request"`

**return:**
| statuscode | Bedeutung |
| :---: | :---: |
| 200 | OK |
| 401 | falsches password |
| 404 | Nutzer nicht gefunden |

### update der Daten eines Nutzers
**Methode:** POST
**parameter:** username, password, request, games_played, games_won, wpm

**example:** `curl -X POST -d username="foo" -d password="foo" -d request="update" -d games_played=15 -d games_won=8 -d wpm=46`

**return:**
| statuscode | Bedeutung |
| :---: | :---: |
| 201 | daten überschrieben |
| 401 | falsched password |
| 404 | Nutzer nicht gefunden |

### fehlerhafter request

| statuscode | Bedeutung |
| :---:| :---: |
| 400 | fehlerhafter request |