
# Starcraft but ~~Bad~~ Good - Code Layout Explanation
Our project code is in two parts:
- Main code, Java and Gradle (APCS Final Project)
- Server code, Node.js (APCS Final Project Server)

# Running the code
We added a `desktop-1.0.jar` file to the base directory of the folder, so to run the program you can run that jar. If you're on a Mac, you have to add a `-XstartOnFirstThread` argument to the `java -jar` command, so the run command is `java -XstarOnFirstThread -jar desktop-1.0.jar`.

The `[LWJGL] [ThreadLocalUtil] Unsupported JNI version detected, this may result in a crash. Please inform LWJGL developers.` error can likely be ignored if you get it.

Alternatively, to run from the code, you can navigate to the `APCS Final Project` directory and run `./gradlew desktop:run` (for Mac/Linux, or `.\gradlew desktop:run` on Windows).

We use a remote Oracle Cloud server to host our server code, so if you want to test the server code yourself, or if our server is down, you would need to change:

- `APCS Final Project/core/src/com/github/StarcraftButGood/LoginValidator.java` -> change line 130 from
    - `URL url = new URL("http://192.9.249.213:3000");` to
    - `URL url = new URL("http://localhost:3000");`
- `APCS Final Project/core/src/com/github/StarcraftButGood/PlayerData.java` -> change line 71 from
    - `URL url = new URL("http://192.9.249.213:3000");` to
    - `URL url = new URL("http://localhost:3000");`

before running the server files and logging in. 