Live365-Search-RadioStations 
============================
1. Open a terminal window/command prompt
2. Clone this project.
3. CD into project directory
4. mvn clean install -U -Pselenium-tests
5. mvn verify -Pselenium-tests -Dbrowser=chrome (to run test on chrome)
6. mvn verify -Pselenium-tests -Dbrowser=firefox (to run test on firefox)