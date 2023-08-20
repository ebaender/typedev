# typedev

![](images/showcase.gif)

Ein Typing-Test nach dem Prinzip von bekannten Seiten wie [Monkeytype](https://monkeytype.com) oder [Keybr](https://keybr.com) mit kompetitivem Multiplayer und persistentem Leaderboard, speziell für Entwickler mit Codesnippets statt zufällig gewählten Wörtern oder Zitaten und Unix-UX. War ursprünglich ein gemeinsames Universitätsprojekt von Malte Meilung und mir.

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
| statuscode | Bedeutung                            | 
| :--------: | :----------------------------------: | 
| 201        | Nutzer wurde erfolgreich registriert | 
| 406        | Nutzer ist bereits registriert       | 

### authentifizierung eines Nutzers
**Methode:** POST
**parameter:** username, password, authentificate

**example:** `curl -X POST -d username="foo" -d password="foo" -d request="authentificate"`

**return:**
| statuscode | Bedeutung                          |
| :--------: | :--------------------------------: |
| 202        | Nutzer erfolgreich authentifiziert, daten werden übermittelt |
| 401        | falsches password                  |
| 404        | Nutzer nicht gefunden              |

### abfrage der Daten eines Nutzers
**Methode:** POST
**parameter:** username, password, request, requested_user

**example:** `curl -X POST -d username="foo" -d password="foo" -d request="request" -d requested_user="foogirl"`

**return:**
| statuscode | Bedeutung             |
| :--------: | :-------------------: |
| 200        | OK                    |
| 401        | falsches password     |
| 404        | Nutzer nicht gefunden |
| 500        | requested_user nicht gefunden |

### update der Daten eines Nutzers
**Methode:** POST
**parameter:** username, password, request, games_played, games_won, wpm

**example:** `curl -X POST -d username="foo" -d password="foo" -d request="update" -d games_played=15 -d games_won=8 -d wpm=46`

**return:**
| statuscode | Bedeutung             |
| :--------: | :-------------------: |
| 201        | daten überschrieben   |
| 401        | falsches password     |
| 404        | Nutzer nicht gefunden |

### fehlerhafter request

| statuscode | Bedeutung            |
| :---------:| :------------------: |
| 400        | fehlerhafter request |

# Codesnippet Quellen:
GO: https://github.com/docker/cli/blob/master/cli/cobra.go  
LUA: https://pastebin.com/cUzGFJyw  
C: https://github.com/torvalds/linux/blob/master/arch/arm64/kernel/syscall.c  
Rust: https://github.com/starship/starship/blob/master/src/main.rs  
zsh: https://github.com/denysdovhan/spaceship-prompt/blob/master/spaceship.zsh  
JavaScript: einfach unseren eigenen JavaScript code lul ich will company leaks in unserem Spiel  
