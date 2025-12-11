System Turniejowy – Spring Boot

Projekt aplikacji backendowej do obsługi turniejów gier typu FPS.  

Aplikacja pozwala na:
- rejestrację i logowanie użytkowników,
- tworzenie drużyn oraz obsługę zgłoszeń,
- zapisywanie drużyn do turniejów,
- generowanie losowej drabinki turniejowej,
- rozgrywanie meczów i awans zwycięzców,
- ranking drużyn oraz ranking graczy (KDA).

link do repozytorium : https://github.com/FajnyCiulTej/tournament-system

Jak uruchomić:
1.Sklonowac repozytorium: git clone https://github.com/FajnyCiulTej/tournament-system.git
2.Przejsc do glownego folderu cd tournament-system/system-tournament
3.Zbudowac aplikacje: mvn clean install
4.Uruchomic aplikacje: mvn spring-boot:run

Po uruchomieniu dostepna jest dokumentacja: http://localhost:8080/swagger-ui/index.html
